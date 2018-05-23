package com.vasistha.bankingsystem.Activities;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vasistha.bankingsystem.BankingTransactions;
import com.vasistha.bankingsystem.MainActivity;
import com.vasistha.bankingsystem.ModelClasses.Amount;
import com.vasistha.bankingsystem.ModelClasses.CustomersData;
import com.vasistha.bankingsystem.R;



public class SignUpActivity extends AppCompatActivity
{
    //Get the uniquely generated UID by the firebase database.
    String userID;
    String userEmailID;

    //Used to record the balance in the customer's account.
    int previousBalance;

    BankingTransactions bankingTransactions;

    //Three Class to hold Customer's Data, Transaction Details and Available Balance
    CustomersData customersData;
    Amount amount;

    //Strings for Spinner Values
    String Name_Title;
    String Gender;
    String Day;
    String Month;
    String Year;

    //Firebase authorization object.
    private FirebaseAuth firebaseAuth;

    //Database reference for all tables.
    DatabaseReference databaseUsers;
    DatabaseReference databaseTransactions;
    DatabaseReference databaseAmount;

    //Stores reference to username,password edittexts and signUp button.
    EditText userEmail, passWord;
    Button signUp;

    //Progress Bar.
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        bankingTransactions = new BankingTransactions();
        amount = new Amount();

        //Spinners in Sign Up Form Activity
        Spinner spinner = findViewById(R.id.NameSignature);
        Spinner spinnerGender = findViewById(R.id.CustomerGender);

        Spinner spinnerDay = findViewById(R.id.birthDay);
        Spinner spinnerMonth = findViewById(R.id.birthMonth);
        Spinner spinnerYear = findViewById(R.id.birthYear);

        //Progress Bar
        progressBar = findViewById(R.id.signingUp);


        //Title spinners item selection listener.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Name_Title = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Gender spinners item selection listeners.
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Get the Date of Birth of the Customer
        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Day = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Month = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Year = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //EditTexts instantiation
        userEmail = findViewById(R.id.userEmail);
        passWord = findViewById(R.id.Password);

        //signUp button instantiation
        signUp = findViewById(R.id.SignUp);

        //firebaseAuth object Instantiation.
        firebaseAuth = FirebaseAuth.getInstance();



        //Instantiation of Database References.
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        databaseTransactions = FirebaseDatabase.getInstance().getReference("Transactions");
        databaseAmount = FirebaseDatabase.getInstance().getReference("Amount");

        //Real Work,SignUp button OnClickListener.
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call Register User Method
                registerUser();
            }
        });
    }

    private void registerUser() {
        //Normal conditions being checked, Such as Email or Password are correct and not Empty

        String email = userEmail.getText().toString().trim();
        String password = passWord.getText().toString().trim();

        if (email.isEmpty()) {
            userEmail.setError("Email is required");
            userEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("Please enter a valid email");
            userEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passWord.setError("Password is required");
            passWord.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passWord.setError("Minimum lenght of password should be 6");
            passWord.requestFocus();
            return;
        }

        boolean anyBad = customerInfo();
        if(anyBad == true)
        {
            return;
        }

        signUp.setEnabled(false);

        progressBar.setVisibility(View.VISIBLE);

        //Creates User with Email Id and Password.
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Sign Up Successful",Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = task.getResult().getUser();

                    //Get the registered users ID.
                    userID = firebaseUser.getUid();
                    userEmailID = firebaseUser.getEmail();

                    bankingTransactions.userID = firebaseUser.getUid();

                    //Set Customers Data(Name,Mobile No, etc) with the unique UserID.
                    databaseUsers.child(userID).setValue(customersData);

                    //Initial amount i.e., 0 in the Amount Table.
                    amount.setAmount(0);
                    databaseAmount.child(userID).setValue(amount);

                    databaseAmount.child(userID).child("amount").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            if(dataSnapshot.exists())
                            {
                                previousBalance = (int) (long) dataSnapshot.getValue();
                                bankingTransactions.creditTransaction(previousBalance,1000);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });

                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    //You know if things go wrong, then these thing happen, Nothing Magical
                    signUp.setEnabled(true);
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }



    //After successfully registering user, there other common data is loaded in the firebase database.
    private boolean customerInfo()
    {
        boolean problem = false;

        // Get User Name
        EditText customeruserEmail = findViewById(R.id.userEmail);
        String userEmail = customeruserEmail.getText().toString();

        //Get Password
        EditText customerPassword = findViewById(R.id.Password);
        String PassWord = customerPassword.getText().toString();

        //Get Title
        String customerNameTitle = Name_Title;

        //Get First Name of the Customer
        EditText customerFirstName = findViewById(R.id.FirstName);
        String FirstName = customerFirstName.getText().toString();

        //Get Middle Name of the Customer
        EditText customerMiddleName = findViewById(R.id.MiddleName);
        String MiddleName = customerMiddleName.getText().toString();

        //Get Last Name of the Customer
        EditText customerLastName = findViewById(R.id.LastName);
        String LastName = customerLastName.getText().toString();

        //Get Gender of the Customer
        String gender = Gender;

        //Get Mobile Number of the Customer
        EditText customerMobileNo = findViewById(R.id.Mobile_No);
        String Mobile_No = customerMobileNo.getText().toString();

        //Get Date of Birth of the Customer;Date,Month,Year
        String date = Day;
        String month = Month;
        String year = Year;

        //Get Address of the Customer
        EditText customerAddress = findViewById(R.id.Address);
        String Address = customerAddress.getText().toString();

        //Check if any of the fields are left empty
        boolean checkuserEmail = check_userEmail(userEmail);
        boolean checkPassWord = check_Password(PassWord);
        boolean checkTitle = check_Title(customerNameTitle);
        boolean checkFirstName = check_Firstname(FirstName);
        boolean checkMiddleName = check_Middlename(MiddleName);
        boolean checkLastName = check_Lastname(LastName);
        boolean checkGender = check_Gender(gender);
        boolean checkMobileNo = check_MobileNo(Mobile_No);
        boolean checkAddress = check_Address(Address);


        if (checkuserEmail == false || checkPassWord == false || checkTitle == false || checkFirstName == false || checkLastName == false || checkGender == false || checkAddress == false) {
            Toast.makeText(getApplicationContext(), "Please fill all the mandatory details!", Toast.LENGTH_LONG).show();
            problem = true;
        }

        if (checkMobileNo == false) {
            Toast.makeText(SignUpActivity.this, "Make sure Mobile No. is correct!", Toast.LENGTH_SHORT).show();
            problem = true;
        }

        if(checkMiddleName == false)
        {
            customersData = new CustomersData(userEmail, customerNameTitle, FirstName, LastName, customerNameTitle + " " + FirstName + " " + LastName, gender, Mobile_No, date + "-" + month + "-" + year, Address);
        }
        else
        {
            customersData = new CustomersData(userEmail, customerNameTitle, FirstName, MiddleName, LastName, customerNameTitle + " " + FirstName + " " + MiddleName + " " + LastName, gender, Mobile_No, date + "-" + month + "-" + year, Address);
        }
        return problem;
    }


    //Supportive Methods for customerInfo().
    public boolean check_userEmail(String userEmail) {
        if (userEmail.matches("")) return false;
        else return true;
    }

    public boolean check_Password(String password) {
        if (password.matches("")) return false;
        else return true;
    }

    public boolean check_Title(String title) {
        if (title.matches("")) return false;
        else return true;
    }

    public boolean check_Firstname(String firstName) {
        if (firstName.matches("")) return false;
        else return true;
    }

    public boolean check_Middlename(String middleName) {
        if (middleName.matches("")) return false;
        else return true;
    }

    public boolean check_Lastname(String lastName) {
        if (lastName.matches("")) return false;
        else return true;
    }

    public boolean check_Gender(String cust_Gender) {
        if (cust_Gender.matches("")) return false;
        else return true;
    }

    public boolean check_MobileNo(String mobileNo) {
        if (mobileNo.matches("") || (mobileNo.length() != 10)) return false;
        else return true;
    }

    public boolean check_Address(String address) {
        if (address.matches("")) return false;
        else return true;
    }
}
