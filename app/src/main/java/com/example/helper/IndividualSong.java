package com.example.helper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class IndividualSong extends AppCompat {


    private ArrayList<Integer> ItemList1 = new ArrayList<Integer>(6);
    private ArrayList<Integer> ItemList2 = new ArrayList<Integer>(6);
    private ArrayList<Integer> ItemList3 = new ArrayList<Integer>(6);
    private ArrayList<Integer> ItemList4 = new ArrayList<Integer>(6);

    private ArrayList<Integer> ImgList1 = new ArrayList<Integer>(6);
    private ArrayList<Integer> ImgList2 = new ArrayList<Integer>(6);
    private ArrayList<Integer> ImgList3 = new ArrayList<Integer>(6);
    private ArrayList<Integer> ImgList4 = new ArrayList<Integer>(6);


    MyMusicAdapter adapter;
    RecyclerView rvs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_song);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        rvs = findViewById(R.id.rvs);

        ItemList1.addAll(Arrays.asList(R.raw.music1,R.raw.music2,R.raw.music3,R.raw.music4,R.raw.music5,R.raw.music6));
        ItemList2.addAll(Arrays.asList(R.raw.music7,R.raw.music8,R.raw.music9,R.raw.music10,R.raw.music11,R.raw.music12));
        ItemList3.addAll(Arrays.asList(R.raw.music13,R.raw.music14,R.raw.music15,R.raw.music16,R.raw.music17,R.raw.music18));
        ItemList4.addAll(Arrays.asList(R.raw.music19,R.raw.music20,R.raw.music21,R.raw.music22,R.raw.music23,R.raw.music24));

        ImgList1.addAll(Arrays.asList(R.drawable.music1,R.drawable.music2,R.drawable.music3,R.drawable.music4,R.drawable.music5,R.drawable.music6));
        ImgList2.addAll(Arrays.asList(R.drawable.music7,R.drawable.music8,R.drawable.music9,R.drawable.music10,R.drawable.music11,R.drawable.music12));
        ImgList3.addAll(Arrays.asList(R.drawable.music13,R.drawable.music14,R.drawable.music15,R.drawable.music16,R.drawable.music17,R.drawable.music18));
        ImgList4.addAll(Arrays.asList(R.drawable.music19,R.drawable.music20,R.drawable.music21,R.drawable.music22,R.drawable.music23,R.drawable.music24));



        //        code to fetch data from previous page
        String item = (String) getIntent().getSerializableExtra("type");

        if(item.equals("english")){
            adapter = new MyMusicAdapter(ItemList1,ImgList1,IndividualSong.this);
        }
        else if(item.equals("hindi")){
            adapter = new MyMusicAdapter(ItemList2,ImgList2,IndividualSong.this);
        }
        else if(item.equals("marathi")){
            adapter = new MyMusicAdapter(ItemList3,ImgList3,IndividualSong.this);
        }
        else{
            adapter = new MyMusicAdapter(ItemList4,ImgList4,IndividualSong.this);
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvs.setLayoutManager(layoutManager);
        rvs.setItemAnimator(new DefaultItemAnimator());
        rvs.setAdapter(adapter);

    }

    public void play_music(){

//        String urls = "R.raw."+idname;
        MediaPlayer mediaPlayer = MediaPlayer.create(IndividualSong.this, R.raw.music1);
        mediaPlayer.start();
    }
}