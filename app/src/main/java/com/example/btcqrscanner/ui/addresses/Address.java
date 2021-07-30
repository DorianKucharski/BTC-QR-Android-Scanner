package com.example.btcqrscanner.ui.addresses;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Address implements Comparable<Address>, Serializable {

    private static final long serialVersionUID = 1L;

    private final String address;
    private final int balance;
    private final int lastTransactionDate;

    public Address(String address) {
        this.address = address;
        this.balance = -1;
        this.lastTransactionDate = -1;
    }


    public Address(String address, int balance, int lastTransactionDate) {
        this.address = address;
        this.balance = balance;
        this.lastTransactionDate = lastTransactionDate;
    }


    public String getAddress() {
        return address;
    }

    public String getBalance() {
        if (!isChecked()){
            return "not checked";
        }

        if (!wasUsed()){
            return "not used";
        }

        float balanceFloat = (float)balance / 100000000;
        return String.format(Locale.ENGLISH, "%.8f", balanceFloat);
    }

    public String getDate() {
        if (!isChecked()){
            return "not checked";
        }

        if (!wasUsed()){
            return "not used";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        return sdf.format(new Date(lastTransactionDate * 1000L));
    }

    public boolean isChecked(){
        return lastTransactionDate != -1;
    }

    public boolean wasUsed(){
        return lastTransactionDate > 0;
    }

    @Override
    public int compareTo(Address o) {
        return Integer.compare(this.lastTransactionDate, o.lastTransactionDate);
    }

    @Override
    public String toString() {
        return "Address{" +
                "address='" + address + '\'' +
                ", balance=" + balance +
                ", lastTransactionDate=" + lastTransactionDate +
                '}';
    }

    public String toEmail(){
        return getAddress() + "\t" + getBalance() + "\t" + getDate() + "\n";
    }


}
