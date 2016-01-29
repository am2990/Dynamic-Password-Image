package edu.iiitd.dynamikpass.model;

import java.util.ArrayList;

/**
 * Created by Shreya Mehrishi on 12/13/2015.
 */
public class User {
    //private variables
    int id;
    String username;
    //ArrayList<Image> password;
    String imgpassword;
    String gesturearr;
    int imageback;

    // Empty constructor
    public User() {
    }
    // getting ID
    public int getID(){
        return this.id;
    }
    public void setID(int id){
        this.id = id;
    }
    // setting id
    public void setImageback(int imageback){
        this.imageback = imageback;
    }
    public int getImageback(){
        return this.imageback;
    }

    // setting id

    // getting username
    public String getUsername(){
        return this.username;
    }

    // setting username
    public void setUsername(String username){
        this.username = username;
    }
    //getting password
    public String getImgPassword(){
        return this.imgpassword;
    }

    // setting username
    public void setImgPassword(String imgpassword){
        this.imgpassword = imgpassword;
    }
    public String getGestarr(){
        return this.gesturearr;
    }

    // setting username
    public void setGestarr(String gesturearr){
        this.gesturearr = gesturearr;
    }



}
