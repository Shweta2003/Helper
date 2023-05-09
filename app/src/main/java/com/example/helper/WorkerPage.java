package com.example.helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;

public class WorkerPage extends AppCompat {

    ImageButton caretaker, plumber, cook, driver, electrician, housekeeper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_page);

        caretaker = findViewById(R.id.caretaker);
        plumber = findViewById(R.id.plumber);
        cook = findViewById(R.id.cook);
        driver = findViewById(R.id.driver);
        electrician = findViewById(R.id.electrician);
        housekeeper = findViewById(R.id.househelp);

//        hide header
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

//        go to next page
        housekeeper.setOnClickListener(view -> {
            Intent intent = new Intent(WorkerPage.this,GetWorker.class);
            intent.putExtra("Val","HouseKeeper");
            startActivity(intent);

        });

        electrician.setOnClickListener(view -> {
            Intent intent = new Intent(WorkerPage.this,GetWorker.class);
            intent.putExtra("Val","Electrician");
            startActivity(intent);

        });

        cook.setOnClickListener(view -> {
            Intent intent = new Intent(WorkerPage.this,GetWorker.class);
            intent.putExtra("Val","Cook");
            startActivity(intent);

        });

        plumber.setOnClickListener(view -> {
            Intent intent = new Intent(WorkerPage.this,GetWorker.class);
            intent.putExtra("Val","Plumber");
            startActivity(intent);

        });

        driver.setOnClickListener(view -> {
            Intent intent = new Intent(WorkerPage.this,GetWorker.class);
            intent.putExtra("Val","Driver");
            startActivity(intent);

        });

        caretaker.setOnClickListener(view -> {
            Intent intent = new Intent(WorkerPage.this,GetWorker.class);
            intent.putExtra("Val","CareTaker");
            startActivity(intent);

        });
    }
}