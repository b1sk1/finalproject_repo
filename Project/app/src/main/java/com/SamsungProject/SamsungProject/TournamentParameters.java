package com.SamsungProject.SamsungProject;

import android.util.Log;

import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.ChronoUnit;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TournamentParameters {
    private String place;
    private String date;
    private String kind;
    private String mode;
    private String name;
    private HashMap<String, ArrayList<String>> places = new HashMap<String, ArrayList<String>>();
    private String type;
    private String weight;
    public HashMap<String, ArrayList<String>> getPlaces() {
        return places;
    }

    public String getKind() {
        return kind;
    }

    public String getMode() {
        return mode;
    }


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getWeight() {
        return weight;
    }

    public String getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public long getMonthNumber(){
        String[] date_of_birth_array = date.split("\\.");
        org.threeten.bp.LocalDate now = org.threeten.bp.LocalDate.now(); //gets localDate
        org.threeten.bp.LocalDate l = LocalDate.of(Integer.parseInt(date_of_birth_array[2]), Integer.parseInt(date_of_birth_array[1]),
                Integer.parseInt(date_of_birth_array[0])); //specify year, month, date directly
        long diff = ChronoUnit.MONTHS.between(l, now);
        return diff;
    }
    TournamentParameters(String place, String date, String kind, String mode, String name, String type, String weight, HashMap<Integer, String> places){
        this.date = date;
        this.kind = kind;
        this.mode = mode;
        this.name = name;
        this.type = type;
        this.weight = weight;
        //заполнение мест в зависимости от типа сетки турнира
        if (type.equals("de")){
            ArrayList<String> list56 = new ArrayList<String>();
            ArrayList<String> list78 = new ArrayList<String>();
            ArrayList<String> list912 = new ArrayList<String>();
            ArrayList<String> list1316 = new ArrayList<String>();
            for (Integer key : places.keySet()){
                if (key >= 1 && key <= 4){
                    ArrayList<String> list14 = new ArrayList<String>();
                    list14.add(places.get(key));
                    this.places.put(Integer.toString(key), list14);
                }
                else if (key >= 5 && key <= 6){
                    list56.add(places.get(key));
                    this.places.put("5-6", list56);

                }
                else if (key >= 7 && key <= 8){
                    list78.add(places.get(key));
                    this.places.put("7-8", list78);

                }
                else if (key >= 9 && key <= 12){
                    list912.add(places.get(key));
                    this.places.put("9-12", list912);

                }
                else{
                    list1316.add(places.get(key));
                    this.places.put("13-16", list1316);

                }
            }
        } else if (type.substring(0, 2).equals("se")) {
            ArrayList<String> list34 = new ArrayList<String>();
            ArrayList<String> list58 = new ArrayList<String>();
            ArrayList<String> list916 = new ArrayList<String>();
            for (Integer key : places.keySet()) {
                if (key >= 1 && key <= 4){
                    if (key == 1 || key == 2) {
                        ArrayList<String> list12 = new ArrayList<String>();
                        list12.add(places.get(key));
                        this.places.put(Integer.toString(key), list12);
                    }
                    else {
                        if (type.equals("se3")) {
                            if (key == 3 || key == 4) {
                                ArrayList<String> list3_4 = new ArrayList<String>();
                                list3_4.add(places.get(key));
                                this.places.put(Integer.toString(key), list3_4);
                            }
                        } else {
                            list34.add(places.get(key));
                            this.places.put("3-4", list34);
                        }
                    }
                }
                else if (key >= 5 && key <= 8){
                    list58.add(places.get(key));
                    this.places.put("5-8", list58);
                }
                else {
                    list916.add(places.get(key));
                    this.places.put("9-16", list916);
                }
            }
        }
    }
    public int getPoints(String place){
        if (place.equals("1")) return 400;
        else if (place.equals("2")) return 300;
        else if (place.equals("3")) return 250;
        else if (place.equals("3-4") || place.equals("4")) return 200;
        else if (place.equals("5-6")) return 150;
        else if (place.equals("5-8") || place.equals("7-8")) return 100;
        else if (place.equals("9-12")) return 75;
        else return 50;

    }
    public HashMap <String, Integer> getTournamentRating(){
        HashMap<ArrayList<String>, Integer> TournamentRating = new HashMap<ArrayList<String>, Integer>();
        HashMap<String, Integer> finalMap = new HashMap<String, Integer>();
        for (String place : places.keySet()){
            TournamentRating.put(places.get(place), getPoints(place) * Integer.parseInt(weight));
        }
        for(ArrayList<String> list : TournamentRating.keySet()){
            for(String i : list){
                finalMap.put(i, TournamentRating.get(list));
            }

        }
        return finalMap;
    }
}
