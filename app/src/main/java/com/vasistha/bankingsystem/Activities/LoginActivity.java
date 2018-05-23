package com.vasistha.bankingsystem.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.vasistha.bankingsystem.MainActivity;
import com.vasistha.bankingsystem.R;
import com.vasistha.bankingsystem.fragments.ForgotPassword;

public class LoginActivity extends AppCompatActivity
{
    //UI Elements instantiation.
    String EmailID;
    String Password;

    EditText loginEmail;
    EditText loginPassword;

    Button loginButton;

    ProgressBar progressBar;

    TextView textView;
    TextView forgotPassword;

    Intent intent;

    //Firebase.
    FirebaseAuth firebaseAuth;

    //Fragment instantiation.
    android.support.v4.app.Fragment fragment = null;
    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.loggingIn);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);

        //Login process, take input from user and process it.
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EmailID = loginEmail.getText().toString();
                Password = loginPassword.getText().toString();

                if(EmailID.isEmpty())
                {
                    loginEmail.setError("Email is required!");
                    loginEmail.requestFocus();
                    return;
                }

                if(Password.isEmpty())
                {
                    loginButton.setError("Password is required!");
                    loginButton.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(EmailID).matches()) {
                    loginEmail.setError("Please enter a valid email");
                    loginEmail.requestFocus();
                    return;
                }

                if (Password.length() < 6) {
                    loginPassword.setError("Minimum length of password should be 6");
                    loginPassword.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(EmailID,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        textView = findViewById(R.id.New_User);
        intent = new Intent(this,SignUpActivity.class);
        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });

        forgotPassword = findViewById(R.id.Forgot_Password);
        forgotPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fragment = new ForgotPassword();
                fragmentManager.beginTransaction().addToBackStack(fragment.getClass().getName()).replace(R.id.loginLayout,fragment).commit();
            }
        });
    }

    //Check if already logged in or not!
    @Override
    protected void onStart()
    {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}
