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
import android.widget.TextView;
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

public class UpdateShow extends AppCompatActivity {
    Button back;
    Button update;
    TextView showLabel;
    EditText showDef;
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_show);
        back=findViewById(R.id.button16);
        update=findViewById(R.id.button14);
        showLabel=findViewById(R.id.textView5);
        showDef=findViewById(R.id.editTextTextPersonName4);
        showLabel.setText(Globals.selectedShow);
        showDef.setText(Globals.selectedShowDef);
        calendarView=findViewById(R.id.calendarView2);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UpdateShow.this, ShowHomePage.class);
                startActivity(i);
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();


                db.collection("shows")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("TAG", document.getId() + " => " + document.getData());

                                        String tempUsername=document.getString("showname");
                                        if (tempUsername.equals(showLabel.getText().toString())){


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


                                                                            Map<String, Object> user = new HashMap<>();
                                                                            Show show1=new Show(showLabel.getText().toString(),calendarView.getDate(),showDef.getText().toString());
                                                                            user.put("showname", show1.showName);
                                                                            user.put("showdate", show1.showDate);
                                                                            user.put("def", show1.showDef);




                                                                            db.collection("shows")
                                                                                    .add(user)
                                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                        @Override
                                                                                        public void onSuccess(DocumentReference documentReference) {
                                                                                            Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                                                            Context context = getApplicationContext();
                                                                                            CharSequence text = "Show has  updated sucsesfully "+showLabel.getText().toString()+".";
                                                                                            int duration = Toast.LENGTH_SHORT;
                                                                                            Toast toast = Toast.makeText(context, text, duration);
                                                                                            toast.show();
                                                                                            Intent i = new Intent(UpdateShow.this, ShowHomePage.class);
                                                                                            startActivity(i);
                                                                                        }
                                                                                    })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Log.w( "Error adding document", e);
                                                                                        }
                                                                                    });


                                                                        }
                                                                    });
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w("TAG", "We get some errors.", task.getException());
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
    }
}