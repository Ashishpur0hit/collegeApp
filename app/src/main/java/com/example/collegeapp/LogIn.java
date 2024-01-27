package com.example.collegeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.collegeapp.databinding.ActivityLogInBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {

    private ActivityLogInBinding binding;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();



        //----------------------------- avoid user to ReLogin-------------------------------------------------------------------//
        if(user!=null)
        {
            Intent it = new Intent(LogIn.this,MainActivity.class);

            startActivity(it);
        }

        //------------------------setting on click on login btn --------------------------------------------------------------//

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInUser();
            }
            //-----------------------------------implementing LogINUser meathod--------------------------------------------------------//

            private void LogInUser() {
                String email = String.valueOf(binding.etEmail.getText());
                String Password = String.valueOf(binding.etPassword.getText());

                if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    if(!Password.isEmpty())
                    {
                        auth.signInWithEmailAndPassword(email,Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(LogIn.this, "LogIn Successfull", Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(LogIn.this,MainActivity.class);
                                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(it);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LogIn.this, "LogIn Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else binding.etPassword.setError("empty feilds are not allowed");
                }
                else if(email.isEmpty())
                {
                    binding.etEmail.setError("Empty Feilds are not allowed");
                }
                else binding.etEmail.setError("Enter valid Email");
            }
        });




        //----------------------------setting on clivck on forgot password---------------------------------------------------//

        binding.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LogIn.this , ForgorPassword.class);
                startActivity(it);
            }
        });


        //----------------------------setting onclick on already a user ------------------------------------------------------//
        binding.tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LogIn.this , SignUp.class);
                startActivity(it);
            }
        });
    }
}