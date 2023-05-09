package com.example.helper;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class register extends AppCompat {
    private Button register;
    private EditText username,phoneno,address,guardian_name,guardian_number;

    List<String> names = new ArrayList<>();

    EditText state,city;
    private EditText password;
    private TextView Loginb;

    CollectionReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        code to remove header
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

//        get data from user
        username = findViewById(R.id.username);
        password = findViewById(R.id.Password);
        register = findViewById(R.id.register);
        city = findViewById(R.id.username2);
        state = findViewById(R.id.state);
        phoneno = findViewById(R.id.contact_of_contact);
        address = findViewById(R.id.username3);
        guardian_name = findViewById(R.id.guardian_name);
        guardian_number = findViewById(R.id.editTextPhone2);

        db = FirebaseFirestore.getInstance().collection("User_details");

//        go to login page if user already has account
        Loginb = findViewById(R.id.go_to_login);
        Loginb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(register.this,Login.class));
            }
        });


//        store the data into firestore database
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userdata = username.getText().toString();
                String passdata = password.getText().toString();
                if(TextUtils.isEmpty(userdata) || TextUtils.isEmpty(passdata)){
                    Toast.makeText(register.this,R.string.ask_enter_all_fields,Toast.LENGTH_SHORT).show();
                    username.setText("");
                    password.setText("");
                }
                else{
//                    this part of code is used to check whether document is present in collection or not
                    db.document(userdata).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Toast.makeText(register.this,R.string.name_in_use,Toast.LENGTH_SHORT).show();
                                    username.setText("");
                                    password.setText("");
                                } else {
                                    Map<String,String> v = new HashMap<>();
                                    v.put("username",userdata);
                                    v.put("password",passdata);
                                    v.put("phone number",phoneno.getText().toString());
                                    v.put("state",state.getText().toString().toLowerCase());
                                    v.put("city",city.getText().toString().toLowerCase());
                                    v.put("address",address.getText().toString());
                                    v.put("guardian name",guardian_name.getText().toString());
                                    v.put("guardian number",guardian_number.getText().toString());
                                    db.document(userdata).set(v).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(register.this,R.string.register_success,Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(register.this,MainActivity.class));
                                        }
                                    });
                                }
                            } else {
                                Log.d("TAG", "Error getting document: ", task.getException());
                            }
                        }
                    });

                }
            }
        });
    }


}