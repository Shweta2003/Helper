package com.example.helper;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class IndividualContact extends AppCompat {

    TextView fullname,number;
    ImageView image;
    String android_id,currUser;
    Button delete;

    ImageButton btnCall;
    static int PERMISSION_CODE=100;
    @SuppressLint("WrongViewCast")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_contact);

//        code to fetch data from previous page
        CheckContact item = (CheckContact) getIntent().getSerializableExtra("ITEM_DATA");
        Log.d("item-val", String.valueOf(item));

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        fullname = findViewById(R.id.name_of_contact);
        number = findViewById(R.id.contact_of_contact);
        image = findViewById(R.id.image_of_contact);
        fullname.setText(item.getName());
        number.setText(item.getNumber());
        byte[] data = Base64.decode(item.getImage(), Base64.DEFAULT);
        image.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));

        //        btnCall=findViewById(R.id.btnCall);
        btnCall=findViewById(R.id.btnCalling);

        //        String phone=txtNum.getText().toString();
        if (ContextCompat.checkSelfPermission(IndividualContact.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED);
        {
            ActivityCompat.requestPermissions(IndividualContact.this,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_CODE);

        }
        btnCall.setOnClickListener(new View.OnClickListener() {
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

        delete = findViewById(R.id.delete_btn);
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        FirebaseFirestore.getInstance().collection("currently logged in").document(android_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    currUser = task.getResult().getString("username");
                    perform_deletion(item.getName());
                }
            }
        });


    }

    protected void perform_deletion(String name){
        delete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.ask_before_delete)
                    .setCancelable(false)
                    .setPositiveButton(R.string.say_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked Yes button
                            // Perform the delete operation
                            FirebaseDatabase.getInstance().getReference().child("contacts").child(currUser).child(name).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(IndividualContact.this,R.string.deleted,Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(IndividualContact.this,ContactPage.class));
                                }
                            });
                        }
                    })
                    .setNegativeButton(R.string.say_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked No button
                            // Dismiss the dialog
                            dialog.cancel();
                            Toast.makeText(IndividualContact.this,R.string.no_delete,Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(IndividualContact.this,ContactPage.class));
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        });
    }

}