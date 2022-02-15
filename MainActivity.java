package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ticketbooking.model.Globals;
import com.example.ticketbooking.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //register yapılacak LOGİN Lİ OLCAK 2 tur olacak bm ve audience
    //
    //3 use case yapılacak
    // BM CRUD Shows                 1
    // BM CRUD reservation           2
    // audience CRUD reservation     2
    // BM generate reports           3

    Button registerButton;
    Button homePageButton;
    EditText username;
    EditText password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=findViewById(R.id.editTextTextEmailAddress);
        password=findViewById(R.id.editTextTextPassword);
        registerButton = findViewById(R.id.button3);
        homePageButton = findViewById(R.id.button4);
        FirebaseFirestore db = FirebaseFirestore.getInstance();




        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Register.class);
                startActivity(i);
            }
        });
        homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    boolean temp=true;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("TAG", document.getId() + " => " + document.getData());
                                        String usernameTemp=document.getString("username");
                                        String passwordTem=document.getString("password");
                                        if (usernameTemp.toString().equals(username.getText().toString())&&passwordTem.toString().equals(password.getText().toString())){

                                            Globals.userstatus=document.getString("type");
                                            Globals.username=document.getString("username");
                                            temp=false;
                                            Intent i = new Intent(MainActivity.this, HomePage.class);
                                            startActivity(i);
                                        }
                                    }
                                    if (temp){
                                        Context context = getApplicationContext();
                                        CharSequence text = "Username And Password NOT match! Try again.";
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();
                                    }
                                    else{
                                        Context context = getApplicationContext();
                                        CharSequence text = "Login success for "+username.getText().toString()+".";
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();
                                    }

                                } else {
                                    Log.w("TAG", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });

    }
}