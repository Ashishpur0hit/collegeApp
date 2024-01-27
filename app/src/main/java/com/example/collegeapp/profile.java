package com.example.collegeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.collegeapp.databinding.ActivityProfileBinding;
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

public class profile extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private final int req_code = 0;
    private Uri ImageUri;

    private FirebaseStorage firebaseStorage;
    private StorageReference reference;
    private String UID;


    private DatabaseReference curr_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        firebaseStorage = FirebaseStorage.getInstance();
        reference = firebaseStorage.getReference();
        binding.progreassBar.setVisibility(View.INVISIBLE);
        UID = FirebaseAuth.getInstance().getUid();
        curr_user = FirebaseDatabase.getInstance().getReference().child("Users");
        Context context = this;



        //-------------------------------- viewing UserName and profile-----------------------------------------------------------------------------------------//

        curr_user.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String UserName = String.valueOf(snapshot.child("UserName").getValue());
                    String DescProfile = String.valueOf(snapshot.child("Profile").getValue());
                    String Institution = String.valueOf(snapshot.child("Institute").getValue());

                    binding.TvUserName.setText(UserName);
                    binding.SeeProfileText.setText(Institution);
                    if(!DescProfile.isEmpty()){
                        Glide.with(context).load(DescProfile).into(binding.DescProfile);
                    }
                    else Toast.makeText(context, "No profile in Database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        //-------------------------setting on lcick on upload ------------------------------------------------------------------//

        binding.UploadProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ImageUri!=null)
                {
                    UploadToStorage(ImageUri);
                }
                else Toast.makeText(profile.this, "Please select an Image", Toast.LENGTH_SHORT).show();
            }

            private void UploadToStorage(Uri uri) {

                StorageReference fileRef = reference.child(System.currentTimeMillis()+"."+getFileExtention(uri));
                fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String profile = uri.toString();
                                curr_user.child(UID).child("Profile").setValue(profile);
                                binding.progreassBar.setVisibility(View.INVISIBLE);
                                Glide.with(context).load(profile).into(binding.DescProfile);

                                Toast.makeText(profile.this, "Image Uploaded", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        binding.progreassBar.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


            }

            private String getFileExtention(Uri uri) {
                ContentResolver cr  = getContentResolver();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                return mime.getExtensionFromMimeType(cr.getType(uri));

            }
        });

            //--------------------------------------- opening Gallery ----------------------------------------------------------------------------------//

        binding.EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery , req_code);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && data!=null && requestCode==req_code)
        {
            ImageUri= data.getData();
            binding.DescProfile.setImageURI(ImageUri);
        }
    }
}