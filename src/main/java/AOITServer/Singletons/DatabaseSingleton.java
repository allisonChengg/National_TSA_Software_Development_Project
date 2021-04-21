package AOITServer.Singletons;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
    Database Singleton allows for creation of connections,statements and prepared statements.
    stores all of them in synchronized lists and will close connections,statements and prepared statements
    when calling close() method. has accessors for connections,statements and prepared statements using index.
    This class is a singleton.
 */

public class DatabaseSingleton {

    private String databaseURL;
    private static DatabaseSingleton databaseSingleton;
    private List<Connection> connections;
    private List<Statement> statements;
    private List<PreparedStatement> preparedStatements;

    protected DatabaseSingleton( String databaseURL){
        this.databaseURL = databaseURL;
        connections = Collections.synchronizedList(new ArrayList<Connection>());
        statements = Collections.synchronizedList(new ArrayList<Statement>());
        preparedStatements = Collections.synchronizedList(new ArrayList<PreparedStatement>());
    }

    //returns the instance of DatabaseSingleton
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

    //creates a connection by specifying the username and the password. Returns index of connection.
    public int createConnection(String username,String password){
        Connection c;
        try {
            c = DriverManager.getConnection(databaseURL,username,password);
        } catch (SQLException throwables) {
            System.out.println("Creating connection failed:" + throwables);
            return -1;
        }

        connections.add(c);
        return connections.size() - 1;
    }

    //creates a prepared statement by specifying the index of a connection and sql for prepared statement.
    // Returns index of preparedStatement in list.
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

    //creates a statement by specifying the index of a connection f.
    // Returns index of statement in list.
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


    public Statement getStatement(int index){
        if(index >= statements.size() || index < 0){
            return null;
        }

        return statements.get(index);
    }

    public PreparedStatement getPreparedStatement(int index){
        if(index >= preparedStatements.size() || index < 0){
            return null;
        }

        return preparedStatements.get(index);
    }

    public Connection getConnection(int index){
        if(index >= connections.size() || index < 0){
            return null;
        }

        return connections.get(index);
    }

    //closes all statements,prepared statements and connections
    public void close() {
        for(int i = 0;i < statements.size();i++){
            try{
                statements.get(i).close();
                statements.remove(i);
            }catch(SQLException throwables){
                System.out.println("Closing statement at index " + i + " did not close due to " + throwables);
            }

        }

        for(int i = 0;i < preparedStatements.size();i++){
            try{
                preparedStatements.get(i).close();
                preparedStatements.remove(i);
            }catch(SQLException throwables){
                System.out.println("Closing prepared statement at index " + i + " did not close due to " + throwables);
            }
        }

        for(int i = 0;i < connections.size();i++){
            try{
                connections.get(i).close();
                connections.remove(i);
            }catch(SQLException throwables){
                System.out.println("Closing statement at index " + i + " did not close due to " + throwables);
            }

        }
    }










}
