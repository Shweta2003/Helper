package com.example.helper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainMenu extends AppCompat {
    CardView contact, getWorker, reminder, donate;
    Button logout;

    View view2;

    CollectionReference db;

    String android_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        contact = findViewById(R.id.contact);

        getWorker = findViewById(R.id.get_worker);

        reminder = findViewById(R.id.reminder);

        donate = findViewById(R.id.donate);

        view2 = findViewById(R.id.view2);
        view2.setOnClickListener(view -> {
            startActivity(new Intent(MainMenu.this,SOS.class));
        });

        db = FirebaseFirestore.getInstance().collection("currently logged in");

        contact.setOnClickListener(view -> {
            startActivity(new Intent(MainMenu.this,ContactPage.class));
        });

        getWorker.setOnClickListener(view -> {
            startActivity(new Intent(MainMenu.this,WorkerPage.class));
        });

        reminder.setOnClickListener(view -> {
            startActivity(new Intent(MainMenu.this,CalenderPage.class));
        });

        donate.setOnClickListener(view -> {
            startActivity(new Intent(MainMenu.this,MusicPage.class));
        });


        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        logout = findViewById(R.id.logout_btn);
        logout.setOnClickListener(view -> {
            db.document(android_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    startActivity(new Intent(MainMenu.this,Login.class));
                }
            });
        });


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }
}