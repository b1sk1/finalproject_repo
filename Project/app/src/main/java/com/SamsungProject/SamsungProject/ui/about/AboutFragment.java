package com.SamsungProject.SamsungProject.ui.about;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.SamsungProject.SamsungProject.Database;
import com.SamsungProject.SamsungProject.ExpandableListAdapter;
import com.SamsungProject.SamsungProject.MainActivity;
import com.SamsungProject.SamsungProject.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AboutFragment extends Fragment {
    ExpandableListAdapter listAdapter;
    ArrayList<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild = new HashMap<>();

    private AboutViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment, container, false);
        listDataHeader.add("Для чего предназначена программа?");
        List<String> forwhat = Arrays.asList("В России проводится достаточно много соревнований по киберфутболу в дисциплине FIFA, однако их организаторы разрозненны, зачастую конкурируют между собой, итоги турниров тяжело найти в структурированном виде. При определении лучших спортсменов за период (например, за календарный год, или по итогам ежегодно обновляемой версии игры) используется простое голосование болельщиков, в результате чего объективные спортивные достижения могут уступать болельщицким пристрастиям. \n" +
                        "\n" +
                        "Приложение разработано с целью систематизации информации по спортсменам-киберфутболистам, принимающим участие в соревнованиях; проводимым в России (а при желании и за рубежом) турнирам в различных режимах и вариациях игры (обычные команды, Ultimate Team, онлайн- и лан-турниры, личные и парные соревнования). ");
        listDataChild.put("Для чего предназначена программа?", forwhat);
        listDataHeader.add("Каким образом происходит подсчет рейтинга?");
        List<String> howratingcount = Arrays.asList("Все довольно просто. Из каждого соревнования, представленного в приложении, выделяются 16 лучших игроков, согласно турнирной сетке. Первоначальные очки распределяются между игроками следующим образом: \n" +
                "\n" +
                "1 место - 400 очков \n" +
                "\n" +
                "2 место - 300 очков \n" +
                "\n" +
                "3 место - 250 очков \n" +
                "\n" +
                "3-4 место / 4 место - 200 очков \n" +
                "\n" +
                "5-6 место - 150 очков \n" +
                "\n" +
                "5-8 место / 7-8 место - 100 очков \n" +
                "\n" +
                "9-12 место - 75 очков \n" +
                "\n" +
                "9-16 место / 13-16 место - 50 очков \n" +
                "\n" +
                "Далее эти очки умножаются на вес турнира, который отображается во вкладке “турниры”. Например, Вася занял 1 место на турнире с весом 5. Значит, он получил 400 * 5 = 2000 очков.  \n" +
                "\n" +
                "Итоговый рейтинг игроков — это список спортсменов, упорядоченный по невозрастанию рейтинговых очков, полученных за все турниры.   ");
        listDataChild.put("Каким образом происходит подсчет рейтинга?", howratingcount);
        ExpandableListView expListView = view.findViewById(R.id.aboutListView);
        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AboutViewModel.class);
        // TODO: Use the ViewModel
    }

}
