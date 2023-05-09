package com.example.helper;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;

public class CheckWorker implements Serializable {
    public String name,number;
    public String image,status;

    public CheckWorker(){

    }

    public CheckWorker(String image, String name, String number, String status ){
        this.image = image;
        this.name = name;
        this.number = number;
        this.status = status;

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

    public String getStatus(){return status;}

    public void setName(String name){
        this.name = name;
    }

    public void setNumber(String number){
        this.number = number;
    }

    public void setImage(String image){
        this.image = image;
    }

    public void setStatus(String status){this.status = status;}

}
