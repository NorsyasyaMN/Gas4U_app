package com.example.gas4u;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gas4u.databinding.ActivityViewHelplineBinding;

public class ViewHelplineActivity extends DrawerBaseActivity {

    ActivityViewHelplineBinding activityViewHelplineBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewHelplineBinding = ActivityViewHelplineBinding.inflate(getLayoutInflater());
        setContentView(activityViewHelplineBinding.getRoot());
        allocateActivityTitle("Helpline");
    }
}