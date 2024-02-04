package io.github.campones76;
import io.github.campones76.backend.dbstuff.CreateDB;
import io.github.campones76.backend.dbstuff.OperationsDB;
import io.github.campones76.banking.Account;
import io.github.campones76.utility.print;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        CreateDB.createDatabaseAndTableIfNotExists();
        //System.out.println("Hello world!");
        Scanner scanner = new Scanner(System.in);
        print.ln("1. Open an account");
        print.ln("2. Already a customer? Login");
        int choiceStart = scanner.nextInt();
        scanner.nextLine();
        if (choiceStart == 1) {
            print.ln("Enter username: ");
            String username = scanner.nextLine();
            print.ln("Enter password: ");
            String passwd = scanner.nextLine();
            //Account account = new Account(username, passwd, BigDecimal.ZERO);
            Account account = new Account();

            account.createAccount(username, passwd, BigDecimal.ZERO);

            //Deposit cash
            print.ln("Enter amount to deposit: ");
            BigDecimal amount = scanner.nextBigDecimal();
            account.deposit(amount);

            //check balance
            print.ln("Current balance: " + account.CheckBalance());

        /*//write to CSV
        try {
            String filename = "src/main/output/accounts/Accounts.csv";
            boolean fileIsEmpty = !new File(filename).exists() || Files.readAllLines(Paths.get(filename)).isEmpty();
            FileWriter out = new FileWriter(filename, true);
            CSVFormat.Builder builder = CSVFormat.DEFAULT.builder();
            if (fileIsEmpty){
                builder.setHeader("Username", "Password", "Balance");
            }
            CSVFormat format = builder.build();
            try (CSVPrinter printer = new CSVPrinter(out, format)){
                printer.printRecord(username, passwd, account.CheckBalance());
            }
        } catch (IOException e) {
           e.printStackTrace();
        }*/
            // Save to SQLite database
            CreateDB.saveToDatabase(username, account.hashedPassword, account.CheckBalance()/*, account.iban*/);
        } else if (choiceStart == 2){
            print.ln("Username: ");
            String username = scanner.nextLine();
            print.ln("Password: ");
            String passwd = scanner.nextLine();
            Account account = OperationsDB.getAccountFromDB(username);
            if (account != null && OperationsDB.verifyPassword(username, passwd)){
                print.ln("Login Successful");
                boolean loggedIn = true;
                while (loggedIn) {
                    print.ln("================");
                    print.ln("Welcome back, " + username);
                    print.ln("Current Balance: " + account.CheckBalance() + "€");
                    print.ln("\n");
                    print.ln("1. Deposit");
                    print.ln("2. Withdraw");
                    print.ln("3. Logout");
                    print.ln("Enter choice: ");

                    int ChoiceLogged = scanner.nextInt();
                    scanner.nextLine(); //skips to the next line
                    switch (ChoiceLogged){
                        case 1:
                            print.ln("Enter amount to deposit: ");
                            BigDecimal DepositAmount = scanner.nextBigDecimal();
                            account.deposit(DepositAmount);
                            print.ln("New balance: " + account.CheckBalance());

                            OperationsDB.updateBalanceInDatabase(username, account.CheckBalance());
                            break;
                        case 2:
                            print.ln("Enter amount to withdraw: ");
                            BigDecimal withdrawamount = scanner.nextBigDecimal();
                            account.withdraw(withdrawamount);
                            print.ln("New balance: " + account.CheckBalance());
                            OperationsDB.updateBalanceInDatabase(username, account.CheckBalance());
                            break;

                        case 3:
                            print.ln("Logged Out!");
                            loggedIn = false;
                            break;
                    }
                    /*if (ChoiceLogged == 1) {
                        print.ln("Test Deposit");
                    } else if (ChoiceLogged == 2) {
                        print.ln("Test Withdraw");
                    } else if (ChoiceLogged == 3){
                        print.ln("Test Logout");
                    }*/

                }

            }

        }
    }
}