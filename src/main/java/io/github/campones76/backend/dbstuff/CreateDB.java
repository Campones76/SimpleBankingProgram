package io.github.campones76.backend.dbstuff;

import io.github.campones76.utility.print;

import java.math.BigDecimal;
import java.sql.*;

public class CreateDB {

    public static void main(String[] args) {
        createDatabaseAndTableIfNotExists();
    }

    public static void createDatabaseAndTableIfNotExists() {
        createDatabase();
        createTable();
    }

    private static void createDatabase() {
        String url = "jdbc:sqlite:BankOfCanedo.db";

        try (Connection connection = DriverManager.getConnection(url)) {
            print.ln("Database connection successful.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void createTable() {
        String url = "jdbc:sqlite:BankOfCanedo.db";

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {

            // SQL command to create a table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS accounts (\n"
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + "username TEXT NOT NULL,\n"
                    + "password TEXT NOT NULL,\n"
                    + "balance REAL/*,*/\n"
                    /*+ "iban TEXT NOT NULL\n"*/
                    + ");";

            // Execute the SQL command
            statement.execute(createTableSQL);

            print.ln("Table successfully created (if not already existing).");

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    public static void saveToDatabase(String username, String password, BigDecimal balance/*, String iban*/) {
        String url = "jdbc:sqlite:BankOfCanedo.db";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accounts(username, password, balance/*, iban*/) VALUES (?, ?, ?/*, ?*/)")) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setBigDecimal(3, balance);
            //preparedStatement.setString(4, iban);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                print.ln("Account successfully saved to the database.");
            } else {
                print.ln("Failed to save account to the database.");
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
