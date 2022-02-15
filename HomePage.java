package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ticketbooking.model.Globals;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomePage extends AppCompatActivity {

    Button showButt;
    Button reservtionButt;
    Button reportButt;
    Button exitButt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        showButt= findViewById(R.id.button5);
        reservtionButt= findViewById(R.id.button6);
        reportButt= findViewById(R.id.button7);
        exitButt= findViewById(R.id.button8);








        showButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Globals.userstatus.equals("admin")){
                    Intent i = new Intent(HomePage.this, ShowHomePage.class);
                    startActivity(i);
                }
                else{
                    Context context = getApplicationContext();
                    CharSequence text = "ONLY BOOKING MENAGERS CAN USE THIS SERVICE!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        reportButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Globals.userstatus.equals("admin")){
                    Intent i = new Intent(HomePage.this, CreateRapor.class);
                    startActivity(i);
                }
                else{
                    Context context = getApplicationContext();
                    CharSequence text = "ONLY BOOKING MENAGERS CAN USE THIS SERVICE!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }



            }
        });
        reservtionButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent i = new Intent(HomePage.this, ReservationsMain.class);
                    startActivity(i);

            }
        });








        exitButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
                finish();
            }
        });
    }

}