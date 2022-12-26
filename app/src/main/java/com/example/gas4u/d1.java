package com.example.gas4u;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gas4u.databinding.ActivityD1Binding;


public class d1 extends DrawerAdminActivity{

    ActivityD1Binding activityAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddProduct = ActivityD1Binding.inflate(getLayoutInflater());
        setContentView(activityAddProduct.getRoot());
        allocateActivityTitle("Add Product");
    }
}