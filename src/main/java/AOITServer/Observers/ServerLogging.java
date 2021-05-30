package AOITServer.Observers;

import AOITServer.Main;
import AOITServer.Singletons.DatabaseSingleton;
import AOITServer.Tables.AOITLogs;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * ServerLogging is used to log clients actions connecting to the server.
 *
 * <p>ServerLogging will automatically update the table with the new log pushed to it
 *  from a LoggingSubject, in this case {@link AccessManagerJWT}</p>
 *
 * @see LoggingObserver,AccessManagerJWT
 */
public class ServerLogging implements LoggingObserver {
    private PreparedStatement ps;
    
    private static Logger logger = LoggerFactory.getLogger(ServerLogging.class);

    /**
     *
     * @param connectionIndex Index of initialized connection from {@link DatabaseSingleton}
     * @param ds {@link DatabaseSingleton} used to get connection and create prepared statement
     */
    public ServerLogging(int connectionIndex,DatabaseSingleton ds){
        String sql = "INSERT INTO " + AOITLogs.TABLENAME + " VALUES (?)";
        int index = ds.createPreparedStatement(connectionIndex,sql);
        ps = ds.getPreparedStatement(index);
    }


    /**
     * setLog method inserts log into logging table found in database.
     * @param log Log to be inserted into the logging table.
     */
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
