package com.example.collegeapp;

import static android.companion.CompanionDeviceManager.RESULT_OK;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MakePostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakePostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String UID,DescProfile , UserName;

    private FirebaseDatabase DB;
    private EditText caption ,InstitueName;
    private Uri ImageUri;
    private DatabaseReference root ,curr_user;
    private FirebaseStorage storage;
    private StorageReference imgUpload;
    private ImageView make_post_profile , demo_profile , imagepost;
    private Context context ;
    private TextView make_post_name , Demo_name;

    private String ImagePostpath;
    private HashMap<String , String>PostMap;
    private ProgressBar post_bar;
    private CardView upload;
    private final int req_code = 0;

    public MakePostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MakePostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MakePostFragment newInstance(String param1, String param2) {
        MakePostFragment fragment = new MakePostFragment();
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
        View rootview =  inflater.inflate(R.layout.fragment_make_post, container, false);
        UID = FirebaseAuth.getInstance().getUid();
        DB = FirebaseDatabase.getInstance();
        curr_user = DB.getReference().child("Users");
        make_post_name = rootview.findViewById(R.id.MakePostUserName);
        Demo_name = rootview.findViewById(R.id.DemoPostUserName);
        make_post_profile = rootview.findViewById(R.id.MakePostProfile);
        demo_profile = rootview.findViewById(R.id.DemoPostCardProfile);
        imagepost = rootview.findViewById(R.id.Post);
        context = rootview.getContext();
        post_bar = rootview.findViewById(R.id.PostProgressBar);
        root = DB.getReference().child("Posts");
        upload = rootview.findViewById(R.id.btnUpload);
        post_bar.setVisibility(View.INVISIBLE);
        storage = FirebaseStorage.getInstance();
        imgUpload = storage.getReference();
        caption = rootview.findViewById(R.id.etCaption);
        PostMap = new HashMap<>();
        InstitueName = rootview.findViewById(R.id.name_Institue);

        //---------------------------setting upload b tn-------------------------------------------------------------------//

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ImageUri!=null)
                {
                    UploadToStorage(ImageUri);
                }
                else Toast.makeText(rootview.getContext(), "Please select an Image", Toast.LENGTH_SHORT).show();
            }

            private void UploadToStorage(Uri imageUri) {
                StorageReference fileRef = imgUpload.child(System.currentTimeMillis()+"."+getFileExtention(imageUri));
                fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                ImagePostpath = uri.toString();
                                PostMap.put("Name" , Demo_name.getText().toString());
                                PostMap.put("LikeCount",String.valueOf(0));
                                if(!caption.getText().toString().isEmpty())
                                {
                                    String Caption = caption.getText().toString();
                                    PostMap.put("Caption", Caption);
                                }
                                else
                                {
                                    caption.setError("Fill follwing Details");
                                }
                                if(!InstitueName.getText().toString().isEmpty())
                                {
                                    String InstName = InstitueName.getText().toString();
                                    PostMap.put("Institue" , InstName);
                                }
                                else InstitueName.setError("Fill follwing Details");

                                if(!DescProfile.isEmpty()) PostMap.put("Profile" , DescProfile);

                                PostMap.put("PostImage",ImagePostpath);

                                PostMap.put("UID",UID);

                                root.push().setValue(PostMap);
                                Toast.makeText(rootview.getContext(), "Post Uploaded ON Database", Toast.LENGTH_SHORT).show();
                                post_bar.setVisibility(View.INVISIBLE);


                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        post_bar.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }


            private String getFileExtention(Uri uri) {
                ContentResolver cr  = rootview.getContext().getContentResolver();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                return mime.getExtensionFromMimeType(cr.getType(uri));
            }
        });

        //--------------------------------gallery intent----------------------------------------------------------------------------//

        imagepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(gallery , req_code);
            }
        });



        //-------------------------------- viewing UserName and profile-----------------------------------------------------------------------------------------//

        curr_user.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                     UserName = String.valueOf(snapshot.child("UserName").getValue());
                     DescProfile = String.valueOf(snapshot.child("Profile").getValue());

                    make_post_name.setText(UserName);
                    Demo_name.setText(UserName);
                    if(!DescProfile.isEmpty()){
                        Glide.with(rootview.getContext()).load(DescProfile).into(make_post_profile);
                        Glide.with(rootview.getContext()).load(DescProfile).into(demo_profile);
                    }
                    else Toast.makeText(rootview.getContext(), "No profile in Database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return rootview;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && data!=null && requestCode==req_code)
        {
            ImageUri= data.getData();
            Glide.with(context).load(ImageUri).into(imagepost);
        }
    }
}