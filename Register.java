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
import android.widget.RadioButton;
import android.widget.Toast;

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

public class Register extends AppCompatActivity {


    EditText username;
    EditText password1;
    EditText password2;
    RadioButton adminRadiobut;
    Button signButton;
    Button homePageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signButton = findViewById(R.id.button);
        homePageButton = findViewById(R.id.button2);

        username=findViewById(R.id.editTextTextEmailAddress2);
        password1=findViewById(R.id.editTextTextPassword2);
        password2=findViewById(R.id.editTextTextPassword3);

        adminRadiobut=findViewById(R.id.radioButton);

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, MainActivity.class);
                startActivity(i);
            }
        });
        homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (username.getText().toString().length()<4){
                    Context context = getApplicationContext();
                    CharSequence text = "User name must be larger than 4 character!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else if(!password1.getText().toString().equals(password2.getText().toString()) ){
                    Context context = getApplicationContext();
                    CharSequence text = "Passwords are not same pls consider it again!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else if(!(password1.getText().toString().length() >3))
                {
                    Context context = getApplicationContext();
                    CharSequence text = "Password needs to be longer than 4 character!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("users")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            boolean temp=false;
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d("TAG", document.getId() + " => " + document.getData());
                                                String tempUsername=document.getString("username");
                                                if (tempUsername.equals(username.getText().toString())){
                                                    temp =true;
                                                    Context context = getApplicationContext();
                                                    CharSequence text = "Username is already taken pls pick different one!";
                                                    int duration = Toast.LENGTH_SHORT;
                                                    Toast toast = Toast.makeText(context, text, duration);
                                                    toast.show();
                                                }
                                            }

                                            if(temp){
                                                Context context = getApplicationContext();
                                                CharSequence text = "Username is already taken pls pick different one!";
                                                int duration = Toast.LENGTH_SHORT;
                                                Toast toast = Toast.makeText(context, text, duration);
                                                toast.show();
                                            }
                                            else{
                                                //save user
                                                String status="normal";
                                                if (((RadioButton)adminRadiobut).isChecked()){
                                                    status="admin";
                                                }
                                                Map<String, Object> user = new HashMap<>();
                                                User user1=new User(username.getText().toString(),password1.getText().toString(),status);
                                                user.put("username", user1.username);
                                                user.put("password", user1.password);
                                                user.put("type", user1.type);

                                                // Add a new document with a generated ID
                                                db.collection("users")
                                                        .add(user)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                                Context context = getApplicationContext();
                                                                CharSequence text = "User is registred succesfully for "+username.getText().toString()+".";
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
                                                Intent i = new Intent(Register.this, MainActivity.class);
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