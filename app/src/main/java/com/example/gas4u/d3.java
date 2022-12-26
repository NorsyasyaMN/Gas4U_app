package com.example.gas4u;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gas4u.databinding.ActivityD2Binding;
import com.example.gas4u.databinding.ActivityD3Binding;

public class d3 extends DrawerAdminActivity {

    ActivityD3Binding activityD3Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d3);
        activityD3Binding = ActivityD3Binding.inflate(getLayoutInflater());
        setContentView(activityD3Binding.getRoot());
        allocateActivityTitle("Customer");
    }
}