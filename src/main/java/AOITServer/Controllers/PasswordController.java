package AOITServer.Controllers;

import AOITServer.Adapters.EmailClient;
import AOITServer.JsonClasses.MessageJson;
import AOITServer.Singletons.DatabaseSingleton;
import AOITServer.Tables.AOITEmailKeyTable;
import AOITServer.Tables.AOITUsersTable;
import io.javalin.http.Handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;

public class PasswordController {
    private PreparedStatement psGetEmail;
    private PreparedStatement psSetEmailKey;
    private PreparedStatement psGetKeyInformation;
    private PreparedStatement psDeleteKeyInformation;
    private PreparedStatement psUpdateUserPassword;

    public PasswordController(int connectionIndex,DatabaseSingleton ds){
        String sql1 = String.format("SELECT %s FROM %s WHERE %s = ?",
                AOITUsersTable.EMAIL,AOITUsersTable.TABLENAME,AOITUsersTable.USERNAME);
        String sql2 = String.format("INSERT INTO %s VALUES (?,?,?) ON DUPLICATE KEY UPDATE %s = ?, %s = ?;",
                AOITEmailKeyTable.TABLENAME,AOITEmailKeyTable.VERIFICATIONKEY,AOITEmailKeyTable.EXPIRATIONTIME);

        String sql3 = String.format("SELECT * FROM %s WHERE %s = ?",AOITEmailKeyTable.TABLENAME,AOITEmailKeyTable.USERNAME);

        String sql4 = String.format("DELETE FROM %s WHERE %s = ?",
                AOITEmailKeyTable.TABLENAME,AOITEmailKeyTable.USERNAME);

        String sql5 = String.format("UPDATE %s SET %s = ? WHERE %s = ?",
                AOITUsersTable.TABLENAME,AOITUsersTable.PASSWORD,AOITUsersTable.USERNAME);

        int index = ds.createPreparedStatement(connectionIndex,sql1);
        psGetEmail = ds.getPreparedStatement(index);
        int index2 = ds.createPreparedStatement(connectionIndex,sql2);
        psSetEmailKey = ds.getPreparedStatement(index2);

        int index3 = ds.createPreparedStatement(connectionIndex,sql3);
        psGetKeyInformation = ds.getPreparedStatement(index3);

        int index4 = ds.createPreparedStatement(connectionIndex,sql4);
        psDeleteKeyInformation = ds.getPreparedStatement(index4);

        int index5 = ds.createPreparedStatement(connectionIndex,sql5);
        psUpdateUserPassword = ds.getPreparedStatement(index5);

    }
    public Handler sendValidationKey(EmailClient ce){
        return ctx -> {
            String username = ctx.queryParam("Username");
            if (username == null) {
                ctx.status(404).json(new MessageJson(false,"Query parameter \"Username\" not found"));
            }
            else{
                psGetEmail.setString(1,username);

                try {
                    ResultSet rs = psGetEmail.executeQuery();

                    if(rs.next()){
                        String email = rs.getString(AOITUsersTable.EMAIL);
                        rs.close();
                        Random rnd = new Random();
                        String key = String.format("%06d", rnd.nextInt(999999));
                        psSetEmailKey.setString(1,username);
                        psSetEmailKey.setString(2,key);
                        psSetEmailKey.setTimestamp(3, Timestamp.from(Instant.now().plusSeconds(3 * 3600)));
                        psSetEmailKey.setString(4,key);
                        psSetEmailKey.setTimestamp(5,Timestamp.from(Instant.now().plusSeconds(3 * 3600)));

                        psSetEmailKey.execute();

                        if(!ce.sendEmail("Password Reset",key,email)){
                            ctx.status(404).json(new MessageJson(false,"Email could not be sent"));
                        }
                        else{
                            ctx.json(new MessageJson(true,""));
                        }
                    }
                    else{
                        ctx.status(404).json(new MessageJson(false,"Email not set by user"));
                    }


                }catch(SQLException s){
                    ctx.status(404).json(new MessageJson(false,"Could not send validation key"));
                }


            }


        };
    }

    public Handler changePassword(){
        return ctx ->{
           String username = ctx.queryParam("Username");
           String password = ctx.queryParam("NewPassword");
           String validationKey = ctx.queryParam("Key");

            if (username == null) {
                ctx.status(404).json(new MessageJson(false,"Query parameter \"Username\" not found"));
            }
            else if(password == null){
                ctx.status(404).json(new MessageJson(false,"Query parameter \"NewPassword\" not found"));
            }
            else if(validationKey == null){
                ctx.status(404).json(new MessageJson(false,"Query parameter \"Key\" not found"));
            }
            else{
                psGetKeyInformation.setString(1,username);
                try {
                    ResultSet rs = psGetKeyInformation.executeQuery();
                    if(rs.next()) {

                        if (rs.getString(AOITEmailKeyTable.VERIFICATIONKEY).equals(validationKey) &&
                                Timestamp.from(Instant.now()).before(rs.getTimestamp(AOITEmailKeyTable.EXPIRATIONTIME))) {

                            psUpdateUserPassword.setString(1, password);
                            psUpdateUserPassword.setString(2, username);
                            psUpdateUserPassword.execute();

                            psDeleteKeyInformation.setString(1, username);

                            psDeleteKeyInformation.execute();



                        } else {
                            ctx.status(401).json(new MessageJson(false, "Unauthorized"));
                        }
                        rs.close();
                    }
                    else{
                        ctx.status(404).json(new MessageJson(false,"Validation key does not exist"));
                    }

                }catch(SQLException e){
                    System.out.println(e);
                    ctx.status(404).json(new MessageJson(false,"Could not reset password"));
                }

            }
        };
    }
}
