package com.example.collegeapp;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2 , SearchedCollegeName;
    private LinearLayout ToHide;
    private CardView Search;
    private RecyclerView SearchedPost;
    private EditText SearchCollege;
    private PostAdapter SearchedPostAdapter;
    private DatabaseReference RelatedPost;
    private static  boolean foundPost = false;
    private ValueEventListener searchListener;
    private ArrayList<PostModel>list;
    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View rootview =  inflater.inflate(R.layout.fragment_search, container, false);
        ToHide = rootview.findViewById(R.id.ToHide);
        SearchedPost = rootview.findViewById(R.id.SearchedPosts);
        Search = rootview.findViewById(R.id.btnSearch);
        SearchCollege = rootview.findViewById(R.id.etSeachCollege);
        SearchedPost = rootview.findViewById(R.id.SearchedPosts);
        list = new ArrayList<>();
        SearchedPost.setLayoutManager(new LinearLayoutManager(rootview.getContext()));
        SearchedPostAdapter = new PostAdapter(rootview.getContext() , list);
        SearchedPost.setAdapter(SearchedPostAdapter);
        RelatedPost = FirebaseDatabase.getInstance().getReference().child("Posts");


        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!SearchCollege.getText().toString().isEmpty())
                {
                    foundPost=false;
                    ToHide.setVisibility(View.INVISIBLE);
                    SearchedPost.setVisibility(View.VISIBLE);


                     RelatedPost.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            SearchedCollegeName = SearchCollege.getText().toString();

                            for(DataSnapshot data : snapshot.getChildren())
                            {




                                PostModel modelimg = new PostModel(data.child("PostImage").getValue(String.class) , data.child("Caption").getValue(String.class) , data.child("Profile").getValue(String.class) , data.child("Name").getValue(String.class),data.child("Institue").getValue(String.class),Integer.valueOf(Objects.requireNonNull(data.child("LikeCount").getValue(String.class))));
                                if (modelimg.Institute.equals(SearchedCollegeName)) {
                                    foundPost = true;
                                    list.add(0, modelimg);
                                }





                            }
                            if (!foundPost)
                            {
                                Toast.makeText(rootview.getContext(), "No Result found", Toast.LENGTH_SHORT).show();

                            }

                            SearchedPostAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else
                {
                    SearchCollege.setError("You haven't searched anything ");
                }
            }
        });


        




        return rootview;
    }
}