package com.example.gas4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gas4u.databinding.ActivityAdminBinding;
import com.example.gas4u.databinding.ActivityDashboardBinding;

public class AdminActivity extends DrawerAdminActivity{

    ActivityAdminBinding activityAdminBinding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdminBinding = ActivityAdminBinding.inflate(getLayoutInflater());
        allocateActivityTitle("Dashboard");
        setContentView(activityAdminBinding.getRoot());
    }
}
