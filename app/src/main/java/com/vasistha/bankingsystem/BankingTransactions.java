package com.vasistha.bankingsystem;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vasistha.bankingsystem.ModelClasses.Amount;
import com.vasistha.bankingsystem.ModelClasses.Transactions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ashutosh Singh on 16-Feb-18.
 */

public class BankingTransactions
{
    public String formattedDate;
    public String userID;

    public Amount amount;
    public Transactions transactions;

    public DatabaseReference databaseTransactions;
    public DatabaseReference databaseAmount;

    public BankingTransactions()
    {
        amount = new Amount();
        databaseAmount = FirebaseDatabase.getInstance().getReference("Amount");
        databaseTransactions = FirebaseDatabase.getInstance().getReference("Transactions");
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void amount(int creditAmount)
    {
        amount.setAmount(creditAmount);
    }


    //This function will be used to add credit Transaction.
    public void creditTransaction(int previousBalance,int credit)
    {
        int finalAmount = previousBalance + credit;

        //Get current Date for entry in transactions.
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

        transactions = new Transactions(formattedDate,credit,0,finalAmount);
        databaseTransactions.child(userID).push().setValue(transactions);

        amount(finalAmount);
        databaseAmount.child(userID).setValue(amount);
    }

    public void debitTransaction(int previousBalance,int debit)
    {
        int finalAmount = previousBalance - debit;

        //Get current Date for entry in transactions.
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

        transactions = new Transactions(formattedDate,0,debit,finalAmount);
        databaseTransactions.child(userID).push().setValue(transactions);

        amount(finalAmount);
        databaseAmount.child(userID).setValue(amount);
    }

}
