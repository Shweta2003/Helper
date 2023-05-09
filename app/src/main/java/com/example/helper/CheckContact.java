package com.example.helper;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class CheckContact implements Serializable {
    public String name,number;
    public String image;

    public CheckContact(){

    }

    public CheckContact(String image, String name, String number){
        this.image = image;
        this.name = name;
        this.number = number;

    }

    public String getName(){
        return name;
    }

    public String getNumber(){
        return number;
    }

    public String getImage(){
        return image;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setNumber(String number){
        this.number = number;
    }

    public void setImage(String Image){
        this.image = Image;
    }



}

interface CallBack{
    void onResult(boolean found);
}



