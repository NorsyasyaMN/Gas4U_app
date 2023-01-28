package com.example.gas4u;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class payCash extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        button = (Button) findViewById(R.id.Paycash);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openConfirmPayment();
            }
        });

    }
    public void openConfirmPayment(){
        Intent intent = new Intent(this,ConfirmPayment.class);
        startActivity(intent);
    }
}