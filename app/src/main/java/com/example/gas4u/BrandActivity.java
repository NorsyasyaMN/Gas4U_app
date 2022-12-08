package com.example.gas4u;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gas4u.databinding.ActivityBrandBinding;

public class BrandActivity extends DrawerBaseActivity {

    ActivityBrandBinding activityBrandBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBrandBinding = ActivityBrandBinding.inflate(getLayoutInflater());
        setContentView(activityBrandBinding.getRoot());
        allocateActivityTitle("Products");
    }
}