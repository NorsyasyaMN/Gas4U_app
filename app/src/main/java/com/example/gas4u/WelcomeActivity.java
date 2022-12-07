package com.example.gas4u;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Coding Pursuits 1");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("GAS_AND_FUEL_DELIVERY_APP", "Value is: " + value);
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("GAS_AND_FUEL_DELIVERY_APP", "Failed to read value.", error.toException());
            }
        });

    }

    public void OnButtonSignUpClicked(View view){

        Intent intent = new Intent(WelcomeActivity.this,SignUpActivity.class);
        startActivity(intent);

    }

    public void OnButtonSignInClicked(View view){

    }
}