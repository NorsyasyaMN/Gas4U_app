package com.example.gas4u;

public class User {

    public String userName;
    public String userPassword;
    public String phoneNo;
    public String email;

    public User() {

    }

    public User (String userName, String password, String mobileNo, String email){
        this.userName = userName;
        this.userPassword = password;
        this.phoneNo = mobileNo;
        this.email = email;
    }
}
