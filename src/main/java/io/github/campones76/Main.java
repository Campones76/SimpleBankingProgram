package io.github.campones76;

import io.github.campones76.backend.dbstuff.CreateDB;
import io.github.campones76.backend.dbstuff.OperationsDB;
import io.github.campones76.banking.Account;
import io.github.campones76.utility.print;

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

            Account account = new Account();
            account.createAccount(username, passwd, BigDecimal.ZERO);

            print.ln("Enter amount to deposit: ");
            BigDecimal amount = scanner.nextBigDecimal();
            account.deposit(amount);

            print.ln("Current balance: " + account.CheckBalance());

            // Save account to database
            CreateDB.saveToDatabase(username, account.getHashedPassword(), account.CheckBalance(), account.getIban());
            scanner.close();
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
        } else if (choiceStart == 2) {
            print.ln("Username: ");
            String username = scanner.nextLine();
            print.ln("Password: ");
            String passwd = scanner.nextLine();
            Account account = OperationsDB.getAccountFromDB(username);
            if (account != null && OperationsDB.verifyPassword(username, passwd)) {
                print.ln("Login Successful");
                boolean loggedIn = true;
                while (loggedIn) {
                    print.ln("================");
                    print.ln("Welcome back, " + username);
                    print.ln("Current Balance: " + account.CheckBalance() + "€");
                    print.ln("\n");
                    print.ln("1. Check IBAN");
                    print.ln("2. Deposit");
                    print.ln("3. Withdraw");
                    print.ln("4. Send money");
                    print.ln("5. Logout");
                    print.ln("Enter choice: ");

                    int ChoiceLogged = scanner.nextInt();
                    scanner.nextLine(); // Skips to the next line
                    switch (ChoiceLogged) {
                        case 1:
                            print.ln("Your IBAN is: " + account.getIban());
                            print.ln("");
                            print.ln("Press Enter key to continue...");
                            try {
                                System.in.read();
                                // Clear the input buffer
                                while (System.in.available() > 0) {
                                    System.in.read();
                                }
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            print.ln("Enter amount to deposit: ");
                            BigDecimal DepositAmount = scanner.nextBigDecimal();
                            account.deposit(DepositAmount);
                            print.ln("New balance: " + account.CheckBalance());
                            OperationsDB.updateBalanceInDatabase(username, account.CheckBalance());
                            break;
                        case 3:
                            print.ln("Enter amount to withdraw: ");
                            BigDecimal withdrawamount = scanner.nextBigDecimal();
                            account.withdraw(withdrawamount);
                            print.ln("New balance: " + account.CheckBalance());
                            OperationsDB.updateBalanceInDatabase(username, account.CheckBalance());
                            break;
                        case 4:
                            print.ln("Enter destination's IBAN: ");
                            String destIban = scanner.nextLine();
                            if (OperationsDB.doesIbanExist(destIban)) {
                                print.ln("Enter amount to send: ");
                                BigDecimal amounttosend = scanner.nextBigDecimal();
                                //OperationsDB.updateBalanceInDatabaseIBAN(destIban, amounttosend);
                                account.amounttosend(amounttosend,destIban);
                                account.withdraw(amounttosend);
                                OperationsDB.updateBalanceInDatabase(username, account.CheckBalance());
                            } else{
                                print.ln("Iban doesn't exist");
                        }

                            break;
                        case 5:
                            print.ln("Logged Out!");
                            loggedIn = false;
                            break;
                    }
                }
            }
            scanner.close();
        }
    }
}
