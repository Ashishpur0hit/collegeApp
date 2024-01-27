package com.example.collegeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.collegeapp.databinding.ActivityLogInBinding;
import com.example.collegeapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {


    private ActivitySignUpBinding binding;
    private FirebaseAuth auth;

    private FirebaseDatabase DB;
    private String UID;
    private DatabaseReference User_ref , curr_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        DB = FirebaseDatabase.getInstance();


        //------------------------ setting on click on sign In btn------------------------------------------------------------//

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUser();
            }

            private void CreateUser() {

                String UserName = binding.etUserName.getText().toString();
                String Email = binding.etRegisterEmail.getText().toString();
                String Password = binding.etRegisterPassword.getText().toString();
                String Instituion = binding.etInstitute.getText().toString();


                if(!Email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(Email).matches() && !UserName.isEmpty() && !Instituion.isEmpty())
                {
                    if(!Password.isEmpty())
                    {
                        auth.createUserWithEmailAndPassword(Email , Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(SignUp.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                                    User_ref = DB.getReference().child("Users");
                                    UID = auth.getCurrentUser().getUid();
                                    curr_user = User_ref.child(UID);
                                    StoreData();
                                    Intent it = new Intent(SignUp.this , LogIn.class);
                                    startActivity(it);

                                }
                                else
                                {
                                    Toast.makeText(SignUp.this, "registration Failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                            private void StoreData() {
                                String Profile = "";

                                HashMap<String , String> UserMap = new HashMap<>();

                                UserMap.put("UserName" , UserName);
                                UserMap.put("Email" , Email);
                                UserMap.put("Password" , Password);
                                UserMap.put("Institute" , Instituion);
                                UserMap.put("Profile" , Profile);

                                curr_user.setValue(UserMap);
                                binding.etUserName.setText("");
                                binding.etRegisterEmail.setText("");
                                binding.etInstitute.setText("");
                                binding.etRegisterPassword.setText("");

                            }
                        });
                    }
                    else binding.etRegisterPassword.setError("Empty feilds are not allowed");
                }
                else if(Email.isEmpty())
                {
                    binding.etRegisterEmail.setError("Empty feilds are not allowed");
                }
                else if(UserName.isEmpty())
                {
                    binding.etUserName.setError("Empty feilds are not allowed");
                }
                else if(Instituion.isEmpty())
                {
                    binding.etInstitute.setError("Empty feilds are not allowed");
                }
                else {
                    binding.etRegisterEmail.setError("Enter a valid Email");
                }

            }
        });


        //------------------------ setting onclick on Already user----------------------------------------------------------//

        binding.tvAlreadyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SignUp.this , LogIn.class);
                startActivity(it);
            }
        });

    }



    }
