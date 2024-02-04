package io.github.campones76.banking;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import io.github.campones76.backend.dbstuff.CreateDB;
import io.github.campones76.utility.print;
import org.mindrot.jbcrypt.BCrypt;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Account {
    public String username;
    public String hashedPassword;
    public BigDecimal balance;
    private String iban;


    private static Set<String> generatedIBANs = new HashSet<>();
    public String getIban() {
        return iban;
    }



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
        this.iban = generateUniqueIban();
        //liban = generatePortugueseIban();
        //this.iban = generatePortugueseIban();
    }

    // Methods

    public void setIban(String iban) {
        this.iban = iban;
    }
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

    private String hashIban(String iban) {
        // Add logic to hash the IBAN (similar to password hashing)
        return BCrypt.hashpw(iban, BCrypt.gensalt());
    }
    private String hashPassword(String password) {
        // Generate a salt and hash the password with bcrypt
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private String generateUniqueIban() {
        String iban;
        do {
            iban = generatePortugueseIban();
        } while (generatedIBANs.contains(iban) || isIbanHashDuplicate(hashIban(iban)));

        generatedIBANs.add(iban);

        // Save hashed IBAN to the database
        CreateDB.saveHashedIbanToDatabase(hashIban(iban));

        return iban;
    }

    private boolean isIbanHashDuplicate(String hashedIban) {
        return CreateDB.isIbanHashDuplicate(hashedIban);
    }

    private String generatePortugueseIban() {
        // PTkk BBBB CCCC CCCC CCCC CCCK KK
        // Where 'k' is the check digit, 'B' is the bank identifier, and 'C' is the account number
        Faker faker = new Faker(new Locale("pt-PT"));
        String bankIdentifier = faker.bool().bool() ? "1212" : String.format("%04d", faker.number().numberBetween(1, 10000));
        //String bankIdentifier = String.format("%04d", faker.number().numberBetween(1, 10000));

        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            accountNumber.append(faker.number().digit());
        }

        String checkDigit = faker.number().digit();

        return "PT" + checkDigit + bankIdentifier + accountNumber + checkDigit;
    }
}
