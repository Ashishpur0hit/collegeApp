package com.example.collegeapp;

import android.content.Context;
import android.content.SharedPreferences;

public class PostModel {
    String PostImage , Caption, Profile , Name , Institute ;
    Integer LikeCount ;
    Boolean isLiked = false;
    private static final String PREF_KEY_IS_LIKED = "pref_key_is_liked";

    public PostModel()
    {

    }

    public PostModel(String postImage, String caption, String profile, String name, String Institute,Integer LikeCount) {
        PostImage = postImage;
        Caption = caption;
        Profile = profile;
        Name = name;
        this.Institute = Institute;
        this.LikeCount = LikeCount;
    }

    public boolean getisLiked() {
        return isLiked;
    }

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getInstitute() {
        return Institute;
    }

    public void setInstitute(String institute) {
        Institute = institute;
    }


    public  void setLikeCount(Integer LikeCount)
    {
        this.LikeCount = LikeCount;
    }

    public Integer getLikeCount(){return LikeCount;}

    public String createShareMessage() {
        // Create a share message based on the post content
        return "Check out this post: Made by "+this.Name+"\n caption = "+this.Caption+"\n About : "+this.Institute+"\nPostImage link : "+this.PostImage+"\n Download College App Now" ;
    }




    // Save the isLiked state to SharedPreferences
    public void saveToPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("PostPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_KEY_IS_LIKED, isLiked);
        editor.apply();
    }

    // Retrieve the isLiked state from SharedPreferences
    public void loadFromPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("PostPreferences", Context.MODE_PRIVATE);
        isLiked = preferences.getBoolean(PREF_KEY_IS_LIKED, false);
    }
}
