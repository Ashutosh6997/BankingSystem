package com.vasistha.bankingsystem.Adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vasistha.bankingsystem.ModelClasses.Transactions;
import com.vasistha.bankingsystem.R;

import java.util.List;

/**
 * Created by Ashutosh Singh on 19-Feb-18.
 */

public class TransactionAdapter extends ArrayAdapter<Transactions>
{
    private Context context;
    private List<Transactions> transactionsList;

    public TransactionAdapter(Context context, List<Transactions> transactionsList)
    {
        super(context, R.layout.transaction_list,transactionsList);
        this.context = context;
        this.transactionsList = transactionsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View transactionView = convertView;

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        transactionView = layoutInflater.inflate(R.layout.transaction_list,null,true);

        TextView transactionDate = transactionView.findViewById(R.id.transactionDate);
        TextView transactionCredit = transactionView.findViewById(R.id.transactionCredit);
        TextView transactionDebit = transactionView.findViewById(R.id.transactionDebit);
        TextView transactionBalance = transactionView.findViewById(R.id.finalBalance);

        Transactions transactions = transactionsList.get(position);

        transactionDate.setText(transactions.getDate());
        transactionCredit.setText(Integer.toString(transactions.getCredit()));
        transactionDebit.setText(Integer.toString(transactions.getDebit()));
        transactionBalance.setText(Integer.toString(transactions.getFinalAmount()));

        return transactionView;
    }
}
