package io.github.campones76;
import io.github.campones76.banking.Account;
import io.github.campones76.utility.print;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        //System.out.println("Hello world!");
        Scanner scanner = new Scanner(System.in);

        print.ln("Create an account: ");
        print.ln("Enter username: ");
        String username = scanner.nextLine();
        print.ln("Enter password: ");
        String passwd = scanner.nextLine();
        Account account = new Account(username, passwd, BigDecimal.ZERO);

        //Deposit cash
        print.ln("Enter amount to deposit: ");
        BigDecimal amount = scanner.nextBigDecimal();
        account.deposit(amount);

        //check balance
        print.ln("Current balance: " + account.CheckBalance());

        //write to CSV
        try {
            FileWriter out = new FileWriter("src/main/output/accounts/Accounts.csv", true);
            CSVFormat format = CSVFormat.DEFAULT
                    .builder()
                    .setHeader("Username", "Password", "Balance")
                    .build();
            try (CSVPrinter printer = new CSVPrinter(out, format)){
                printer.printRecord(username, passwd, account.CheckBalance());
            }
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
}