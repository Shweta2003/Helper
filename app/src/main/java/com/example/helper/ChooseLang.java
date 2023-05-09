package com.example.helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class ChooseLang extends AppCompat {

    ImageButton ben, bhn, bmr, bbg;
    Button go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_lang);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        ben = findViewById(R.id.ChooseEng);
        ben.setBackgroundColor(Color.rgb(241,239,239));

        bhn = findViewById(R.id.ChooseHin);
        bhn.setBackgroundColor(Color.rgb(241,239,239));

        bmr = findViewById(R.id.ChooseMar);
        bmr.setBackgroundColor(Color.rgb(241,239,239));

        bbg = findViewById(R.id.ChooseBen);
        bbg.setBackgroundColor(Color.rgb(241,239,239));

        go = findViewById(R.id.go_to_main);

        go.setBackgroundColor(Color.BLACK);
        LanguageManager lang = new LanguageManager(this);

        ben.setOnClickListener(view -> {
            ben.setBackgroundColor(Color.WHITE);
            bmr.setBackgroundColor(Color.rgb(241,239,239));
            bbg.setBackgroundColor(Color.rgb(241,239,239));
            bhn.setBackgroundColor(Color.rgb(241,239,239));

            lang.updateResource("en");
//            recreate();
        });

        bhn.setOnClickListener(view -> {
            bhn.setBackgroundColor(Color.WHITE);
            bmr.setBackgroundColor(Color.rgb(241,239,239));
            bbg.setBackgroundColor(Color.rgb(241,239,239));
            ben.setBackgroundColor(Color.rgb(241,239,239));
            lang.updateResource("hi");
//            recreate();
        });

        bmr.setOnClickListener(view -> {
            bmr.setBackgroundColor(Color.WHITE);
            ben.setBackgroundColor(Color.rgb(241,239,239));
            bbg.setBackgroundColor(Color.rgb(241,239,239));
            bhn.setBackgroundColor(Color.rgb(241,239,239));
            lang.updateResource("mr");
//            recreate();
        });

        bbg.setOnClickListener(view -> {
            bbg.setBackgroundColor(Color.WHITE);
            bmr.setBackgroundColor(Color.rgb(241,239,239));
            ben.setBackgroundColor(Color.rgb(241,239,239));
            bhn.setBackgroundColor(Color.rgb(241,239,239));
            lang.updateResource("bn");
//            recreate();
        });

        go.setOnClickListener(view -> {

            startActivity(new Intent(ChooseLang.this,Login.class));
        });
    }
}