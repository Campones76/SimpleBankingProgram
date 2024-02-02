package io.github.campones76.banking;

import io.github.campones76.utility.print;
import org.mindrot.jbcrypt.BCrypt;

import java.math.BigDecimal;

public class Account {
    public String username;
    public String hashedPassword;  // Updated field for hashed password
    public BigDecimal balance;

    // Constructor
    public Account() {
    }

    public void createAccount(String username, String passwd, BigDecimal initialBalance) {
        // Hash the password before storing it
        String hashedPassword = hashPassword(passwd);

        this.username = username;
        this.hashedPassword = hashedPassword;
        this.balance = initialBalance;
    }

    // Methods
    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) >= 0) {
            balance = balance.subtract(amount);
        } else {
            print.ln("Insufficient balance!");
        }
    }

    public BigDecimal CheckBalance() {
        return balance;
    }

    private String hashPassword(String password) {
        // Generate a salt and hash the password with bcrypt
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyPassword(String inputPassword) {
        // Check if the input password matches the stored hashed password
        return BCrypt.checkpw(inputPassword, hashedPassword);
    }
}
