package io.github.campones76.backend.dbstuff;

import io.github.campones76.banking.Account;
import io.github.campones76.backend.dbstuff.ConnectionDB;
import org.mindrot.jbcrypt.BCrypt;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OperationsDB {
    public static Account getAccountFromDB(String username){
        String url = ConnectionDB.url;
        try(java.sql.Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement("Select * FROM accounts WHERE username = ?")){
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
        String url = ConnectionDB.url;
        try(java.sql.Connection connection2 = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection2.prepareStatement("Select password FROM accounts WHERE username = ?")){
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String passwd = resultSet.getString("password");
                return BCrypt.checkpw(inputPassword, passwd); // Check the input password against the hashed password from the database
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false; // Return false if the user is not found
    }

    public static void updateBalanceInDatabase(String username, BigDecimal newBalance) {
        String url = ConnectionDB.url;
        String updateQuery = "UPDATE accounts SET balance = ? WHERE username = ?";
        try(java.sql.Connection connection3 = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection3.prepareStatement(updateQuery)) {
            preparedStatement.setBigDecimal(1, newBalance);
            preparedStatement.setString(2, username);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Balance updated in the database for user: " + username);
            } else {
                System.out.println("Failed to update balance in the database for user: " + username);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Handle the exception according to your application's needs.
        }
    }
}