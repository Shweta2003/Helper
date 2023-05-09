package com.example.helper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class GetWorker extends AppCompat {

    RecyclerView rv;

    Button b1;

    private ArrayList<CheckWorker> myItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_worker);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        rv = findViewById(R.id.rv1);
        b1 = findViewById(R.id.button);
        String item = (String) getIntent().getSerializableExtra("Val");
        FirebaseDatabase.getInstance().getReference().child("Workers").child(item).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                myItems.clear();
                for(DataSnapshot element : dataSnapshot.getChildren()){
                    Log.d("got_items",element.child("name").getValue(String.class));
                    CheckWorker item = new CheckWorker(element.child("image").getValue(String.class),
                            element.child("name").getValue(String.class),
                            element.child("number").getValue(String.class),
                            element.child("status").getValue(String.class));

                    myItems.add(item);
                }
                MyWorkerAdapter adapter = new MyWorkerAdapter(myItems);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                rv.setLayoutManager(layoutManager);
                rv.setItemAnimator(new DefaultItemAnimator());
                rv.setAdapter(adapter);


            }
        });

        b1.setBackgroundColor(Color.rgb(244,67,54));

        b1.setOnClickListener(view -> {
            Random rand = new Random();
            int index = rand.nextInt(myItems.size());

            CheckWorker randomElement = myItems.get(index);

            Intent intent = new Intent(GetWorker.this,IndividualWorker.class);
            intent.putExtra("send_item",randomElement);
            intent.putExtra("worker_category",item);
            intent.putExtra("index_pos",index);
            startActivity(intent);
        });
    }
}