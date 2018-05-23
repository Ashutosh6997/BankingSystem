package com.vasistha.bankingsystem.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class Recharge extends Fragment
{
    String amount;
    String operatorName;
    int balance;

    Spinner spinnerOperators;
    EditText rechargeMobileNo;
    EditText rechargeAmount;
    Button rechargeButton;

    DatabaseReference databaseUsers;
    DatabaseReference databaseTransactions;
    DatabaseReference databaseAmount;

    BankingTransactions bankingTransactions;

    public Recharge() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recharge, container, false);

        getActivity().setTitle("Recharge");

        bankingTransactions = new BankingTransactions();

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        databaseTransactions = FirebaseDatabase.getInstance().getReference("Transactions");
        databaseAmount = FirebaseDatabase.getInstance().getReference("Amount");

        spinnerOperators = view.findViewById(R.id.operatorsSpinner);
        rechargeMobileNo = view.findViewById(R.id.rechargeMobileNo);
        rechargeAmount = view.findViewById(R.id.rechargeAmount);
        rechargeButton = view.findViewById(R.id.rechargeButton);

        spinnerOperators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                operatorName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rechargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(operatorName.matches("--Select Operator--"))
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Please select operator!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(rechargeMobileNo.getText().toString().matches(""))
                {
                    rechargeMobileNo.setError("Enter Mobile No.");
                    rechargeMobileNo.requestFocus();
                    return;
                }
                if(rechargeMobileNo.getText().toString().length() != 10)
                {
                    rechargeMobileNo.setError("Please enter correct Mobile No.");
                    rechargeMobileNo.requestFocus();
                    return;
                }
                if(rechargeAmount.getText().toString().matches(""))
                {
                    rechargeAmount.setError("Please enter amount!");
                    rechargeAmount.requestFocus();
                    return;
                }
                rechargeButton.setClickable(false);
                recharge();
            }
        });

        return view;
    }

    public void recharge()
    {
        amount = rechargeAmount.getText().toString();

        databaseAmount.child(((MainActivity)getActivity()).UserID).child("amount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    balance = (int) (long) dataSnapshot.getValue();

                    if(Integer.valueOf(amount) > balance || balance == 0)
                    {
                        rechargeButton.setClickable(true);
                        rechargeAmount.setError("You don't have sufficient balance!");
                        rechargeAmount.requestFocus();
                        return;
                    }

                    bankingTransactions.setUserID((((MainActivity) getActivity()).UserID));
                    bankingTransactions.debitTransaction(balance,Integer.valueOf(amount));

                    Toast.makeText(getContext(),"Recharged Successfully!",Toast.LENGTH_SHORT).show();

                    Fragment fragment = null;
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                    fragment = new Recharge();
                    fragmentManager.beginTransaction().remove(fragment).commit();
                    fragmentManager.popBackStack();


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
