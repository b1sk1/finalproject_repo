package com.SamsungProject.SamsungProject.ui.tournaments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.SamsungProject.SamsungProject.Database;
import com.SamsungProject.SamsungProject.ExpandableListAdapter;
import com.SamsungProject.SamsungProject.PlayerParameters;
import com.SamsungProject.SamsungProject.R;
import com.SamsungProject.SamsungProject.RatingFIltersExpandableAdapter;
import com.SamsungProject.SamsungProject.RatingFiltersFragment;
import com.SamsungProject.SamsungProject.TournamentFiltersFragment;
import com.SamsungProject.SamsungProject.TournamentParameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.SamsungProject.SamsungProject.MainActivity.tournamentsList;

public class TournamentsFragment extends Fragment {

    private TournamentsViewModel mViewModel;

    public static TournamentsFragment newInstance() {
        return new TournamentsFragment();
    }
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tournaments_fragment, container, false);
        Database base = new Database();
        base.readTournaments(new Database.TournamentsCallback() {
            @Override
            public void onCallback(ArrayList<TournamentParameters> tournamentsList,
            ArrayList<String> tournamentsNames, HashMap<String, String> ratingAnswer,
            ArrayList<String> tournamentsAnswer, ArrayList<PlayerParameters> playersList) {
                listDataHeader = tournamentsNames;
                Collections.sort(listDataHeader);
                listDataChild = new HashMap<String, List<String>>();
                HashMap<String, ArrayList<String>> playerInfo = new HashMap<String, ArrayList<String>>();
                HashMap<String, Integer> rating = new HashMap<String, Integer>();
                for (TournamentParameters j : tournamentsList) {


                    ArrayList<String> infoList = new ArrayList<String>();
                    if (j.getType().equals("de")){
                        infoList.add("Тип: " + "Double Elimination");
                    }
                    else if (j.getType().equals("se3")){
                        infoList.add("Тип: " + "Single Elimination (матч за 3е место)");
                    }
                    else{
                        infoList.add("Тип: " + "Single Elimination");
                    }
                    infoList.add("Вес: " + j.getWeight());
                    for (String place : j.getPlaces().keySet()){
                        infoList.add(place + " место: " + j.getPlaces().get(place).toString()
                                .substring(1, j.getPlaces().get(place).toString().length() - 1)
                                + " — " + j.getPoints(place) * Integer.parseInt(j.getWeight()) + " очков");
                    }
                    Comparator<String> comp = new Compare();
                    Collections.sort(infoList.subList(2, infoList.size()), comp);
                    playerInfo.put(j.getName(), infoList);
                }


                for (int i = 0; i < listDataHeader.size(); i++){
                    listDataChild.put(listDataHeader.get(i), playerInfo.get(listDataHeader.get(i)));
                }
                ExpandableListView expListView = view.findViewById(R.id.lvExp);
                listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
                expListView.setAdapter(listAdapter);
                expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v,
                                                int groupPosition, long id) {
                        // Toast.makeText(getApplicationContext(),
                        // "Group Clicked " + listDataHeader.get(groupPosition),
                        // Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

            }
        });





        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TournamentsViewModel.class);
        // TODO: Use the ViewModel
    }

}

class Compare implements Comparator<String> {
    public int compare(String a1, String b1) {
        int place1;
        int place2;
        String[] a2 = a1.split("[ -]");
        String[] b2 = b1.split("[ -]");
        if (a2[0].length() == 1) {
            place1 = Integer.parseInt(a2[0]);
        } else {
            place1 = Integer.parseInt(a2[0].substring(0, 2));
        }
        if (b2[0].length() == 1) {
            place2 = Integer.parseInt(b2[0]);
        } else {
            place2 = Integer.parseInt(b2[0].substring(0, 2));
        }
        if (place1 > place2) {
            return 1;
        } else {
            return -1;
        }

    }
}