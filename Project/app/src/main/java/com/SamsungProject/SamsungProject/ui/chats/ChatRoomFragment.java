package com.SamsungProject.SamsungProject.ui.chats;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.SamsungProject.SamsungProject.Database;
import com.SamsungProject.SamsungProject.PlayerParameters;
import com.SamsungProject.SamsungProject.R;
import com.SamsungProject.SamsungProject.TournamentParameters;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChatRoomFragment extends Fragment implements Database.TournamentsCallback {
    ArrayList<String> items = new ArrayList<String>();
    private Database base = new Database();
    //public static ArrayList<String> tournamentNames = new ArrayList<String>();
    DatabaseReference reference;
    static HashMap<String, String> dataHashMap = new HashMap<String, String>();
    ArrayList<String> arrayList;
    EditText e1;
    ListView l1;
    ArrayAdapter<String> adapter;
    ArrayAdapter listAdapter;
    String name;
    EditText ee;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chatroom_fragment, container, false);
        super.onCreate(savedInstanceState);
        //e1 = (EditText)view.findViewById(R.id.editText);
        l1 = (ListView) view.findViewById(R.id.listView);
        arrayList = new ArrayList<>();

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        l1.setAdapter(adapter);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:156555417126:android:54fddcbcce90fe295a78e7") // Required for Analytics.
                .setApiKey("AIzaSyCNvlJQhcSc_5jKnP8_Xa17IQIv8zSKbHk") // Required for Auth.
                .setDatabaseUrl("https://fir-chatroom-76779.firebaseio.com/") // Required for RTDB.
                .build();
        try {
            FirebaseApp.initializeApp(getActivity() /* Context */, options, "secondary");
        } catch (Exception e) {

        }
        FirebaseApp app = FirebaseApp.getInstance("secondary");
        reference = FirebaseDatabase.getInstance(app).getReference().getRoot();
        request_username();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();


                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    set.add(((DataSnapshot) i.next()).getKey());

                }

                arrayList.clear();
                arrayList.addAll(set);
                for (int j = 0; j < adapter.getCount(); j++) {
                    items.add(adapter.getItem(j));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(NavigationDrawerActivity.this, "No network connectivity", Toast.LENGTH_SHORT).show();
            }
        });
        ChatFragment fragment = new ChatFragment();


        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dataHashMap.put("room_name", ((TextView) view).getText().toString());
                dataHashMap.put("user_name", name);
                ChatFragment fragment = new ChatFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();



            }
        });
        base.readTournaments(this);

        return view;

    }

    public void request_username() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Введите свое имя");
        ee = new EditText(getActivity());
        builder.setView(ee);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name = ee.getText().toString();


            }
        });

        builder.setNegativeButton("Назад", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                request_username();


            }
        });
        builder.show();

    }

    public void onCallback(ArrayList<TournamentParameters> tournamentsList,
                           ArrayList<String> tournamentsNames,
                           HashMap<String, String> ratingAnswer,
                           ArrayList<String> tournamentsAnswer,
                           ArrayList<PlayerParameters> playersList) {
        if (!(items.equals(tournamentsNames))) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < tournamentsNames.size(); i++) {
                map.put(tournamentsNames.get(i), "");
            }
            //reference.updateChildren(map);
        }
        items.clear();


    }





    /*@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:

            insert_data(v);

                break;
        }
    }

     */
    /*public void insert_data(View v)
    {

        switch (v.getId()) {
            case R.id.button :

                Map<String, Object> map = new HashMap<>();
                map.put("Hello", "");
                //reference.updateChildren(map);

        }


    }

     */

}
