package com.example.helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MusicPage extends AppCompat {

    ImageView english, hindi, marathi, bengali;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_page);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        english = findViewById(R.id.english);
        hindi = findViewById(R.id.hindi);
        marathi = findViewById(R.id.marathi);
        bengali = findViewById(R.id.bengali);

        english.setOnClickListener(view -> {
            Intent intent = new Intent(MusicPage.this,IndividualSong.class);
            intent.putExtra("type","english");
            startActivity(intent);
        });

        hindi.setOnClickListener(view -> {
            Intent intent = new Intent(MusicPage.this,IndividualSong.class);
            intent.putExtra("type","hindi");
            startActivity(intent);
        });

        marathi.setOnClickListener(view -> {
            Intent intent = new Intent(MusicPage.this,IndividualSong.class);
            intent.putExtra("type","marathi");
            startActivity(intent);
        });

        bengali.setOnClickListener(view -> {
            Intent intent = new Intent(MusicPage.this,IndividualSong.class);
            intent.putExtra("type","bengali");
            startActivity(intent);
        });
    }
}