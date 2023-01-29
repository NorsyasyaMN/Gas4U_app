package com.example.gas4u;

public class User {

    public String userName;
    public String userPhone;
    public String userEmail;
    public String profilePhoto;

    public User(){
    }

    public User(String userName, String userPhone, String userEmail, String profilePhoto) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.profilePhoto = profilePhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
