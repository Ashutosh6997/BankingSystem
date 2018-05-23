package com.vasistha.bankingsystem.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vasistha.bankingsystem.MainActivity;
import com.vasistha.bankingsystem.R;
import com.vasistha.bankingsystem.Adapters.TransactionAdapter;
import com.vasistha.bankingsystem.ModelClasses.Transactions;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionHistory extends Fragment
{
    ListView transactionList;
    List<Transactions> transactionsList;

    DatabaseReference databaseTransactions;

    public TransactionHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);

        getActivity().setTitle("Transaction History");

        transactionList = view.findViewById(R.id.transactionDetails);
        transactionsList = new ArrayList<>();

        databaseTransactions = FirebaseDatabase.getInstance().getReference("Transactions");

        databaseTransactions.child(((MainActivity)getActivity()).UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                transactionsList.clear();
                for(DataSnapshot transactionSnapshot : dataSnapshot.getChildren())
                {
                    Transactions transactions = transactionSnapshot.getValue(Transactions.class);
                    transactionsList.add(transactions);
                }
                if(getContext()!= null)
                {
                    TransactionAdapter transactionAdapter = new TransactionAdapter(getContext(),transactionsList);
                    transactionList.setAdapter(transactionAdapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
