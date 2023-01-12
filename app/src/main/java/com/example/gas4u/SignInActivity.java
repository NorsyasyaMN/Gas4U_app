package com.example.gas4u;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity {

    EditText editTextUserName, editTextPassword;
    TextView textViewForgotPassword, textViewRegister;
    ProgressBar progressBar;
    Button btnSignIn;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextUserName = (EditText) findViewById(R.id.editTextSignInUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextSignInPassword);
        textViewForgotPassword = (TextView) findViewById(R.id.txtSignInForgotPassword);
        textViewRegister = (TextView) findViewById(R.id.txtSignInRegister);
        progressBar = (ProgressBar) findViewById(R.id.progressBarSignIn);
        btnSignIn = (Button) findViewById(R.id.signInBtn);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = editTextUserName.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
                    editTextUserName.setError("Please Enter a Valid Email");
                    editTextUserName.requestFocus();
                }

                if (editTextPassword.length() < 6) {
                    editTextPassword.setError("Please Enter Password containing at least 6 characters");
                    editTextPassword.requestFocus();
                }

                progressBar.setVisibility(View.GONE);

                mAuth.signInWithEmailAndPassword(userName,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignInActivity.this, "User is Successfully Signed In", Toast.LENGTH_LONG).show();
                        checkUserAccessLevel(authResult.getUser().getUid());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignInActivity.this, "User is Failed to Signed In", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);
        //extract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG","onSuccess: " + documentSnapshot.getData());
                //identify the user access level

                if(documentSnapshot.getString("isAdmin") != null) {
                  //user is admin
                  startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                  finish();
                }
                if(documentSnapshot.get("isUser") != null){
                    startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                    finish();
                }
                if(documentSnapshot.get("isRider") != null){
                    startActivity(new Intent(getApplicationContext(), RiderActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                finish();
            }
        });
    }

    public void textSignInForgotPasswordClicked(View v){

        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
    public void textSignInRegisterClicked(View v){

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}



