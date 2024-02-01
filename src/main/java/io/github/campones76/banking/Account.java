package io.github.campones76.banking;

import io.github.campones76.utility.print;

import java.math.BigDecimal;

public class Account {
    public String username;
    public String passwd;
    public BigDecimal balance;

    //contructor
    public Account(String username, String passwd, BigDecimal balance) {
        this.username = username;
        this.passwd = passwd;
        this.balance = balance;
    }

    //methods
    public void deposit(BigDecimal amount){
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount){
        if (balance.compareTo(amount) >= 0){
            balance = balance.subtract(amount);
        } else {
            print.ln("Insufficient balance!");
        }
    }

    public BigDecimal CheckBalance(){
        return balance;
    }



}
