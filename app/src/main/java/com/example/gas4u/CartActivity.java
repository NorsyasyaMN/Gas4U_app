package com.example.gas4u;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gas4u.databinding.ActivityCartBinding;

public class CartActivity extends DrawerBaseActivity {

    ActivityCartBinding activityCartBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCartBinding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(activityCartBinding.getRoot());
        allocateActivityTitle("Carts");
    }
}