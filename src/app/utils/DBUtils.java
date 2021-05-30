package app.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {

    public static Connection getConnectionWith(String dbName){
        Connection connection = null;
        try {
            String jdbcurl= "jdbc:sqlite:src/app/db/" + dbName;
            connection = DriverManager.getConnection(jdbcurl);
            System.out.println("Connected!");
        } catch (SQLException e ) {
            System.out.println(e);
        }

        return connection;
    }

}
