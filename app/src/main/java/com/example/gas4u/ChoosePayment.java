package com.example.gas4u;

import static com.example.gas4u.R.id.button3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.wallet.WalletConstants;


public class ChoosePayment extends AppCompatActivity {

    private Button googlePayButton, Paycash;
    private TextView paymentMethod;


    @Override
    protected void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_checkout);
        googlePayButton = findViewById(R.id.googlePayButton);
        Paycash = findViewById(R.id.Paycash);
        paymentMethod = findViewById(R.id.titleMethod);
    }

    public void onClick(View v){
        Toast.makeText(ChoosePayment.this, "Payment", Toast.LENGTH_SHORT).show();

    }



}

