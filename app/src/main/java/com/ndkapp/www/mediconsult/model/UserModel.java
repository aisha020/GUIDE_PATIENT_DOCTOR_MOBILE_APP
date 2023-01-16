package com.ndkapp.www.mediconsult.model;

public class UserModel{
    String name;
    String email;
    String password;
    String password_c;

    public UserModel(){
    }

    public UserModel(String email,String password,String password_c){
      //  this.name=name;
        this.email=email;
        this.password=password;
        this.password_c=password_c;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_c() {
        return password_c;
    }

    public void setPassword_c(String password) {
        this.password_c = password_c;
    }
}
