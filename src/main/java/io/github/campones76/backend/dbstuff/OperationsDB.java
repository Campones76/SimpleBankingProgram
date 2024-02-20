package io.github.campones76.backend.dbstuff;

import io.github.campones76.banking.Account;
import io.github.campones76.backend.dbstuff.ConnectionDB;
import io.github.campones76.utility.print;
import org.mindrot.jbcrypt.BCrypt;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class OperationsDB {
    public static Account getAccountFromDB(String username){
        String SERVER_NAME = "192.168.1.70";
        String DATABASE_NAME = "BankOfCanedo";
        String UID = "boc";
        String PWD = "VeryStr0ngP@ssw0rd";

        String connectionString = String.format("jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s;trustServerCertificate=true",
                SERVER_NAME, DATABASE_NAME, UID, PWD);

        try(Connection connection = DriverManager.getConnection(connectionString);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE username = ?")){
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String passwd = resultSet.getString("password");
                BigDecimal balance = resultSet.getBigDecimal("balance");
                Account account = new Account();
                account.createAccount(username, passwd, balance); // Use the password from the database
                return account;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static boolean verifyPassword(String username, String inputPassword) {
        String SERVER_NAME = "192.168.1.70";
        String DATABASE_NAME = "BankOfCanedo";
        String UID = "boc";
        String PWD = "VeryStr0ngP@ssw0rd";

        String connectionString = String.format("jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s;trustServerCertificate=true",
                SERVER_NAME, DATABASE_NAME, UID, PWD);

        int maxAttempts = 3;
        int attempts = 0;

        try(Connection connection = DriverManager.getConnection(connectionString);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT password FROM accounts WHERE username = ?")){
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            Scanner scanner = new Scanner(System.in);

            if (resultSet.next()) {
                String passwd = resultSet.getString("password");
                while (attempts < maxAttempts - 1){
                    // Check the input password against the hashed password from the database
                    if (BCrypt.checkpw(inputPassword, passwd)){
                        return true;
                    } else {
                        attempts++;
                        print.ln("Incorrect password. Attempts left: " + (maxAttempts - attempts));

                        // Prompt the user for password again
                        print.ln("Password: ");
                        inputPassword = scanner.nextLine();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false; // Return false if the user is not found
    }

    public static void updateBalanceInDatabase(String username, BigDecimal newBalance) {
        String SERVER_NAME = "192.168.1.70";
        String DATABASE_NAME = "BankOfCanedo";
        String UID = "boc";
        String PWD = "VeryStr0ngP@ssw0rd";

        String connectionString = String.format("jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s;trustServerCertificate=true",
                SERVER_NAME, DATABASE_NAME, UID, PWD);

        String updateQuery = "UPDATE accounts SET balance = ? WHERE username = ?";
        try(Connection connection = DriverManager.getConnection(connectionString);
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setBigDecimal(1, newBalance);
            preparedStatement.setString(2, username);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Balance updated in the database for user: " + username);
            } else {
                System.out.println("Failed to update balance in the database for user: " + username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void closeResources(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
