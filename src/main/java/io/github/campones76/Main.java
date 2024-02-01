package io.github.campones76;
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
        }
    }
}