package com.example.helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompat {
    private Button login;

    String android_id;

    Context context = this;

    String currIp;
    private EditText username;
    private EditText password;
    private TextView registerb;

    CollectionReference db, dblog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        login = findViewById(R.id.loginl);
        username = findViewById(R.id.userlogin);
        password = findViewById(R.id.Passwordl);
        registerb = findViewById(R.id.to_register);
        dblog = FirebaseFirestore.getInstance().collection("currently logged in");

//      this code generates a unique androidid to identify your android device in logged in or not
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        db = FirebaseFirestore.getInstance().collection("User_details");

        registerb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, register.class));
            }
        });

        login.setOnClickListener(view -> {
            loginUser();
        });
    }

    protected void loginUser(){
        String userData = username.getText().toString();
        String passData = password.getText().toString();


        if(TextUtils.isEmpty(userData)){

        Toast.makeText(Login.this,R.string.ask_enter_all_fields,Toast.LENGTH_SHORT).show();
        username.requestFocus();
    }else if(TextUtils.isEmpty(passData)){
        Toast.makeText(Login.this,R.string.please_enter_password,Toast.LENGTH_SHORT).show();
        password.requestFocus();
    }else{
            Map<String,String> v = new HashMap<>();
            v.put("androidId",android_id);
            v.put("username",userData);
            //                    this part of code is used to check whether document is present in collection or not
            db.document(userData).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if (passData.equals(document.get("password").toString())){
                                dblog.document(android_id).set(v).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        startActivity(new Intent(Login.this,MainActivity.class));
                                    }
                                });
                            }
                            else{
                                Toast.makeText(Login.this,R.string.wrong_password,Toast.LENGTH_SHORT).show();
                                username.setText("");
                                password.setText("");
                            }
                        } else {
                            Toast.makeText(Login.this,R.string.user_not_found,Toast.LENGTH_SHORT).show();
                            username.setText("");
                            password.setText("");
                        }
                    } else {
                        Log.d("TAG", "Error getting document: ", task.getException());
                    }
                }
            });
        }
    }
}