package com.example.collegeapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView Profile;
    private FirebaseDatabase DB;
    private RecyclerView PostRecycler;
    private DatabaseReference curr_user , root;
    private ArrayList<PostModel> list;
    private PostAdapter adapter;
    private String UID;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View rootview =  inflater.inflate(R.layout.fragment_home, container, false);
        Profile = rootview.findViewById(R.id.profile_home);
        UID = FirebaseAuth.getInstance().getUid();
        DB = FirebaseDatabase.getInstance();
        curr_user = DB.getReference().child("Users");
        PostRecycler = rootview.findViewById(R.id.ReviewRecyclerView);
        PostRecycler.setLayoutManager(new LinearLayoutManager(rootview.getContext()));
        list = new ArrayList<>();
        adapter = new PostAdapter(rootview.getContext(),list);
        PostRecycler.setAdapter(adapter);
        root = DB.getReference().child("Posts");


        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot data : snapshot.getChildren())
                {
                    PostModel modelimg = new PostModel(data.child("PostImage").getValue(String.class) , data.child("Caption").getValue(String.class) , data.child("Profile").getValue(String.class) , data.child("Name").getValue(String.class),data.child("Institue").getValue(String.class),Integer.valueOf(Objects.requireNonNull(data.child("LikeCount").getValue(String.class))));
                    list.add(0,modelimg);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(rootview.getContext(), ""+error.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });











        //------------------------vewing profile------------------------------------------------------------------------------//


        curr_user.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String ProfileUri = String.valueOf(snapshot.child("Profile").getValue());
                    if(ProfileUri.isEmpty())
                    {
                        Toast.makeText(rootview.getContext(), "You haven't set up your profile yet .", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Glide.with(rootview.getContext()).load(ProfileUri).into(Profile);
                    }
                }
                else
                {
                    Toast.makeText(rootview.getContext(), "Profile Picture didn't load", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        return  rootview;
    }
}