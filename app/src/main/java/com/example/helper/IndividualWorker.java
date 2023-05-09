package com.example.helper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class IndividualWorker extends AppCompat {
    Button status, book, back, free;
    ImageView image;
    TextView name, number,head;

    FloatingActionButton call;

    static int PERMISSION_CODE=100;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_worker);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

//      code to fetch data from previous page
        CheckWorker item = (CheckWorker) getIntent().getSerializableExtra("send_item");
        Log.d("item-val", String.valueOf(item));

        String category = (String) getIntent().getSerializableExtra("worker_category");

        int index_position = (int) getIntent().getSerializableExtra("index_pos");

        call = findViewById(R.id.call);
        status = findViewById(R.id.tell_status);
        book = findViewById(R.id.book_now);
        back = findViewById(R.id.go_back);
        image = findViewById(R.id.imageView8);
        name = findViewById(R.id.name_of_worker);
        number = findViewById(R.id.number_of_worker);
        free = findViewById(R.id.free);
        free.setVisibility(View.GONE);
        head = findViewById(R.id.textView20);
        head.setVisibility(View.GONE);

        name.setText(item.getName());
        number.setText(item.getNumber());

        Picasso.get().load(item.getImage()).into(image);

        if(item.getStatus().equals("true")){
            status.setBackgroundColor(Color.GREEN);
            status.setTextColor(Color.BLACK);
            status.setText(R.string.available);
        }
        else{
            status.setBackgroundColor(Color.RED);
            status.setText(R.string.reserved);
        }

        back.setOnClickListener(view -> {
            startActivity(new Intent(IndividualWorker.this,GetWorker.class));
        });

        book.setOnClickListener(view -> {
            if(item.getStatus().equals("true")){
                FirebaseDatabase.getInstance().getReference().child("Workers").child(category).child(String.valueOf(index_position)).child("status").setValue("false").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(IndividualWorker.this,R.string.book_worker,Toast.LENGTH_SHORT).show();
                        Toast.makeText(IndividualWorker.this,R.string.call_now_worker,Toast.LENGTH_SHORT).show();
                        book.setVisibility(View.GONE);
                        free.setVisibility(View.VISIBLE);
                        head.setVisibility(View.VISIBLE);
                    }
                }) ;
            }
        });

        //        String phone=txtNum.getText().toString();
        if (ContextCompat.checkSelfPermission(IndividualWorker.this, android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED);
        {
            ActivityCompat.requestPermissions(IndividualWorker.this,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_CODE);

        }

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getNumber().length()==10){
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:" + item.getNumber()));
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid Number",Toast.LENGTH_SHORT).show();
                }
            }
        });

        free.setOnClickListener(view -> {
            FirebaseDatabase.getInstance().getReference().child("Workers").child(category).child(String.valueOf(index_position)).child("status").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(IndividualWorker.this,R.string.task_complete,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(IndividualWorker.this,MainMenu.class));
                }
            });
        });
    }
}