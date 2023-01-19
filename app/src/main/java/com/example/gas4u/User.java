package com.example.gas4u;

public class User {

    public String userName;
    public String userPassword;
    public String phoneNo;
    public String email;
    public String fullName;
    public String address;
    public long age;

    public User() {

    }


    public User(String userName, String userPassword, String phoneNo, String email, String fullName, String address, long age) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.phoneNo = phoneNo;
        this.email = email;
        this.fullName = fullName;
        this.address = address;
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getAge() {return age;}

    public void setAge(long age) {this.age = age;}
}
