package com.SamsungProject.SamsungProject;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.time.Period;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.ChronoUnit;


import org.threeten.bp.ZoneId;
import org.threeten.bp.temporal.TemporalAdjusters;

public class PlayerParameters {
    private int position;
    private String name;
    private int rating;
    private String nickname;
    private String city;
    private String age;
    private String sex;

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getNickname() {
        return nickname;
    }

    public String getCity() {
        return city;
    }

    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }

    PlayerParameters(String nickname, String city, String date_of_birth, String sex, String name){
        this.nickname = nickname;
        this.city = city;
        if(!date_of_birth.equals("-")){
            String[] date_of_birth_array = date_of_birth.split("\\.");
            LocalDate now = LocalDate.now(); //gets localDate
            LocalDate l = LocalDate.of(Integer.parseInt(date_of_birth_array[2]), Integer.parseInt(date_of_birth_array[1]),
                    Integer.parseInt(date_of_birth_array[0])); //specify year, month, date directly
            long diff = ChronoUnit.YEARS.between(l, now);
            this.age = Long.toString(diff);
        }
        else{
            this.age = "-";
        }
        this.sex = sex;
        this.name = name;
    }
    PlayerParameters(String nickname, String city, String date_of_birth, String sex, String name, int rating){
        this.nickname = nickname;
        this.city = city;
        if(!date_of_birth.equals("-")){
            String[] date_of_birth_array = date_of_birth.split("\\.");
            LocalDate now = LocalDate.now(); //gets localDate
            LocalDate l = LocalDate.of(Integer.parseInt(date_of_birth_array[2]), Integer.parseInt(date_of_birth_array[1]),
                    Integer.parseInt(date_of_birth_array[0])); //specify year, month, date directly
            long diff = ChronoUnit.YEARS.between(l, now);
            this.age = Long.toString(diff);
        }
        else{
            this.age = "-";
        }
        this.sex = sex;
        this.name = name;
        this.rating = rating;
    }
}
