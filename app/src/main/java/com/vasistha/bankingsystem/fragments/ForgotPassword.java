package com.vasistha.bankingsystem.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.vasistha.bankingsystem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPassword extends Fragment
{
    FirebaseAuth firebaseAuth;
    EditText emailID;
    Button submitEmail;
    public ForgotPassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        getActivity().setTitle("Forgot Password");

        firebaseAuth = FirebaseAuth.getInstance();

        emailID = view.findViewById(R.id.sendEmailID);
        submitEmail = view.findViewById(R.id.sendLink);

        submitEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(emailID.getText().toString().isEmpty())
                {
                    emailID.setError("Email is required!");
                    emailID.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(emailID.getText().toString()).matches()) {
                    emailID.setError("Please enter a valid email");
                    emailID.requestFocus();
                    return;
                }
                submitEmail.setClickable(false);
                firebaseAuth.sendPasswordResetEmail(emailID.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getContext(),"Password reset link has been sent to your Email-ID",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            submitEmail.setClickable(true);
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return view;
    }

}
