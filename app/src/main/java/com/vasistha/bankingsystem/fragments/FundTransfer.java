package com.vasistha.bankingsystem.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vasistha.bankingsystem.ModelClasses.Amount;
import com.vasistha.bankingsystem.BankingTransactions;
import com.vasistha.bankingsystem.MainActivity;
import com.vasistha.bankingsystem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FundTransfer extends Fragment
{
    int balance = 0;

    String beneficiaryID;

    String email;
    String amount;

    EditText beneficiaryEmail;
    EditText transferAmount;

    Button transferAmountButton;

    DatabaseReference databaseUsers;
    DatabaseReference databaseTransactions;
    DatabaseReference databaseAmount;

    BankingTransactions bankingTransactions;
    Amount amountClass;

    public FundTransfer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fund_transfer, container, false);

        getActivity().setTitle("Fund Transfer");

        bankingTransactions = new BankingTransactions();
        amountClass = new Amount();

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        databaseTransactions = FirebaseDatabase.getInstance().getReference("Transactions");
        databaseAmount = FirebaseDatabase.getInstance().getReference("Amount");

        beneficiaryEmail = view.findViewById(R.id.beneficiaryEmail);
        transferAmount = view.findViewById(R.id.transferAmount);

        transferAmountButton = view.findViewById(R.id.transferAmountButton);

        transferAmountButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                email = beneficiaryEmail.getText().toString();
                amount = transferAmount.getText().toString();

                if (email.isEmpty()) {
                    beneficiaryEmail.setError("Email is required");
                    beneficiaryEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    beneficiaryEmail.setError("Please enter a valid email");
                    beneficiaryEmail.requestFocus();
                    return;
                }

                if(email.equals(((MainActivity)getActivity()).UserEmailID))
                {
                    beneficiaryEmail.setError("You can't transfer to your own account!");
                    beneficiaryEmail.requestFocus();
                    return;
                }

                if(amount.isEmpty())
                {
                    transferAmount.setError("Amount is required!");
                    transferAmount.requestFocus();
                    return;
                }

                databaseAmount.child(((MainActivity)getActivity()).UserID).child("amount").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists())
                        {
                            balance = (int) (long) dataSnapshot.getValue();
                            if(Integer.valueOf(amount) > balance || balance == 0)
                            {
                                transferAmountButton.setClickable(true);
                                transferAmount.setError("You don't have sufficient balance!");
                                transferAmount.requestFocus();
                                return;
                            }
                            transferAmountButton.setClickable(false);
                            TransferAmount();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        return;
                    }
                });

            }
        });

        return view;
    }

    public void TransferAmount()
    {
        databaseUsers.orderByChild("userEmail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                int i = (int) (long) dataSnapshot.getChildrenCount();

                if(i != 1)
                {
                    transferAmountButton.setClickable(true);
                    beneficiaryEmail.setError("User with this Email doesn't exists!");
                    beneficiaryEmail.requestFocus();
                    return;
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    beneficiaryID = snapshot.getKey();
                }


                bankingTransactions.setUserID(beneficiaryID);

                databaseAmount.child(beneficiaryID).child("amount").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists())
                        {
                            int i = (int) (long) dataSnapshot.getValue();
                            bankingTransactions.creditTransaction(i,Integer.valueOf(amount));

                            bankingTransactions.setUserID(((MainActivity)getActivity()).UserID);

                            databaseAmount.child(((MainActivity)getActivity()).UserID).child("amount").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot)
                                {
                                    if(dataSnapshot.exists())
                                    {
                                        int j = (int) (long) dataSnapshot.getValue();
                                        bankingTransactions.debitTransaction(j,Integer.valueOf(amount));

                                        Toast.makeText(getContext(),"Fund Transferred Successfully!",Toast.LENGTH_SHORT).show();

                                        Fragment fragment = null;
                                        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                                        fragment = new FundTransfer();
                                        fragmentManager.beginTransaction().remove(fragment).commit();
                                        fragmentManager.popBackStack();

                                        fragment = new Home();
                                        fragmentManager.beginTransaction().replace(R.id.mainContent,fragment).commit();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError)
                                {
                                    return;
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        return;
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                return;
            }
        });
    }

}
