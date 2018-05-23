package com.vasistha.bankingsystem.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vasistha.bankingsystem.BankingTransactions;
import com.vasistha.bankingsystem.MainActivity;
import com.vasistha.bankingsystem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillPayment extends Fragment
{
    int balance;
    String amount;

    String selectedBillerName;
    Spinner billerName;

    EditText customerBillID;
    EditText billAmount;

    Button payBillButton;

    DatabaseReference databaseUsers;
    DatabaseReference databaseTransactions;
    DatabaseReference databaseAmount;

    BankingTransactions bankingTransactions;

    public BillPayment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill_payment, container, false);
        getActivity().setTitle("Bill Payment");

        bankingTransactions = new BankingTransactions();

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        databaseTransactions = FirebaseDatabase.getInstance().getReference("Transactions");
        databaseAmount = FirebaseDatabase.getInstance().getReference("Amount");

        billerName = view.findViewById(R.id.billerName);

        customerBillID = view.findViewById(R.id.customerBillID);
        billAmount = view.findViewById(R.id.billAmount);

        payBillButton = view.findViewById(R.id.billPayButton);

        billerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedBillerName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        payBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(selectedBillerName.matches("--Select Biller Name--"))
                {
                    Toast.makeText(getActivity(),"Please select Biller Name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(customerBillID.getText().toString().matches(""))
                {
                    customerBillID.setError("Please enter Customer ID");
                    customerBillID.requestFocus();
                    return;
                }
                if(billAmount.getText().toString().matches(""))
                {
                    billAmount.setError("Please Enter Amount!");
                    billAmount.requestFocus();
                    return;
                }

                payBillButton.setClickable(false);
                payBill();
            }
        });

        return view;
    }

    public void payBill()
    {
        amount = billAmount.getText().toString();

        databaseAmount.child(((MainActivity)getActivity()).UserID).child("amount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    balance = (int) (long) dataSnapshot.getValue();

                    if(Integer.valueOf(amount) > balance || balance == 0)
                    {
                        payBillButton.setClickable(true);
                        billAmount.setError("You don't have sufficient balance!");
                        billAmount.requestFocus();
                        return;
                    }

                    bankingTransactions.setUserID((((MainActivity) getActivity()).UserID));
                    bankingTransactions.debitTransaction(balance,Integer.valueOf(amount));

                    Toast.makeText(getContext(),"Bill Paid Successfully!",Toast.LENGTH_SHORT).show();

                    Fragment fragment = null;
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                    fragment = new BillPayment();
                    fragmentManager.beginTransaction().remove(fragment).commit();
                    fragmentManager.popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    fragment = new Home();
                    fragmentManager.beginTransaction().replace(R.id.mainContent,fragment).commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                return;
            }
        });


    }

}
