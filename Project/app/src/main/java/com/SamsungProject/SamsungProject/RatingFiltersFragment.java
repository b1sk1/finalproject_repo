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


public class RatingFiltersFragment extends Fragment {

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


    RatingFIltersExpandableAdapter listAdapter;

    private OnFragmentInteractionListener mListener;

    public RatingFiltersFragment() {
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
        expandablelistviewFilter = view.findViewById(R.id.expandablelistview_filter);
        linearLayout = view.findViewById(R.id.linearlayout_filter);
        applyFiltersButton = view.findViewById(R.id.applyFiltersBtn);
        textviewReset = view.findViewById(R.id.button_reset);
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
        View view = inflater.inflate(R.layout.ratingfragment_filter, container, false);
        return view;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<FilterHeader>();
        listDataChild = new HashMap<>();
        // Adding child data
        listDataHeader.add(new FilterHeader("Период времени", "3 месяца/6 месяцев/12 месяцев/все"));
        listDataHeader.add(new FilterHeader("Вид соревнований", "online/LAN/все"));
        listDataHeader.add(new FilterHeader("Режим соревнований", "классика/Ultimate Team/все"));
        listDataHeader.add(new FilterHeader("Город участников", "Москва/Санкт-Петербург/все"));
        listDataHeader.add(new FilterHeader("Возраст участников", "0-15/16-18/19+/все"));




// Adding child data
        List<String> period = new ArrayList<>();

        period.add("3 месяца");
        period.add("6 месяцев");
        period.add("12 месяцев");
        period.add("все");

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


        List<String> age = new ArrayList<>();
        age.add("0 - 15");
        age.add("16 - 18");
        age.add("19+");
        age.add("все");


        listDataChild.put(listDataHeader.get(0).getTitle(), period); // Header, Child data
        listDataChild.put(listDataHeader.get(1).getTitle(), type); // Header, Child data
        listDataChild.put(listDataHeader.get(2).getTitle(), mode);
        listDataChild.put(listDataHeader.get(3).getTitle(), city);
        listDataChild.put(listDataHeader.get(4).getTitle(), age);

        listAdapter = new RatingFIltersExpandableAdapter(mContext, listDataHeader, listDataChild);
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

        linearLayout.setOnClickListener(new DebouncingOnClickListener() {
            @Override
            public void doClick(View v) {
                mContext.dismissRatingFiltersFragment();
            }
        });

        applyFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mContext.dismissRatingFiltersFragment();
            }
        });


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

