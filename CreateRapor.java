package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CreateRapor extends AppCompatActivity {
    EditText email;
    Button send;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rapor);
        email=findViewById(R.id.editTextTextPersonName6);
        send=findViewById(R.id.button19);
        back=findViewById(R.id.button20);




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateRapor.this, HomePage.class);
                startActivity(i);
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!TextUtils.isEmpty(email.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("reservs")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        String temp="Reservation List:";
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d("TAG", document.getId() + " => " + document.getData());
                                            String tempUsername = document.getString("ownerName");
                                            temp=temp+"\nOwnner:"+tempUsername;
                                            String seatNumber = document.getString("seatNumber");
                                            temp=temp+" Number:"+seatNumber;
                                            String showName = document.getString("showName");
                                            temp=temp+" Show:"+showName;
                                        }
                                        sendEmail(temp,email.getText().toString());
                                    }
                                }
                            });
                }
                else
                {
                    Context context = getApplicationContext();
                    CharSequence text = "Email is not valid!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }






            }
        });
    }

    @SuppressLint({"LongLogTag", "IntentReset"})
    protected void sendEmail(String text,String email) {
        Log.i("Send email", "");

        String[] TO = {email};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reservations list");
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CreateRapor.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}