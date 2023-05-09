package com.example.helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ContactPage extends AppCompat {

    Button btnCon;
    GridView rv;
    String android_id,currUser;

    private DatabaseReference db;

    private ArrayList<CheckContact> myItems = new ArrayList <>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_page);
        btnCon=findViewById(R.id.btnFinal);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        btnCon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i=new Intent(ContactPage.this,MainContact.class);
                startActivity(i);
            }
        });

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

        db = FirebaseDatabase.getInstance().getReference().child("contacts");

        rv = findViewById(R.id.rv);
    }

    void go_further(){
        db.child(currUser).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myItems.clear();

                if(snapshot.exists()){
                    rv.setVisibility(View.VISIBLE);
                    for(DataSnapshot user:snapshot.getChildren()){
                        final String fullname = user.child("name").getValue(String.class);
                        final String phoneno = user.child("number").getValue(String.class);
                        final String image = user.child("image").getValue(String.class);

                        CheckContact ch = new CheckContact(image,fullname,phoneno);
                        myItems.add(ch);
                    }
                    rv.setAdapter(new RecyclerViewAdapter(myItems,ContactPage.this));
                    rv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            // Get the item that was clicked
                            CheckContact item = myItems.get(i);
                            // Start the next activity and pass the item data to it
                            Intent intent = new Intent(getApplicationContext(), IndividualContact.class);
                            intent.putExtra("ITEM_DATA", item);
                            startActivity(intent);
                        }
                    });
                }
                else{
                    TextView t;
                    rv.setVisibility(View.GONE);
                    t = findViewById(R.id.no_contact_present);
                    t.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}