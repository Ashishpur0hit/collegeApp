package com.example.collegeapp;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CardView LogOut;
    private  String UID;
    private FirebaseAuth auth;
    private FirebaseDatabase DB;
    private DatabaseReference curr_user;

    private TextView name , SeeProfile;
    private ImageView Profile;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_settings, container, false);
        DB = FirebaseDatabase.getInstance();
        curr_user = DB.getReference().child("Users");
        auth = FirebaseAuth.getInstance();
        UID = auth.getUid();
        name = rootview.findViewById(R.id.TvUserName);
        Profile = rootview.findViewById(R.id.IvProfile);
        SeeProfile = rootview.findViewById(R.id.SeeProfileText);




        //--------------------------------------------- setting all onclicks to Profile page ---------------------------------------------------------------------//

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(rootview.getContext() , profile.class);
                startActivity(it);
            }
        });


        SeeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(rootview.getContext() , profile.class);
                startActivity(it);
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(rootview.getContext() , profile.class);
                startActivity(it);
            }
        });




        //-------------------------------- viewing UserName and profile-----------------------------------------------------------------------------------------//

        curr_user.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String UserName = String.valueOf(snapshot.child("UserName").getValue());
                    String DescProfile = String.valueOf(snapshot.child("Profile").getValue());

                    name.setText(UserName);
                    if(!DescProfile.isEmpty()){
                        Glide.with(rootview.getContext()).load(DescProfile).into(Profile);
                    }
                    else Toast.makeText(rootview.getContext(), "No profile in Database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        //----------------------setting on click on log out -------------------------------------------------------------//
        LogOut = rootview.findViewById(R.id.btnLogOut);

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(rootview.getContext(), "Logging Out", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(rootview.getContext() , SignUp.class);
                startActivity(it);
            }
        });
        return rootview;





    }
}