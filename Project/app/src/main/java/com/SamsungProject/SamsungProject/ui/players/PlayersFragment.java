package com.SamsungProject.SamsungProject.ui.players;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.arch.lifecycle.ViewModelProviders;
import android.text.Editable;
import android.text.TextWatcher;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.SamsungProject.SamsungProject.Database;
import com.SamsungProject.SamsungProject.ExpandableListAdapter;
import com.SamsungProject.SamsungProject.PlayerParameters;
import com.SamsungProject.SamsungProject.PlayerRating;
import com.SamsungProject.SamsungProject.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PlayersFragment extends Fragment {
    public void getContext(final ContextCallback callback){
        callback.onCallback(getActivity());
    }
    public void getFragmentActivity(final ActivityCallback callback){
        callback.onCallback(getActivity());
    }
    //Activity playersFragmentActivity = getActivity();



    private PlayersViewModel mViewModel;

    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.players_fragment, container, false);
        Database base = new Database(); //чтение информацию из БД
        base.readPlayers(new Database.PlayersCallback() {
            @Override
            public void onCallback(ArrayList<PlayerParameters> playersList, ArrayList<String> playersNames) {
                listDataHeader = playersNames;
                List<String> constantListDataHeader = new ArrayList<String>(playersNames); //постоянный лист, для фильтрации
                List<String> editableListDataHeader = playersNames;
                String[] listDataHeaderArray = new String[listDataHeader.size()];
                playersNames.toArray(listDataHeaderArray);
                Collections.sort(playersNames);
                listDataChild = new HashMap<String, List<String>>();
                HashMap<String, ArrayList<String>> playerInfo = new HashMap<String, ArrayList<String>>();
                for (PlayerParameters j : playersList) {
                    ArrayList<String> infoList = new ArrayList<String>();
                    infoList.add("Ник: " + j.getNickname());
                    infoList.add("Возраст: " + j.getAge());
                    infoList.add("Город: " + j.getCity());
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

                        return false;
                    }
                });
                EditText textsearch = view.findViewById(R.id.txtsearch);
                textsearch.setImeOptions(EditorInfo.IME_ACTION_DONE);
                textsearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            editableListDataHeader.clear();
                            for(String item2 : constantListDataHeader){
                                editableListDataHeader.add(item2);
                            }
                            //editableListDataHeader = Collections.copy(editableListDataHeader, constantListDataHeader);
                            for (String item : listDataHeaderArray) {
                                if (item.toLowerCase().replaceAll("ё", "е").
                                        contains(textsearch.getText().
                                                toString().replaceAll("ё", "е").toLowerCase())) {
                                } else {
                                    editableListDataHeader.remove(item);
                                }
                                listDataHeader = new ArrayList<>(editableListDataHeader);
                            }



                            return false;





                        }


                        return false;
                    }


                });


                textsearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                    }
                    @Override
                    public void afterTextChanged(Editable s) {

                        for (String item : listDataHeaderArray) {

                            if (item.toLowerCase().contains(textsearch.getText().toString().toLowerCase())){
                            }
                            else{
                                listDataHeader.remove(item);
                            }
                        }
                    }
                });

            }
            });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PlayersViewModel.class);
        // TODO: Use the ViewModel
    }
    public interface ContextCallback {
        void onCallback(Context context);
    }
    public interface ActivityCallback{
        void onCallback(Activity activity);
    }



}