package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static DBConnection dbConnection;
    private Connection connection;

    private DBConnection() throws SQLException {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist","root","root");


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
    public static DBConnection getInstance() throws SQLException {

        return(dbConnection == null) ? dbConnection =new DBConnection() : dbConnection;
    }

    public Connection getConnection(){
        return connection;
    }

}
