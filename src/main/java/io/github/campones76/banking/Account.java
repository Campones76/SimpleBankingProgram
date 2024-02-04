package io.github.campones76.banking;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import io.github.campones76.utility.print;
import org.mindrot.jbcrypt.BCrypt;

import java.math.BigDecimal;
import java.util.Locale;

public class Account {
    public String username;
    public String hashedPassword;  // Updated field for hashed password
    public BigDecimal balance;

    //public String liban;

    // Constructor
    public Account() {
    }

    public void createAccount(String username, String passwd, BigDecimal initialBalance) {
        // Hash the password before storing it
        String hashedPassword = hashPassword(passwd);

        this.username = username;
        this.hashedPassword = hashedPassword;
        this.balance = initialBalance;
        //liban = generatePortugueseIban();
        //this.iban = generatePortugueseIban();
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

    private String generatePortugueseIban() {
        Faker faker = new Faker(new Locale("pt-PT"));
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("pt-PT"), new RandomService());

        return fakeValuesService.resolve("iban", null, faker);
    }
}
