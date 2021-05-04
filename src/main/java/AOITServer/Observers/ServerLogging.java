package AOITServer.Observers;

import AOITServer.Main;
import AOITServer.Singletons.DatabaseSingleton;
import AOITServer.Tables.AOITLogs;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ServerLogging implements LoggingObserver {
    private PreparedStatement ps;
    
    private static Logger logger = LoggerFactory.getLogger(ServerLogging.class);

    public ServerLogging(int connectionIndex,DatabaseSingleton ds){
        String sql = "INSERT INTO " + AOITLogs.TABLENAME + " VALUES (?)";
        int index = ds.createPreparedStatement(connectionIndex,sql);
        ps = ds.getPreparedStatement(index);
    }

    @Override
    public void setLog(String log){
    	logger.info(log);
        try {
            ps.setString(1, log);
            
            ps.execute();
        }catch(SQLException s){
            System.out.println("Logging failed: " + s);
        }
    }
}
