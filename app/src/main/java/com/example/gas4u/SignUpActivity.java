package com.example.gas4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity{

    EditText editTextUsername;
    EditText editTextPassword;
    EditText editTextMobileNo;
    EditText editTextEmail;
    ProgressBar progressBar;
    Button btnSignUp;
    CheckBox isCustomerBox, isRiderBox, isAdminBox;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextMobileNo = findViewById(R.id.editTextMobileNo);
        editTextEmail = findViewById(R.id.editTextEmail);
        btnSignUp = findViewById(R.id.signUpBtn);
        progressBar = findViewById(R.id.progressBar);
        isCustomerBox = findViewById(R.id.checkCustomer);
        isRiderBox = findViewById(R.id.checkRider);
        isAdminBox = findViewById(R.id.checkAdmin);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        isCustomerBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isAdminBox.setChecked(false);
                    isRiderBox.setChecked(false);
                }
            }
        });

        isAdminBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isCustomerBox.setChecked(false);
                    isRiderBox.setChecked(false);
                }
            }
        });

        isRiderBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isCustomerBox.setChecked(false);
                    isAdminBox.setChecked(false);
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtUserName = editTextUsername.getText().toString();
                String txtPassword = editTextPassword.getText().toString();
                String txtPhoneNo = editTextMobileNo.getText().toString();
                String txtEmail = editTextEmail.getText().toString();

                if (txtUserName.isEmpty()){
                    editTextUsername.setError("Please Enter Username");
                    editTextUsername.requestFocus();
                }
                if (txtPassword.isEmpty() || txtPassword.length() < 6){
                    editTextPassword.setError("Please Enter Password Containing at least 6 characters");
                    editTextPassword.requestFocus();
                }
                if (txtPhoneNo.isEmpty()){
                    editTextMobileNo.setError("Please Enter Phone No");
                    editTextMobileNo.requestFocus();
                }
                if (txtEmail.isEmpty()){
                    editTextEmail.setError("Please Enter Email");
                    editTextEmail.requestFocus();
                }

                // checkbox validation
                if(!(isCustomerBox.isChecked() || isAdminBox.isChecked() || isRiderBox.isChecked())){
                    Toast.makeText(SignUpActivity.this, "Select The Account Type",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString(),editTextPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(SignUpActivity.this, "User Registered Successfully", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        DocumentReference df = fStore.collection("Users").document(user.getUid());
                        Map<String,Object> userInfo = new HashMap<>();
                        userInfo.put("Username",editTextUsername.getText().toString());
                        userInfo.put("Password",editTextPassword.getText().toString());
                        userInfo.put("Email", editTextEmail.getText().toString());
                        userInfo.put("PhoneNumber", editTextMobileNo.getText().toString());
                        // Specify if the user is admin
                        if(isCustomerBox.isChecked()){
                            userInfo.put("isUser","1");
                        }
                        if(isAdminBox.isChecked()){
                            userInfo.put("isAdmin","1");
                        }
                        df.set(userInfo);
                        startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "User Failed to Register", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}