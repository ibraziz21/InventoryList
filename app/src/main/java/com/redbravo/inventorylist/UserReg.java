package com.redbravo.inventorylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserReg extends AppCompatActivity {
FirebaseAuth mAuth;
EditText email;
EditText pass;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg);
    mAuth=FirebaseAuth.getInstance();
    email= findViewById(R.id.uemail);
    pass = findViewById(R.id.upass);
    }

    public void logInActivity(View view) {
    Intent intent =new Intent(getApplicationContext(), loginactivity.class);
        startActivity(intent);
        finish();
}

    public void Register(View view) {
    String uemail = email.getText().toString();
    String upass = pass.getText().toString();
    mAuth.createUserWithEmailAndPassword(uemail,upass)
            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(getApplicationContext(), "Successfuly created account", Toast.LENGTH_SHORT);
                    Intent intent =new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed to create Account: "+e.getMessage(), Toast.LENGTH_SHORT);
                }
            });
    }
}