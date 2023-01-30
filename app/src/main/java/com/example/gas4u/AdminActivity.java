package com.example.gas4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.gas4u.databinding.ActivityAdminBinding;
import com.example.gas4u.databinding.ActivityDashboardBinding;

public class AdminActivity extends DrawerAdminActivity{

    ActivityAdminBinding activityAdminBinding;
    Button productBtn, orderBtn, profileBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdminBinding = ActivityAdminBinding.inflate(getLayoutInflater());
        allocateActivityTitle("Dashboard");
        setContentView(activityAdminBinding.getRoot());

        productBtn = findViewById(R.id.productBtn);
        orderBtn = findViewById(R.id.orderBtn);
        profileBtn = findViewById(R.id.profileBtn);

        productBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AdminViewProduct.class));
            }
        });
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, d2.class));
            }
        });
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AdminProfile.class));
            }
        });
    }
}
