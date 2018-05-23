package com.vasistha.bankingsystem.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vasistha.bankingsystem.Adapters.FDAdapter;
import com.vasistha.bankingsystem.BankingTransactions;
import com.vasistha.bankingsystem.ModelClasses.FixedDepositData;
import com.vasistha.bankingsystem.MainActivity;
import com.vasistha.bankingsystem.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FixedDeposit extends Fragment
{
    int balance;

    int fd_Amount;
    double maturityAmount;
    int term;
    String fdTerm;

    String startDate;
    String endDate;

    EditText fdAmount;
    Spinner fdYears;
    Button addFD;

    ListView fdList;
    List<FixedDepositData> fixedDepositList;

    DatabaseReference databaseFixedAmounts;
    DatabaseReference databaseAmount;

    BankingTransactions bankingTransactions;
    FixedDepositData fixedDepositData;

    public FixedDeposit()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fixed_deposit, container, false);

        getActivity().setTitle("Fixed Deposit");

        fdList = view.findViewById(R.id.displayFD);
        fixedDepositList = new ArrayList<>();

        databaseFixedAmounts = FirebaseDatabase.getInstance().getReference("Fixed Amount");
        databaseAmount = FirebaseDatabase.getInstance().getReference("Amount");

        bankingTransactions = new BankingTransactions();

        fdAmount = view.findViewById(R.id.fdAmount);
        fdYears = view.findViewById(R.id.fdYears);

        databaseFixedAmounts.child(((MainActivity)getActivity()).UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                fixedDepositList.clear();
                for(DataSnapshot fdSnapshot : dataSnapshot.getChildren())
                {
                    FixedDepositData fixedDepositData = fdSnapshot.getValue(FixedDepositData.class);
                    fixedDepositList.add(fixedDepositData);
                }
                if(getContext() != null)
                {
                    FDAdapter fdAdapter = new FDAdapter(getContext(),fixedDepositList);
                    fdList.setAdapter(fdAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fdYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                fdTerm = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        addFD = view.findViewById(R.id.addFD);

        addFD.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(fdAmount.getText().toString().matches(""))
                {
                    fdAmount.setError("Please Enter FD Amount!");
                    fdAmount.requestFocus();
                    return;
                }
                if(fdTerm.matches("--Select Years--"))
                {
                    Toast.makeText(getContext(),"Please select years!",Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (fdTerm)
                {
                    case "One Year":
                        term = 1;
                        maturityAmount = fd_Amount * java.lang.Math.pow ((1 + (0.06/1)),1);
                        break;
                    case "Two Years":
                        term = 2;
                        maturityAmount = fd_Amount * java.lang.Math.pow ((1 + (0.06/1)),2);
                        break;
                    case "Three Years":
                        term = 3;
                        maturityAmount = fd_Amount * java.lang.Math.pow ((1 + (0.06/1)),3);
                        break;
                    case "Five Years":
                        term = 5;
                        maturityAmount = fd_Amount * java.lang.Math.pow ((1 + (0.065/1)),5);
                        break;
                    case "Seven Years":
                        term = 7;
                        maturityAmount = fd_Amount * java.lang.Math.pow ((1 + (0.065/1)),7);
                        break;
                    case "Ten Years":
                        term = 10;
                        maturityAmount = fd_Amount * java.lang.Math.pow ((1 + (0.07/1)),10);
                        break;
                }
                fd_Amount = Integer.parseInt(fdAmount.getText().toString());
                addFD.setClickable(false);
                addNewFD();
            }
        });

        return view;
    }
    public void addNewFD()
    {
        databaseAmount.child(((MainActivity)getActivity()).UserID).child("amount").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    balance = (int) (long) dataSnapshot.getValue();
                    if(fd_Amount > balance || balance == 0)
                    {
                        addFD.setClickable(true);
                        fdAmount.setError("You don't have sufficient balance!");
                        fdAmount.requestFocus();
                        return;
                    }

                    //Get Dates for entry in Fixed Amount Details.
                    Calendar cal = Calendar.getInstance();
                    Date today = cal.getTime();
                    cal.add(Calendar.YEAR, term);
                    Date nextYear = cal.getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    startDate = dateFormat.format(today);
                    endDate = dateFormat.format(nextYear);

                    fixedDepositData = new FixedDepositData(startDate,fd_Amount,endDate,(int)maturityAmount);
                    databaseFixedAmounts.child(((MainActivity)getActivity()).UserID).push().setValue(fixedDepositData);

                    bankingTransactions.setUserID((((MainActivity) getActivity()).UserID));
                    bankingTransactions.debitTransaction(balance,fd_Amount);

                    Toast.makeText(getContext(),"Fixed Deposit Successful!",Toast.LENGTH_SHORT).show();

                    Fragment fragment = null;
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                    fragment = new FixedDeposit();
                    fragmentManager.beginTransaction().remove(fragment).commit();
                    fragmentManager.popBackStack();


                    fragment = new Home();
                    fragmentManager.beginTransaction().replace(R.id.mainContent,fragment).commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
