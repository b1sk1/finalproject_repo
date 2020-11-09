package com.SamsungProject.SamsungProject;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerRating {
    int position;
    String name;
    int rating;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


    public PlayerRating(String name, int rating){
        this.name = name;
        this.rating = rating;
    }
    public PlayerRating(String name, int rating, int position){
        this.name = name;
        this.rating = rating;
        this.position = position;
    }

}