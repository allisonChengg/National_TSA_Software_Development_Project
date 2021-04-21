package AOITServer.Observers;

import AOITServer.Singletons.DatabaseSingleton;
import AOITServer.Tables.AOITLogs;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServerLogging implements LoggingObserver {
    private PreparedStatement ps;

    public ServerLogging(int connectionIndex,DatabaseSingleton ds){
        String sql = "INSERT INTO " + AOITLogs.TABLENAME + " VALUES (?)";
        int index = ds.createPreparedStatement(connectionIndex,sql);
        ps = ds.getPreparedStatement(index);
    }

    @Override
    public void setLog(String log){
        try {
            ps.setString(1, log);

            ps.execute();
        }catch(SQLException s){
            System.out.println("Logging failed: " + s);
        }
    }
}
