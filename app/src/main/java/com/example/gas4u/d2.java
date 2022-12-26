package com.example.gas4u;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gas4u.databinding.ActivityD2Binding;

public class d2 extends DrawerAdminActivity{

    ActivityD2Binding activityD2Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityD2Binding = ActivityD2Binding.inflate(getLayoutInflater());
        setContentView(activityD2Binding.getRoot());
        allocateActivityTitle("Orders");
    }
}