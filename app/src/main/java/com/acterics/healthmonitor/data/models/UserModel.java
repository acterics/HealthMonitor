package com.acterics.healthmonitor.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oleg on 16.05.17.
 */

public class UserModel {

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("height")
    private int height;

    @SerializedName("weight")
    private int weight;

    /**
     * yyyy-MM-dd
     */
    @SerializedName("birthDate")
    private String date;

    @SerializedName("city")
    private String city;

    @SerializedName("county")
    private String country;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
