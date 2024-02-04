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
        createHashedIbansTable();
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
                    + "balance REAL,\n"
                    + "iban TEXT NOT NULL\n"
                    + ");";

            // Execute the SQL command
            statement.execute(createTableSQL);

            print.ln("Table successfully created (if not already existing).");

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


    private static void createHashedIbansTable() {
        String url = "jdbc:sqlite:BankOfCanedo.db";
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS hashed_ibans (\n"
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + "hashed_iban TEXT NOT NULL\n"
                    + ");";

            statement.execute(createTableSQL);
            System.out.println("Hashed IBANs table successfully created (if not already existing).");

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void saveHashedIbanToDatabase(String hashedIban) {
        String url = "jdbc:sqlite:BankOfCanedo.db";
        String insertQuery = "INSERT INTO hashed_ibans(hashed_iban) VALUES (?)";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, hashedIban);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Hashed IBAN successfully saved to the database.");
            } else {
                System.out.println("Failed to save hashed IBAN to the database.");
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static boolean isIbanHashDuplicate(String hashedIban) {
        String url = "jdbc:sqlite:BankOfCanedo.db";
        String selectQuery = "SELECT hashed_iban FROM hashed_ibans WHERE hashed_iban = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, hashedIban);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }


    public static void saveToDatabase(String username, String password, BigDecimal balance, String iban) {
        String url = "jdbc:sqlite:BankOfCanedo.db";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accounts(username, password, balance, iban/*, iban*/) VALUES (?, ?, ?, ?/*, ?*/)")) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setBigDecimal(3, balance);
            preparedStatement.setString(4, iban);
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
    public static void closeResources(Connection connection, Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
