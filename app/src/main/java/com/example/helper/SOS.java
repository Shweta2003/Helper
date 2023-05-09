package com.example.helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SOS extends AppCompat{

    TextView name, number, state, city, address, guardian_name,g_number;
    Button sendCall;

    String android_id, currUser, numberA;

    CollectionReference db;

    static int PERMISSION_CODE=100;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        name = findViewById(R.id.username_sos);
        number = findViewById(R.id.phone_no_sos);
        state = findViewById(R.id.state_sos);
        city = findViewById(R.id.city_sos);
        address = findViewById(R.id.address_sos);
        guardian_name = findViewById(R.id.guardian_name_sos);
        g_number = findViewById(R.id.g_number_sos);
        sendCall = findViewById(R.id.emergency);

        db = FirebaseFirestore.getInstance().collection("User_details");

        //      this code generates a unique androidid to identify your android device in logged in or not
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        FirebaseFirestore.getInstance().collection("currently logged in").document(android_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    currUser = task.getResult().getString("username");
                    go_further();
                }
            }
        });

    }
    void go_further(){

        db.document(currUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name.setText(document.get("username").toString());
                        number.setText(document.get("phone number").toString());
                        state.setText(document.get("state").toString());
                        city.setText(document.get("city").toString());
                        address.setText(document.get("address").toString());
                        guardian_name.setText(document.get("guardian name").toString());
                        g_number.setText(document.get("guardian number").toString());
                        numberA = document.get("phone number").toString();

                        sendCall.setOnClickListener(view -> {
                            if (numberA.length()==10){
                                Intent i = new Intent(Intent.ACTION_CALL);
                                i.setData(Uri.parse("tel:" + numberA));
                                startActivity(i);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),R.string.valid_number,Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }
        });
    }
}