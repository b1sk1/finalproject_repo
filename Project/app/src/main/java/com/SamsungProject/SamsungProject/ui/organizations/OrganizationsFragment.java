package com.SamsungProject.SamsungProject.ui.organizations;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.SamsungProject.SamsungProject.Database;
import com.SamsungProject.SamsungProject.OrganizationParameters;
import com.SamsungProject.SamsungProject.R;
import com.SamsungProject.SamsungProject.ThreeLevelListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class OrganizationsFragment extends Fragment {


    private OrganizationsViewModel mViewModel;

    public static OrganizationsFragment newInstance() {
        return new OrganizationsFragment();
    }
    ExpandableListView expandableListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.organizations_fragment, container, false);
        Database base = new Database();
        base.readOrganizations(new Database.OrganizationsCallback() {
            @Override
            public void onCallback(ArrayList<OrganizationParameters> organizationsList, ArrayList<String> organizationsNames) {
                expandableListView = (ExpandableListView) view.findViewById(R.id.lvExp);
                String[] parent = new String[] {"Организаторы турниров", "Команды"};
                ArrayList<String> competitionsorgs = new ArrayList<String>();
                ArrayList<String> teams = new ArrayList<String>();
                for(OrganizationParameters organization : organizationsList){
                    if (organization.getType().equals("Teams")){
                        teams.add(organization.getName());
                    }
                    else{
                        competitionsorgs.add(organization.getName());
                    }
                }

                String[] competitionsArray = Arrays.copyOf(competitionsorgs.toArray(), competitionsorgs.toArray().length, String[].class);
                String[] teamsArray = Arrays.copyOf(teams.toArray(), teams.toArray().length, String[].class);
                LinkedHashMap<String, String[]> thirdLevelq1 = new LinkedHashMap<>();
                LinkedHashMap<String, String[]> thirdLevelq2 = new LinkedHashMap<>();
                String[] strarr1 = new String[]{"hello!", "hey!"};
                List<String[]> secondLevel = new ArrayList<>();
                List<LinkedHashMap<String, String[]>> data = new ArrayList<>();
                secondLevel.add(competitionsArray);
                secondLevel.add(teamsArray);

                int countAll = 0;
                int countComp = 0;
                int countTeams = 0;
                for(OrganizationParameters organization : organizationsList){
                    if (organization.getType().equals("Competitions")){
                        String[] competitionsFillArray = new String[2];
                        competitionsFillArray[0] = organizationsList.get(countAll).getDescription();
                        competitionsFillArray[1] = "Сайт: " + organizationsList.get(countAll).getWebsite();

                      thirdLevelq1.put(competitionsArray[countComp], competitionsFillArray);
                      countComp += 1;
                      countAll += 1;
                    }
                    else{
                        String[] teamsFillArray = new String[4];
                        teamsFillArray[0] =  organizationsList.get(countAll).getDescription();
                        teamsFillArray[1] =  "Игроки: " + organizationsList.get(countAll).getPlayers();
                        teamsFillArray[2] =  "Экс-игроки: " + organizationsList.get(countAll).getEx_players();
                        teamsFillArray[3] =  "Сайт: " + organizationsList.get(countAll).getWebsite();
                        thirdLevelq2.put(teamsArray[countTeams], teamsFillArray);
                        countTeams += 1;
                        countAll += 1;
                    }
                }
                data.add(thirdLevelq1);
                data.add(thirdLevelq2);

                ThreeLevelListAdapter threeLevelListAdapterAdapter = new ThreeLevelListAdapter(getActivity(), parent, secondLevel, data);
                expandableListView.setAdapter(threeLevelListAdapterAdapter);
                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    int previousGroup = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (groupPosition != previousGroup)
                            //expandableListView.collapseGroup(previousGroup);
                        previousGroup = groupPosition;
                    }
                });


            }



        });
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OrganizationsViewModel.class);
        // TODO: Use the ViewModel
    }

}
