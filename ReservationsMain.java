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
import com.example.ticketbooking.model.Reservation;
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

public class ReservationsMain extends AppCompatActivity {


    EditText list;
    EditText showChose;
    EditText seatNumber;
    Button save;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations_main);
        list=findViewById(R.id.editTextTextMultiLine2);
        showChose=findViewById(R.id.editTextTextPersonName5);
        seatNumber=findViewById(R.id.editTextNumber);
        save =findViewById(R.id.button18);
        back =findViewById(R.id.button17);


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
                Intent i = new Intent(ReservationsMain.this, HomePage.class);
                startActivity(i);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();


                if (showChose.getText().toString().length()==0){
                    Context context = getApplicationContext();
                    CharSequence text = "Show name can not be empty!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else if (seatNumber.getText().toString().length()==0){
                    Context context = getApplicationContext();
                    CharSequence text = "seatNumber can not be empty!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else{
                    db.collection("shows")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        boolean temp=false;
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d("TAG", document.getId() + " => " + document.getData());

                                            String tempUsername=document.getString("showname");
                                            if (tempUsername.equals(showChose.getText().toString())){
                                                temp =true;
                                            }
                                        }

                                        if (temp) {//show is valid

                                            db.collection("reservs")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                boolean temp = false;
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                                                    String seatNumber1 = document.getString("seatNumber");
                                                                    String showName = document.getString("showName");
                                                                    if (showName.equals(showChose.getText().toString()) && seatNumber1.equals(seatNumber.getText().toString())) {
                                                                        temp = true;

                                                                    }
                                                                }
                                                                if (temp) {
                                                                    Context context = getApplicationContext();
                                                                    CharSequence text = "Seat is taken for this seat, pls try again.";
                                                                    int duration = Toast.LENGTH_SHORT;
                                                                    Toast toast = Toast.makeText(context, text, duration);
                                                                    toast.show();
                                                                } else {
                                                                    Map<String, Object> user = new HashMap<>();
                                                                    Reservation res1 = new Reservation( showChose.getText().toString(),Globals.username, seatNumber.getText().toString());
                                                                    user.put("showName", res1.showName);
                                                                    user.put("seatNumber", res1.seatNumber);
                                                                    user.put("ownerName", res1.ownerName);

                                                                    // Add a new document with a generated ID
                                                                    db.collection("reservs")
                                                                            .add(user)
                                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                @Override
                                                                                public void onSuccess(DocumentReference documentReference) {
                                                                                    Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                                                    Context context = getApplicationContext();
                                                                                    CharSequence text = "Reservation has saved succesfully for " + showChose.getText().toString() + ".";
                                                                                    int duration = Toast.LENGTH_SHORT;
                                                                                    Toast toast = Toast.makeText(context, text, duration);
                                                                                    toast.show();
                                                                                    Intent i = new Intent(ReservationsMain.this, HomePage.class);
                                                                                    startActivity(i);
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Log.w("Error adding document", e);
                                                                                }
                                                                            });

                                                                }

                                                            } else {
                                                                Log.w("TAG", "Error getting documents.", task.getException());
                                                            }
                                                        }
                                                    });

                                        }



                                        else {
                                            Context context = getApplicationContext();
                                            CharSequence text = "Pelese enter valid show name!";
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
            }
        });




    }
}