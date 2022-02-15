package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ticketbooking.model.Show;
import com.example.ticketbooking.model.User;
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

public class CreateShow extends AppCompatActivity {
    Button back;
    Button create;
    EditText showName;
    EditText showDef;
    CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_show);
        back=findViewById(R.id.button13);
        create=findViewById(R.id.button15);
        showName=findViewById(R.id.editTextTextPersonName);
        showDef=findViewById(R.id.editTextTextPersonName2);
        calendarView=findViewById(R.id.calendarView);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateShow.this, ShowHomePage.class);
                startActivity(i);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();


                if (showName.getText().toString().length()==0){
                    Context context = getApplicationContext();
                    CharSequence text = "Show name is can not be empty!";
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
                                            if (tempUsername.equals(showName.getText().toString())){
                                                temp =true;
                                                Context context = getApplicationContext();
                                                CharSequence text = "Show name is already taken pls pick different one!";
                                                int duration = Toast.LENGTH_SHORT;
                                                Toast toast = Toast.makeText(context, text, duration);
                                                toast.show();
                                            }
                                        }

                                        if(temp){
                                            Context context = getApplicationContext();
                                            CharSequence text = "Show name is already taken pls pick different one!";
                                            int duration = Toast.LENGTH_SHORT;
                                            Toast toast = Toast.makeText(context, text, duration);
                                            toast.show();
                                        }
                                        else{
                                            //save show

                                            Map<String, Object> user = new HashMap<>();
                                            Show show1=new Show(showName.getText().toString(),calendarView.getDate(),showDef.getText().toString());
                                            user.put("showname", show1.showName);
                                            user.put("showdate", show1.showDate);
                                            user.put("def", show1.showDef);

                                            // Add a new document with a generated ID
                                            db.collection("shows")
                                                    .add(user)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                            Context context = getApplicationContext();
                                                            CharSequence text = "Show has saved succesfully for "+showName.getText().toString()+".";
                                                            int duration = Toast.LENGTH_SHORT;
                                                            Toast toast = Toast.makeText(context, text, duration);
                                                            toast.show();

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w( "Error adding document", e);
                                                        }
                                                    });
                                            Intent i = new Intent(CreateShow.this, ShowHomePage.class);
                                            startActivity(i);
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