package AOITServer.Controllers;

import AOITServer.Factories.JWTFactory;
import AOITServer.JsonClasses.MessageJson;
import AOITServer.JsonClasses.JWTJson;
import AOITServer.Singletons.DatabaseSingleton;
import AOITServer.Tables.AOITUsersTable;
import io.javalin.http.Handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class UserController {
    private PreparedStatement psCreateUser;
    private PreparedStatement psLogin;
    public UserController(int connectionIndex,DatabaseSingleton ds){
        String sql1 = String.format("INSERT INTO %s (%s,%s,%s) VALUES (?,?,?)",
                AOITUsersTable.TABLENAME, AOITUsersTable.USERNAME, AOITUsersTable.PASSWORD
                , AOITUsersTable.ROLE);

        String sql2 = String.format("SELECT %s FROM %s WHERE %s = ? AND %s = ?",AOITUsersTable.ROLE,
                AOITUsersTable.TABLENAME,AOITUsersTable.USERNAME,AOITUsersTable.PASSWORD);

        int index1 = ds.createPreparedStatement(connectionIndex,sql1);
        int index2 = ds.createPreparedStatement(connectionIndex,sql2);

        psCreateUser = ds.getPreparedStatement(index1);
        psLogin = ds.getPreparedStatement(index2);

    }
    public Handler createUser(){
        return ctx ->{
            String username = ctx.queryParam("Username");
            String password = ctx.queryParam("Password");
            if(username == null){
                ctx.status(404).json(new MessageJson(false,"Query parameter \"Username\" not found"));
            }
            else if(password == null){
                ctx.status(404).json(new MessageJson(false,"Query parameter \"Password\" not found"));
            }
            else{
                psCreateUser.setString(1,username);
                psCreateUser.setString(2,password);
                psCreateUser.setInt(3,1);

                try {
                    psCreateUser.execute();
                }catch(SQLIntegrityConstraintViolationException e){
                    ctx.status(404).json(new MessageJson(false,"Username already exists"));
                }catch(SQLException e){
                    System.out.println(e);
                    ctx.status(404).json(new MessageJson(false,"Could not create User"));
                }
            }
        };
    }

    public Handler loginUser(JWTFactory jwtFactory){
        return ctx ->{

            String username = ctx.queryParam("Username");
            String password = ctx.queryParam("Password");
            if(username == null){
                ctx.status(404).json(new MessageJson(false,"Query parameter \"Username\" not found"));
            }
            else if(password == null){
                ctx.status(404).json(new MessageJson(false,"Query parameter \"Password\" not found"));
            }
            else{
                psLogin.setString(1,username);
                psLogin.setString(2,password);

                try {
                    ResultSet rs = psLogin.executeQuery();

                    if(rs.next()){
                        int role = rs.getInt(AOITUsersTable.ROLE);
                        ctx.json(new JWTJson(jwtFactory.createToken(username,role + "")));
                    }

                    else{
                        ctx.status(401).json(new MessageJson(false,"Unauthorized"));
                    }

                    rs.close();

                }catch(SQLException e){
                    ctx.status(401).json(new MessageJson(false,"Unauthorized"));
                }

            }
        };
    }

    public Handler validateToken(){
        return ctx ->{
          ctx.json(new MessageJson(true,""));
        };
    }


}
