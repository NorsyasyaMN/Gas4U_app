package com.example.gas4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class splashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //TextView textView = findViewById(R.id.textSplashScreen);
        //textView.animate().translationX(-1050).setDuration(1000).setStartDelay(2500);

        Thread thread = new Thread(){

            public void run(){
                try{
                    Thread.sleep(4000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally{
                    Intent intent = new Intent(splashActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();

    }

    public static class Customer {

        String Email, Username;
        Long PhoneNumber;

        //constructor
        public Customer(){}

        public Customer(String email, String username, Long phoneNumber) {
            Email = email;
            Username = username;
            PhoneNumber = phoneNumber;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            Email = email;
        }


        public String getUsername() {
            return Username;
        }

        public void setUsername(String username) {
            Username = username;
        }

        public Long getPhoneNumber() {
            return PhoneNumber;
        }

        public void setPhoneNumber(Long phoneNumber) {
            PhoneNumber = phoneNumber;
        }
    }
}