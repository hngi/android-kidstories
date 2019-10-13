package com.project.android_kidstories.db;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class DbUserClass {
    int dbUserId;
    int id;
    byte[] userImageByteArray;
    Bitmap userImageBitmap;

    String firstName,lastName;

    public void setUserImageByteArray(byte[] userImageByteArray){
        this.userImageByteArray = userImageByteArray;
    }

    public byte[] getUserImageByteArray(){
        return userImageByteArray;
    }

    public void setUserImageBitmap(Bitmap userImageBitmap){
        this.userImageBitmap = userImageBitmap;
    }

    public Bitmap getUserImageBitmap(){
        return userImageBitmap;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setDbUserId(int userId){
        this.dbUserId = userId;
    }

    public int getDbUserId(){
        return dbUserId;
    }

    public void setUserId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }
}
