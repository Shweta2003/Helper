package com.example.helper;

import java.io.Serializable;

public class CheckMusic implements Serializable {
    String img;

    public String getImg() {
        return img;
    }

    public String getVideo() {
        return video;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    String video;
    
    public CheckMusic(){}
    
    public CheckMusic(String img, String video){
        this.img = img;
        this.video = video;
    }
}
