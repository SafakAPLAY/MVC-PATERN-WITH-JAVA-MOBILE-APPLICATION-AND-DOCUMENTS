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
import com.example.ticketbooking.model.Show;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ShowHomePage extends AppCompatActivity {
    Button update;
    Button remove;
    Button create;
    Button back;
    EditText list;
    EditText showSerchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_home_page);
        update=findViewById(R.id.button10);
        remove=findViewById(R.id.button9);
        create=findViewById(R.id.button11);
        back=findViewById(R.id.button12);
        list=findViewById(R.id.editTextTextMultiLine);
        showSerchName=findViewById(R.id.editTextTextPersonName3);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("shows")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String temp="Show List:";
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                String tempUsername = document.getString("showname");
                                temp=temp+"\n"+tempUsername;
                            }
                            list.setText(temp);
                        }
                    }
                });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ShowHomePage.this, HomePage.class);
                startActivity(i);
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection("shows")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("TAG", document.getId() + " => " + document.getData());

                                        String tempUsername=document.getString("showname");
                                        if (tempUsername.equals(showSerchName.getText().toString())){

                                            Context context = getApplicationContext();
                                            CharSequence text = "Show name"+showSerchName.getText().toString()+"has been deleted!";
                                            int duration = Toast.LENGTH_SHORT;
                                            Toast toast = Toast.makeText(context, text, duration);
                                            toast.show();

                                            db.collection("shows").document(document.getId())
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            db.collection("shows")
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                String temp="Show List:";
                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                                                                    String tempUsername = document.getString("showname");
                                                                                    temp=temp+"\n"+tempUsername;
                                                                                }
                                                                                list.setText(temp);
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });
                                        }
                                    }
                                } else {
                                    Log.w("TAG", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ShowHomePage.this, CreateShow.class);
                startActivity(i);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("shows")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    boolean temp=true;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("TAG", document.getId() + " => " + document.getData());

                                        String tempUsername = document.getString("showname");
                                        if (tempUsername.equals(showSerchName.getText().toString())) {
                                            temp=false;
                                            Globals.selectedShowDef=document.getString("def");
                                            Globals.selectedShow=showSerchName.getText().toString();
                                            Intent i = new Intent(ShowHomePage.this, UpdateShow.class);
                                            startActivity(i);

                                        }
                                    }
                                    if (temp){
                                        Context context = getApplicationContext();
                                        CharSequence text = "There is no Such a "+showSerchName.getText().toString()+"!";
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();
                                    }

                                }
                            }
                        });



            }
        });
    }
}