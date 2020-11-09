package com.SamsungProject.SamsungProject;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {
    ArrayList<String> tournamentsAnswer = new ArrayList<>();
    HashMap<String, String> ratingAnswer = new HashMap<>();
    private ArrayList<TournamentParameters> tournamentsList = new ArrayList<TournamentParameters>();
    private ArrayList<String> tournamentsNames = new ArrayList<String>();
    static private ArrayList<PlayerParameters> playersList = new ArrayList<PlayerParameters>();
    private ArrayList<String> playersNames = new ArrayList<String>();
    private ArrayList<OrganizationParameters> organizationsList = new ArrayList<>();
    private ArrayList<String> organizationsNames = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference playersReference = database.getReference("Players");
    private DatabaseReference tournamentsReference = database.getReference("Tournaments");
    private DatabaseReference organizationsReference = database.getReference("Organizations");
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    public void readBoth(final OrgAndTournCallback orgAndTournCallback){
        ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //Node 1: This let's you get data from the first node
                Log.d("checkdatman",dataSnapshot.child("Tournaments").getValue().toString());

                //Node 2: This let's you get data from the second node
                Log.d("checkdatman",dataSnapshot.child("Players").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readPlayers(final PlayersCallback playersCallback){ //читам информацию об игроках
        playersList.clear();
        playersReference.addValueEventListener(new ValueEventListener() {
            //@RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, HashMap<String, String>> map = (Map<String, HashMap<String, String>>) dataSnapshot.getValue();
                for (String key : map.keySet()) {
                    ArrayList<String> constructorList = new ArrayList<String>();
                    PlayerParameters playerParameters = new PlayerParameters(
                            key,
                            dataSnapshot.child(key).child("city").getValue().toString(),
                            dataSnapshot.child(key).child("date_of_birth").getValue().toString(),
                            dataSnapshot.child(key).child("sex").getValue().toString(),
                            dataSnapshot.child(key).child("name").getValue().toString()
                    );
                    playersList.add(playerParameters);
                }
                for (PlayerParameters i : playersList) {
                    playersNames.add(i.getName());

                }
                playersCallback.onCallback(playersList, playersNames);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void readTournaments(final TournamentsCallback tournamentsCallback){ //читаем информацию о турнирах,
        // заодно забираем значения с панели фильтров
        tournamentsReference.addValueEventListener(new ValueEventListener() {
            //@RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, HashMap<String, String>> map = (Map<String, HashMap<String, String>>) dataSnapshot.getValue();
                for (String key : map.keySet()) {
                    String name = key;
                    tournamentsNames.add(name);
                    String place = null;
                    String date = null;
                    String kind = null;
                    String mode = null;
                    String type = null;
                    String weight = null;
                    HashMap<Integer, String> places = new HashMap<Integer, String>();
                    for (String key2 : map.get(key).keySet()) {
                        if (key2.length() >= 1 && key2.length() <= 2){
                            places.put(Integer.parseInt(key2), map.get(key).get(key2));
                        }
                        else if(key2.equals("type")){
                            type = map.get(key).get(key2);
                        }
                        else if (key2.equals("weight")){
                            weight = map.get(key).get(key2);
                        }
                        else if (key2.equals("date")){
                            date = map.get(key).get(key2);
                        }
                        else if (key2.equals("kind")){
                            kind = map.get(key).get(key2);
                        }
                        else if (key2.equals("mode")){
                            mode = map.get(key).get(key2);
                        }
                        else if(key2.equals("place")){
                            place = map.get(key).get(key2);
                        }
                    }
                    for(Integer i : places.keySet()){
                        places.put(i, places.get(i).replace("-country", ""));

                    }


                    TournamentParameters tournamentParameters = new TournamentParameters(place, date, kind, mode, name, type, weight, places);
                    tournamentsList.add(tournamentParameters);
                }
                RatingFIltersExpandableAdapter ratingAdapter = new RatingFIltersExpandableAdapter();
                ratingAdapter.getAnswer(new RatingFIltersExpandableAdapter.DataCallback() {
                    @Override
                    public void onCallback(HashMap<String, String> result) {
                        ratingAnswer = result;
                    }
                });
                TournamentFiltersFragmentAdapter tournamentsAdapter  = new TournamentFiltersFragmentAdapter();
                tournamentsAdapter.getTournamentAnswer(new TournamentFiltersFragmentAdapter.DataCallback() {
                    @Override
                    public void onCallback(ArrayList<String> tournamentsResult) {
                        tournamentsAnswer = tournamentsResult;
                        tournamentsCallback.onCallback(tournamentsList, tournamentsNames, ratingAnswer, tournamentsAnswer, playersList);
                    }
                });





            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void readOrganizations(final OrganizationsCallback organizationsCallback){ //читаем информацию об организациях
        organizationsReference.addValueEventListener(new ValueEventListener() {
            //@RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, HashMap<String, String>> map = (Map<String, HashMap<String, String>>) dataSnapshot.getValue();
                for (String key : map.keySet()) {
                    if (dataSnapshot.child(key).child("Players").exists()){
                        OrganizationParameters organizationParameters = new OrganizationParameters(
                                key,
                                dataSnapshot.child(key).child("Description").getValue().toString(),
                                dataSnapshot.child(key).child("Players").getValue().toString(),
                                dataSnapshot.child(key).child("Type").getValue().toString(),
                                dataSnapshot.child(key).child("Website").getValue().toString(),
                                dataSnapshot.child(key).child("ex-Players").getValue().toString()
                        );
                        organizationsList.add(organizationParameters);
                    } else {
                        OrganizationParameters organizationParameters = new OrganizationParameters(
                                key,
                                dataSnapshot.child(key).child("Description").getValue().toString(),
                                dataSnapshot.child(key).child("Type").getValue().toString(),
                                dataSnapshot.child(key).child("Website").getValue().toString()

                        );
                        organizationsList.add(organizationParameters);

                    }
                }
                for (OrganizationParameters i : organizationsList) {
                    organizationsNames.add(i.getName());

                }
                int acheck = 5;
                organizationsCallback.onCallback(organizationsList, organizationsNames);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    //коллбеки для передачи данных в другие классы
    public interface OrgAndTournCallback{
        void onCallback(ArrayList<TournamentParameters> tournamentsList, ArrayList<String> tournamentsNames,
                        ArrayList<OrganizationParameters> organizationsList, ArrayList<String> organizationsNames);
    }
    public interface PlayersCallback {
        void onCallback(ArrayList<PlayerParameters> playersList, ArrayList<String> playersNames);
    }
    public interface TournamentsCallback {
        void onCallback(ArrayList<TournamentParameters> tournamentsList,
                        ArrayList<String> tournamentsNames, HashMap<String, String> ratingAnswer,
                        ArrayList<String> tournamentsAnswer, ArrayList<PlayerParameters> playersList);
    }
    public interface OrganizationsCallback{
        void onCallback(ArrayList<OrganizationParameters> organizationsList, ArrayList<String> organizationsNames);

    }





}

