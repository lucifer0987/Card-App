package com.example.splitmonk.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User {
    private String email, name, uid, updated_profile, upi;
    private long monk_points = 0;
    private boolean isSelected = false;
    ArrayList<String> groups = new ArrayList<>();

    public User(String email, String name, String uid, String updated_profile, String upi, ArrayList<String> groups) {
        this.email = email;
        this.name = name;
        this.uid = uid;
        this.updated_profile = updated_profile;
        this.upi = upi;
        this.groups = groups;
    }

    public User(String email, String name, String uid, String updated_profile, String upi, long monk_points, ArrayList<String> groups) {
        this.email = email;
        this.name = name;
        this.uid = uid;
        this.updated_profile = updated_profile;
        this.upi = upi;
        this.monk_points = monk_points;
        this.groups = groups;
    }

    public User(String email, String name, String uid, String updated_profile, String upi) {
        this.email = email;
        this.name = name;
        this.uid = uid;
        this.updated_profile = updated_profile;
        this.upi = upi;
    }

    public User() {
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public long getMonk_points() {
        return monk_points;
    }

    public void setMonk_points(long monk_points) {
        this.monk_points = monk_points;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUpdated_profile() {
        return updated_profile;
    }

    public void setUpdated_profile(String updated_profile) {
        this.updated_profile = updated_profile;
    }

    public String getUpi() {
        return upi;
    }

    public void setUpi(String upi) {
        this.upi = upi;
    }
}
