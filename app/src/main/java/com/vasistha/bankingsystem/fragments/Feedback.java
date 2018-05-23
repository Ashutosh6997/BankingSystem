package com.vasistha.bankingsystem.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vasistha.bankingsystem.MainActivity;
import com.vasistha.bankingsystem.R;
import com.vasistha.bankingsystem.ModelClasses.UserFeedback;

/**
 * A simple {@link Fragment} subclass.
 */
public class Feedback extends Fragment
{
    String feedBack;
    String rating;

    EditText editTextFeedback;
    RatingBar userRating;
    Button submitFeedback;

    DatabaseReference databaseFeedbacks;

    UserFeedback userFeedback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        getActivity().setTitle("Feedback");

        databaseFeedbacks = FirebaseDatabase.getInstance().getReference("Feedbacks");

        editTextFeedback = view.findViewById(R.id.editTextFeedback);
        userRating = view.findViewById(R.id.userRating);
        submitFeedback = view.findViewById(R.id.submitFeedbackButton);

        submitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                feedBack = editTextFeedback.getText().toString();
                rating = String.valueOf(userRating.getRating());
                if(feedBack.matches(""))
                {
                    editTextFeedback.setError("Please give some userFeedback!");
                    editTextFeedback.requestFocus();
                    return;
                }
                if(rating.matches(""))
                {
                    Toast.makeText(getContext(),"Please provide Rating",Toast.LENGTH_SHORT).show();
                    return;
                }
                submitFeedback.setClickable(false);
                userFeedback = new UserFeedback(feedBack,rating);
                databaseFeedbacks.child(((MainActivity)getActivity()).UserID).push().setValue(userFeedback);
                Toast.makeText(getContext(),"Thanks for you Feedback",Toast.LENGTH_SHORT).show();

                Fragment fragment = null;
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                fragment = new Feedback();
                fragmentManager.beginTransaction().remove(fragment).commit();
                fragmentManager.popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                fragment = new Home();
                fragmentManager.beginTransaction().replace(R.id.mainContent,fragment).commit();
            }
        });
        return view;
    }

}
