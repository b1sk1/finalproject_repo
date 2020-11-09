package com.SamsungProject.SamsungProject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button click;
    MainActivity activity = this;
    public static int blabla = 5;
    public static ArrayList<PlayerParameters> playersList = new ArrayList<PlayerParameters>();
    public static ArrayList<String> playersNames = new ArrayList<String>();
    public static ArrayList<TournamentParameters> tournamentsList;
    public static ArrayList<String> tournamentsNames;
    public static ArrayList<OrganizationParameters> organizationsList;
    public static ArrayList<String> organizationsNames;

    public void goTo2ndActivity(){
        Intent intent = new Intent(this, NavigationDrawerActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        playersNames.add("fuckdatshitman");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        click = (Button)findViewById(R.id.button);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Database base = new Database();
                base.readBoth(new Database.OrgAndTournCallback() {
                    @Override
                    public void onCallback(ArrayList<TournamentParameters> tournamentsList, ArrayList<String> tournamentsNames, ArrayList<OrganizationParameters> organizationsList, ArrayList<String> organizationsNames) {

                    }
                });

                activity.goTo2ndActivity();

            }
        });
    }
}