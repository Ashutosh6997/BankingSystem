package com.vasistha.bankingsystem.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vasistha.bankingsystem.ModelClasses.CustomersData;
import com.vasistha.bankingsystem.MainActivity;
import com.vasistha.bankingsystem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfile extends Fragment
{
    DatabaseReference databaseUsers;
    CustomersData customersData;

    TextView customerEmailID,customerName,customerGender,customerDateofBirth,customerMobileNumber,customerAddress;

    public MyProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        getActivity().setTitle("My Profile");

        customerEmailID = view.findViewById(R.id.customerEmailID);
        customerName = view.findViewById(R.id.customerName);
        customerGender = view.findViewById(R.id.customerDisplayGender);
        customerDateofBirth = view.findViewById(R.id.customerDateofBirth);
        customerMobileNumber = view.findViewById(R.id.customerMobileNumber);
        customerAddress = view.findViewById(R.id.customerAddress);

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        databaseUsers.child(((MainActivity)getActivity()).UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.getValue() != null)
                {
                    customersData = dataSnapshot.getValue(CustomersData.class);
                    customerEmailID.setText(customersData.getUserEmail());
                    customerName.setText(customersData.getCustomerName());
                    customerGender.setText(customersData.getGender());
                    customerDateofBirth.setText(customersData.getDateOfBirth());
                    customerMobileNumber.setText(customersData.getMobileNumber());
                    customerAddress.setText(customersData.getAddress());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
