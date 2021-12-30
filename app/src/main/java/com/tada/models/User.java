package com.tada.models;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class User  {

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    public String userId;

    @SerializedName("name")
    public String name;

    @SerializedName("first_name")
    public String firstName;

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("img_url")
    public String avatar;

    @SerializedName("email")
    public String email;

    @SerializedName("phone")
    public String number;


}
