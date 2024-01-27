package com.example.collegeapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {


    private Context context;
    private ArrayList<PostModel> list;







    public PostAdapter (Context context , ArrayList<PostModel>list)
    {

        this.context = context;
        this.list = list;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.postcard , parent , false);
        ViewHolder postview = new ViewHolder(v);
        return postview;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

         list.get(position).loadFromPreferences(context);


        holder.Caption.setText(list.get(position).Caption);
        Glide.with(context).load(list.get(position).getPostImage()).into(holder.Image);
        holder.Name.setText(list.get(position).Name);
        Glide.with(context).load(list.get(position).getProfile()).into(holder.Profile);
        holder.Institute.setText(list.get(position).Institute);
        holder.number.setText(String.valueOf(list.get(position).LikeCount));

        int temp = position;

        if(list.get(temp).isLiked)
        {
            holder.Like.setImageResource(R.drawable.filledlike);
        }
        else holder.Like.setImageResource(R.drawable.like);



        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a share intent
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");

                // Set the share message
                String shareMessage = list.get(temp).createShareMessage();
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

                // Start the chooser to let the user pick a sharing app
                Intent chooserIntent = Intent.createChooser(shareIntent, "Share Post");
                context.startActivity(chooserIntent);
            }
        });



        holder.Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.get(temp).isLiked)
                {
                    Toast.makeText(context, "Retrive", Toast.LENGTH_SHORT).show();
                    //retrive;
                    holder.Like.setImageResource(R.drawable.like);
                    //likecount decrement
                    list.get(temp).isLiked=false;
                    list.get(temp).LikeCount--;


                }
                else
                {
                    //like
                    Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show();
                    holder.Like.setImageResource(R.drawable.filledlike);
                    list.get(temp).isLiked=true;
                    list.get(temp).LikeCount++;
                }
                holder.number.setText(String.valueOf(list.get(temp).LikeCount));
                list.get(temp).saveToPreferences(context);
            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder
    {

        TextView Caption, Name,Institute,number;
        ImageView Image , Profile,Like,Heart,share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Caption = itemView.findViewById(R.id.caption);
            Image = itemView.findViewById(R.id.UploadedImage);
            Profile  = itemView.findViewById(R.id.ProfilePic);
            Name = itemView.findViewById(R.id.UserName) ;
            Institute = itemView.findViewById(R.id.Institute);
            Like = itemView.findViewById(R.id.like);
            Heart=itemView.findViewById(R.id.filledlike);
            number = itemView.findViewById(R.id.comment);
            share = itemView.findViewById(R.id.share);
        }
    }
}
