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
        String SERVER_NAME = "192.168.1.70";
        String DATABASE_NAME = "BankOfCanedo";
        String UID = "boc";
        String PWD = "VeryStr0ngP@ssw0rd";

        String connectionString = String.format("jdbc:sqlserver://%s;user=%s;password=%s;trustServerCertificate=true",
                SERVER_NAME, UID, PWD);

        try (Connection connection = DriverManager.getConnection(connectionString);
             Statement statement = connection.createStatement()) {

            // Check if the database already exists
            String checkDatabaseExistsSQL = "SELECT COUNT(*) AS db_count FROM sys.databases WHERE name = '" + DATABASE_NAME + "'";
            ResultSet resultSet = statement.executeQuery(checkDatabaseExistsSQL);
            resultSet.next();
            int dbCount = resultSet.getInt("db_count");

            if (dbCount == 0) {
                // Database doesn't exist, so create it
                String createDatabaseSQL = "CREATE DATABASE " + DATABASE_NAME;
                statement.execute(createDatabaseSQL);
                print.ln("New database created: " + DATABASE_NAME);
            } else {
                print.ln("Using existing database: " + DATABASE_NAME);
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


    private static void createTable() {
        String SERVER_NAME = "192.168.1.70";
        String DATABASE_NAME = "BankOfCanedo";
        String UID = "boc";
        String PWD = "VeryStr0ngP@ssw0rd";

        String connectionString = String.format("jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s;trustServerCertificate=true",
                SERVER_NAME, DATABASE_NAME, UID, PWD);

        try (Connection connection = DriverManager.getConnection(connectionString);
             Statement statement = connection.createStatement()) {

            // Check if the table already exists
            String checkTableExistsSQL = "SELECT COUNT(*) AS table_count FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'dbo' AND TABLE_NAME = 'accounts'";
            ResultSet resultSet = statement.executeQuery(checkTableExistsSQL);
            resultSet.next();
            int tableCount = resultSet.getInt("table_count");

            if (tableCount == 0) {
                // Table doesn't exist, so create it
                String createTableSQL = "CREATE TABLE accounts (\n"
                        + "id INT PRIMARY KEY IDENTITY(1,1),\n"  // IDENTITY for auto-increment in SQL Server
                        + "username NVARCHAR(255) NOT NULL,\n"  // NVARCHAR for variable-length strings
                        + "password NVARCHAR(255) NOT NULL,\n"
                        + "balance DECIMAL(10, 2),\n"  // DECIMAL for exact numeric data types absolutely needed when handling money
                        + "iban NVARCHAR(50) NOT NULL\n"
                        + ")";
                statement.execute(createTableSQL);
                System.out.println("Table successfully created.");
            } else {
                System.out.println("Table 'accounts' already exists.");
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }



    /*private static void createHashedIbansTable() {
        String SERVER_NAME = "192.168.1.70";
        String DATABASE_NAME = "BankOfCanedo";
        String UID = "boc";
        String PWD = "VeryStr0ngP@ssw0rd";

        String connectionString = String.format("jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s;trustServerCertificate=true",
                SERVER_NAME, DATABASE_NAME, UID, PWD);

        try (Connection connection = DriverManager.getConnection(connectionString);
             Statement statement = connection.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS hashed_ibans (\n"
                    + "id INT PRIMARY KEY IDENTITY(1,1),\n"
                    + "hashed_iban NVARCHAR(255) NOT NULL\n"
                    + ")";

            statement.execute(createTableSQL);
            System.out.println("Hashed IBANs table successfully created (if not already existing).");

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }*/

    private static void createHashedIbansTable() {
        String SERVER_NAME = "192.168.1.70";
        String DATABASE_NAME = "BankOfCanedo";
        String UID = "boc";
        String PWD = "VeryStr0ngP@ssw0rd";

        String connectionString = String.format("jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s;trustServerCertificate=true",
                SERVER_NAME, DATABASE_NAME, UID, PWD);

        try (Connection connection = DriverManager.getConnection(connectionString);
             Statement statement = connection.createStatement()) {

            // Check if the table already exists
            String checkTableExistsSQL = "SELECT COUNT(*) AS table_count FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'dbo' AND TABLE_NAME = 'hashed_ibans'";
            ResultSet resultSet = statement.executeQuery(checkTableExistsSQL);
            resultSet.next();
            int tableCount = resultSet.getInt("table_count");

            if (tableCount == 0) {
                // Table doesn't exist, so create it
                String createTableSQL = "CREATE TABLE hashed_ibans (\n"
                        + "id INT PRIMARY KEY IDENTITY(1,1),\n"
                        + "hashed_iban NVARCHAR(255) NOT NULL\n"
                        + ")";
                statement.execute(createTableSQL);
                System.out.println("Hashed IBANs table successfully created.");
            } else {
                System.out.println("Hashed IBANs table already exists.");
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void saveHashedIbanToDatabase(String hashedIban) {
        String SERVER_NAME = "192.168.1.70";
        String DATABASE_NAME = "BankOfCanedo";
        String UID = "boc";
        String PWD = "VeryStr0ngP@ssw0rd";

        String connectionString = String.format("jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s;trustServerCertificate=true",
                SERVER_NAME, DATABASE_NAME, UID, PWD);

        String insertQuery = "INSERT INTO hashed_ibans(hashed_iban) VALUES (?)";
        try (Connection connection = DriverManager.getConnection(connectionString);
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
        String SERVER_NAME = "192.168.1.70";
        String DATABASE_NAME = "BankOfCanedo";
        String UID = "boc";
        String PWD = "VeryStr0ngP@ssw0rd";

        String connectionString = String.format("jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s;trustServerCertificate=true",
                SERVER_NAME, DATABASE_NAME, UID, PWD);

        String selectQuery = "SELECT hashed_iban FROM hashed_ibans WHERE hashed_iban = ?";
        try (Connection connection = DriverManager.getConnection(connectionString);
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
        String SERVER_NAME = "192.168.1.70";
        String DATABASE_NAME = "BankOfCanedo";
        String UID = "boc";
        String PWD = "VeryStr0ngP@ssw0rd";

        String connectionString = String.format("jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s;trustServerCertificate=true",
                SERVER_NAME, DATABASE_NAME, UID, PWD);

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accounts(username, password, balance, iban) VALUES (?, ?, ?, ?)")) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setBigDecimal(3, balance);
            preparedStatement.setString(4, iban);

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