package com.example.collegeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.collegeapp.databinding.ActivityForgorPasswordBinding;

import java.util.Objects;

public class ForgorPassword extends AppCompatActivity {

    private ActivityForgorPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgorPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        //-------------------------------setting onclick on back --------------------------------------------------------------------//
        binding.tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ForgorPassword.this , LogIn.class);
                startActivity(it);
            }
        });


        //----------------------------setting on click on remember password---------------------------------------------//
        binding.tvRemeberPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ForgorPassword.this , LogIn.class);
                startActivity(it);
            }
        });
    }

    }
