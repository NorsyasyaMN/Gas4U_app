package com.example.gas4u;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

public class Feedback extends AppCompatActivity {

    TextView tvFeedback;
    RatingBar rbStars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        tvFeedback = findViewById(R.id.tvFeedback);
        rbStars = findViewById(R.id.rbStars);

        rbStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean frontUser) {
                if(rating==0){
                tvFeedback.setText("Very Dissatisfied");}
                else if(rating == 1){
                    tvFeedback.setText(" Dissatisfied");}
                else if(rating == 2 || rating ==3){
                    tvFeedback.setText(" Okay");}
                else if(rating == 4){
                    tvFeedback.setText("Satisfied");}
                else if(rating == 5){
                    tvFeedback.setText(" Very Satisfied");}
                else {
                    tvFeedback.setText("No Rating");
                }
            }
        });

    }
}