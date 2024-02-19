package io.github.campones76.backend.dbstuff;

import java.sql.*;

public class SqlServerExample {
    public static void main(String[] args) {

        String SERVER_NAME = "DESKTOP-HL1EFJ9"; // Replace with your server name
        String DATABASE_NAME = "BankOfCanedo"; // Replace with your database name
        String UID = "boc"; // Replace with your username
        String PWD = "1234"; // Replace with your password

        /*String connectionString =
                "jdbc:sqlserver://DESKTOP-HL1EFJ9:1433;database=BankOfCanedo;integratedSecurity=true";*/

        String connectionString = String.format("jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s;trustServerCertificate=true",
                SERVER_NAME, DATABASE_NAME, UID, PWD);

        try {
            Connection connection = DriverManager.getConnection(connectionString);//jdbcUrl
            // Use the connection for executing queries or other database operations
            System.out.println("Connected successfully!");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM [Table]");

            while ( resultSet.next()){
                System.out.println(resultSet.getString("id"));
            }
            // Don't forget to close the connection when done
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
