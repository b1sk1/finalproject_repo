package com.SamsungProject.SamsungProject;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.SamsungProject.SamsungProject.ui.players.PlayersFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.internal.DebouncingOnClickListener;


public class TournamentFiltersFragment extends Fragment {

    Class mFragment = PlayersFragment.class;
    LinearLayout linearLayout;

    Button applyFiltersButton;

    TextView textviewReset;

    ExpandableListView expandablelistviewFilter;

    //PlayersFragment mContext;
    NavigationDrawerActivity mContext;
    Context playersFragmentContext;
    List<FilterHeader> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    TournamentFiltersFragmentAdapter listAdapter;

    private OnFragmentInteractionListener mListener;

    public TournamentFiltersFragment() {
        // Required empty public constructor
    }


    public static RatingFiltersFragment newInstance() {
        RatingFiltersFragment fragment = new RatingFiltersFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expandablelistviewFilter = view.findViewById(R.id.tournamentsexpandablelistview_filter);
        linearLayout = view.findViewById(R.id.tournamentslinearlayout_filter);
        applyFiltersButton = view.findViewById(R.id.tournamentsapplyFiltersBtn);
        textviewReset = view.findViewById(R.id.tournamentsbutton_reset);
        prepareListData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tournamentfragment_filter, container, false);
        return view;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<FilterHeader>();
        listDataChild = new HashMap<>();
        // Adding child data
        listDataHeader.add(new FilterHeader("Дата", "прошедшие/предстоящие/все"));
        listDataHeader.add(new FilterHeader("Временной промежуток", "1 месяц/3 месяца/6 месяцев/12 месяцев"));
        listDataHeader.add(new FilterHeader("Вид соревнований", "online/LAN/все"));
        listDataHeader.add(new FilterHeader("Режим соревнований", "классика/Ultimate Team/все"));
        listDataHeader.add(new FilterHeader("Город проведения соревнований", "Москва/Санкт-Петербург/все"));





// Adding child data
        List<String> isHeld = new ArrayList<>();
        isHeld.add("прошедшие");
        isHeld.add("предстоящие");
        isHeld.add("все");

        List<String> time = new ArrayList<>();
        time.add("1 месяц");
        time.add("3 месяца");
        time.add("6 месяцев");
        time.add("12 месяцев");

        List<String> type = new ArrayList<>();
        type.add("online");
        type.add("LAN");
        type.add("все");


        List<String> mode = new ArrayList<>();
        mode.add("классика");
        mode.add("Ultimate Team");
        mode.add("все");

        List<String> city = new ArrayList<>();
        city.add("Москва");
        city.add("Санкт-Петербург");
        city.add("все");

        listDataChild.put(listDataHeader.get(0).getTitle(), isHeld);
        listDataChild.put(listDataHeader.get(1).getTitle(), time); // Header, Child data
        listDataChild.put(listDataHeader.get(2).getTitle(), type);
        listDataChild.put(listDataHeader.get(3).getTitle(), mode);
        listDataChild.put(listDataHeader.get(4).getTitle(), city);

        listAdapter = new TournamentFiltersFragmentAdapter(mContext, listDataHeader, listDataChild);
        expandablelistviewFilter.setAdapter(listAdapter);
        PlayersFragment fragment = new PlayersFragment();
        fragment.getContext(new PlayersFragment.ContextCallback() {
            @Override
            public void onCallback(Context context) {
                playersFragmentContext = context;
            }
        });
        fragment.getFragmentActivity(new PlayersFragment.ActivityCallback() {
            @Override
            public void onCallback(Activity activity) {
                //mContext = activity;
            }
        });

        /*linearLayout.setOnClickListener(new DebouncingOnClickListener() {
            @Override
            public void doClick(View v) {
                mContext.dismissTournamentsFiltersFragment();
            }
        });

        applyFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mContext.dismissTournamentsFiltersFragment();
            }
        });


         */

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NavigationDrawerActivity) {
            mContext = (NavigationDrawerActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // : Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
