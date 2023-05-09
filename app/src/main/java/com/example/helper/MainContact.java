package com.example.helper;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.Request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainContact extends AppCompat implements CallBack {

    public final int CAMERA_REQUEST=4737;

    byte[] data1;
    ImageView imgVw;
    Button btnSave,btnPhoto;
    EditText txtNme,txtCon;
    String phone;
    String android_id,currUser;

    Bitmap photo;

    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_contact);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        View vw = findViewById(R.id.vwHead);
        vw.setBackgroundColor(Color.rgb(120,189,40));

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        FirebaseFirestore.getInstance().collection("currently logged in").document(android_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    currUser = task.getResult().getString("username");
                }
            }
        });
        db = FirebaseDatabase.getInstance().getReference().child("contacts");

        imgVw = findViewById(R.id.imgCon);
        btnSave = findViewById(R.id.btnFinal);
        btnPhoto = findViewById(R.id.btnImg);
        txtNme = findViewById(R.id.txtName);
        txtCon = findViewById(R.id.txtPh);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, CAMERA_REQUEST);
            }
        });
        btnSave.setOnClickListener(view -> {
            check_for_valid();
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
//        Uri imgUri=data.getData();
        if(requestCode==CAMERA_REQUEST)
        {
            photo=(Bitmap) data.getExtras().get("data");
            imgVw.setImageBitmap(photo);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG,100,baos);
            data1 = baos.toByteArray();
//            imgVw.setImageURI(imgUri);
//            video_view.start();

        }
    }

    void check_for_valid(){
        if(TextUtils.isEmpty(txtNme.getText())){
            Toast.makeText(MainContact.this,R.string.ask_enter_all_fields,Toast.LENGTH_SHORT).show();
            txtNme.setText("");
            txtNme.requestFocus();
        }
        else if(TextUtils.isEmpty(txtCon.getText()) || txtCon.getText().length() != 10){
            Toast.makeText(MainContact.this,R.string.valid_number,Toast.LENGTH_SHORT).show();
            txtCon.setText("");
            txtCon.requestFocus();
        }
        else{
            check_if_exist();
//            onResult(false);
        }
    }


    void check_if_exist(){

        db.child(currUser).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean found = false;
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        CheckContact i = dataSnapshot.getValue(CheckContact.class);
                        if(txtCon.getText().toString().equals(i.number)){
                            Toast.makeText(MainContact.this,R.string.already_exist,Toast.LENGTH_SHORT).show();
                            found = true;
                            break;
                        }
                        else if(txtNme.getText().toString().equals(i.name)){
                            Toast.makeText(MainContact.this,R.string.name_in_use,Toast.LENGTH_SHORT).show();
                            found = true;
                            break;
                        }
                        else{
                            continue;
                        }
                    }
                }
                onResult(found);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onResult(false);
            }
        });
    }


    @Override
    public void onResult(boolean found) {
        if(found == false){
            if(!txtNme.getText().toString().toString().isEmpty() && !txtNme.getText().toString().toString().isEmpty() && !photo.toString().isEmpty()) {
                String encodedData = Base64.encodeToString(data1, Base64.DEFAULT);
                Map<String, String> v = new HashMap<>();
                v.put("name", txtNme.getText().toString());
                v.put("number", txtCon.getText().toString());
                v.put("image", encodedData);
                db.child(currUser).child(txtNme.getText().toString()).setValue(v).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainContact.this, R.string.save_contact, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainContact.this, ContactPage.class));
                    }
                });
            }
        }
            txtCon.setText("");
            txtNme.setText("");
            imgVw.setImageBitmap(null);
            txtNme.requestFocus();
//            check_for_valid();
    }

}

