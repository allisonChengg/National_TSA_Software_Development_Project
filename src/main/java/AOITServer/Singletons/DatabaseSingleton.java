package AOITServer.Singletons;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DatabaseSingleton class is designed to create connections,statements and prepared statements.
 * <p>By using Database Singleton, all resources that could leak are in one
 * place allowing for users to easily not leak resources. The users first have to create a
 * connection,statement or prepared statement and get back the index
 * were the connection,statement or prepared statement is stored. Then they can access that
 * connection,statement or prepared statement using that index. By calling the {@link #close()}
 * all connections,statements and prepared statements are closed </p>
 *
 * @deprecated
 */
public class DatabaseSingleton {

    private String databaseURL;
    private static DatabaseSingleton databaseSingleton;
    private List<Connection> connections;
    private List<Statement> statements;
    private List<PreparedStatement> preparedStatements;

    /**
     *
     * @param databaseURL The database URL used to access the database.
     *
     * @deprecated
     */
    protected DatabaseSingleton( String databaseURL){
        this.databaseURL = databaseURL;
        connections = Collections.synchronizedList(new ArrayList<Connection>());
        statements = Collections.synchronizedList(new ArrayList<Statement>());
        preparedStatements = Collections.synchronizedList(new ArrayList<PreparedStatement>());
    }

    /**
     *
     *
     * @param jdbcDriver Driver needed to access database.
     * @param databaseURL The database URL used to access the database.
     * @return Returns null if Driver could not be initialized, else returns an instance of DatabaseSingleton.
     *
     * @deprecated
     */
    public static synchronized DatabaseSingleton getInstance(String jdbcDriver,String databaseURL){
        if(databaseSingleton == null){
            try {
                Class.forName(jdbcDriver);
                databaseSingleton = new DatabaseSingleton(databaseURL);

            }catch(ClassNotFoundException c){
                System.out.println("Driver initialization failed:" + c);
                return null;
            }


        }

        return databaseSingleton;
    }

    /**
     * Used to create a connection which can be accessed by the index returned
     *
     * @param username username used for login into database
     * @param password password for authentication of user
     * @return Returns -1 if connection could not be created,else returns connection.
     */
    public int createConnection(String username,String password){
        Connection c;
        try {
            System.out.println("Connection attempt started");
            c = DriverManager.getConnection(databaseURL,username,password);
            System.out.println("Connection attempt ended succesfully");
        } catch (SQLException throwables) {
            System.out.println("Creating connection failed:" + throwables);
            return -1;
        }

        connections.add(c);
        return connections.size() - 1;
    }

    /**
     * Used to create a prepared statement using connection at index and sql to be stored
     *
     * @param connectionIndex Index of created connection
     * @param sql Sql used to be stored in preparedStatement
     * @return returns -1 if creating prepared statement failed, else returns index of preparedStatement
     *
     * @deprecated
     */
    public int createPreparedStatement(int connectionIndex,String sql){
        if(connectionIndex >= connections.size() || connectionIndex < 0){
            return -1;
        }
        PreparedStatement p = null;
        try {
             p = connections.get(connectionIndex).prepareStatement(sql);
        } catch (SQLException throwables) {
            System.out.println("Creating prepared statement failed");
            return -1;
        }

        preparedStatements.add(p);

        return preparedStatements.size() - 1;
    }

    /**
     * Used to create a statement using connection at index
     * @param connectionIndex Index of created connection
     * @return Returns -1 if creation of statement failed, else returns index of statement
     *
     * @deprecated
     */
    public int createStatement(int connectionIndex) {
        if(connectionIndex >= connections.size() || connectionIndex < 0){
            return -1;
        }

        Statement s = null;

        try {
            s = connections.get(connectionIndex).createStatement();
        }catch(SQLException throwables){
            System.out.println("Creating statement failed");
            return -1;
        }

        statements.add(s);

        return statements.size() - 1;

    }

    /**
     * Used to get already created Statement at index
     *
     * @param index <p>index used to access were statement is stored that was returned by {@link #createStatement(int)}</p>
     * @return returns null if statement couldn't be gotten , else returns statement
     *
     * @deprecated
     */
    public Statement getStatement(int index){
        if(index >= statements.size() || index < 0){
            return null;
        }

        return statements.get(index);
    }

    /**
     * Used to get already created preparedStatement at index
     *
     * @param index <p>index used to access were preparedStatement is stored that was returned by
     * {@link #createPreparedStatement(int, String)}</p>
     * @return returns null if preparedStatement couldn't be gotten , else returns preparedStatement
     *
     * @deprecated
     */
    public PreparedStatement getPreparedStatement(int index){
        if(index >= preparedStatements.size() || index < 0){
            return null;
        }

        return preparedStatements.get(index);
    }

    /**
     * Used to get already created connection at index
     *
     * @param index <p>index used to access were connection is stored that was returned by
     * {@link #createConnection(String, String)}</p>
     * @return returns null if connection couldn't be gotten , else returns connection
     *
     * @deprecated
     */
    public Connection getConnection(int index){
        if(index >= connections.size() || index < 0){
            return null;
        }

        return connections.get(index);
    }

    /**
     * Used to close all connections,prepared statements and statements ever created.
     *
     * @deprecated
     */
    public void close() {
        for(int i = 0;i < statements.size();i++){
            try{
                statements.get(i).close();
                statements.remove(i);
                i--;
            }catch(SQLException throwables){
                System.out.println("Closing statement at index " + i + " did not close due to " + throwables);
            }

        }

        for(int i = 0;i < preparedStatements.size();i++){
            try{
                preparedStatements.get(i).close();
                preparedStatements.remove(i);
                i--;
            }catch(SQLException throwables){
                System.out.println("Closing prepared statement at index " + i + " did not close due to " + throwables);
            }
        }

        for(int i = 0;i < connections.size();i++){
            try{
                connections.get(i).close();
                connections.remove(i);
                i--;
            }catch(SQLException throwables){
                System.out.println("Closing statement at index " + i + " did not close due to " + throwables);
            }

        }
    }










}
