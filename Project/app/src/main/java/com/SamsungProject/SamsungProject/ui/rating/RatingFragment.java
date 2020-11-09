package com.SamsungProject.SamsungProject.ui.rating;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;

import com.SamsungProject.SamsungProject.RatingFiltersFragment;
import com.itextpdf.kernel.pdf.*;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.SamsungProject.SamsungProject.Database;
import com.SamsungProject.SamsungProject.PlayerParameters;
import com.SamsungProject.SamsungProject.PlayerRating;
import com.SamsungProject.SamsungProject.PlayerRatingAdapter;
import com.SamsungProject.SamsungProject.R;
import com.SamsungProject.SamsungProject.TournamentParameters;


import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
//import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import static com.SamsungProject.SamsungProject.TournamentFiltersFragmentAdapter.answer;

class Compare implements Comparator<PlayerRating> {



    public int compare(PlayerRating a1, PlayerRating b1) {
        int rating1 = a1.getRating();
        int rating2 = b1.getRating();
        if (rating1 > rating2) {
            return -1;
        } else {
            return 1;
        }


    }

}


public class RatingFragment extends Fragment {
    static int selectedIndex;
    PlayerParameters findPlayer(String name, ArrayList<PlayerParameters> playersList){ //достать игрока из листа по нику
        for(PlayerParameters i : playersList){
            if (name.equals(i.getNickname())){
                return i;
            }
        }
        return null;
    }
    static PlayerRatingAdapter adapter;
    static ArrayList<PlayerRating> editableRatingList = new ArrayList<PlayerRating>();
    Fragment fragment = new RatingFiltersFragment();
    private void showFragment() { //показать панель фильтров
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {

            transaction.add(R.id.ratingframe, fragment, "filter");
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1; //чтобы иметь возможность писать в память телефона
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }


    }



    private RatingViewModel mViewModel;

    public static RatingFragment newInstance() {
        return new RatingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        verifyStoragePermissions(getActivity());
        ArrayList<PlayerRating> finalRatingList = new ArrayList<PlayerRating>();
        View view = inflater.inflate(R.layout.rating_fragment, container, false);
        Database base = new Database();
        base.readPlayers(new Database.PlayersCallback() {
            @Override
            public void onCallback(ArrayList<PlayerParameters> playersList, ArrayList<String> playersNames) {

            }
        });
        base.readTournaments(new Database.TournamentsCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCallback(ArrayList<TournamentParameters> tournamentsList,
                                   ArrayList<String> tournamentsNames,
                                   HashMap<String, String> ratingAnswer, ArrayList<String> tournamentsAnswer,
                                   ArrayList<PlayerParameters> playersList) {
                Button button = view.findViewById(R.id.convertButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertSingleChoiceItems(); ;

                    }});
                Button filtersTV = getActivity().findViewById(R.id.ratingfiltersTV); //кнопка для вызова панели фильтров
                filtersTV.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                ArrayList<PlayerRating>ratingList = new ArrayList<PlayerRating>();
                ArrayList<TournamentParameters> editableTournamentsList = new ArrayList<TournamentParameters>(tournamentsList);
                ListView listview = (ListView) view.findViewById(R.id.ratingListView);
                if (ratingAnswer.size() == 0) { // фильтры не выбраны, показываем первоначальный лист
                    finalRatingList.clear();
                    HashMap<String, Integer> rating = new HashMap<String, Integer>();

                    for (TournamentParameters j : tournamentsList) {
                        for (String i : j.getTournamentRating().keySet()) {
                            boolean flag = true;
                            String[] checkArray = i.split("-");
                            if (checkArray[checkArray.length - 1].equals("country")) {
                                flag = false;
                            }
                            if (flag) {
                                if (rating.containsKey(i)) {
                                    int prev_value = rating.get(i);
                                    rating.put(i, prev_value + j.getTournamentRating().get(i));
                                } else {
                                    rating.put(i, j.getTournamentRating().get(i));
                                }
                            }
                        }
                    }
                    for (String i : rating.keySet()) {
                        PlayerRating rating_of_player = new PlayerRating(i, rating.get(i));
                        ratingList.add(rating_of_player);
                    }
                    Comparator<PlayerRating> comp = new Compare();
                    Collections.sort(ratingList, comp);
                    ratingList.get(0).setPosition(1);
                    Log.d(ratingList.toString(), "meme");
                    for (int i = 1; i < ratingList.size(); i++) {
                        int prev_number = ratingList.get(i - 1).getRating();
                        int number;
                        number = ratingList.get(i).getRating();
                        if (number == prev_number) {
                            //String[] array = new String[1];
                            //array = ratingList.get(i-1).split("");
                            ratingList.get(i).setPosition(ratingList.get(i - 1).getPosition());
                            //ratingList.set(i, ratingList.get(i-1).split(" ")[0] +" " +ratingList.get(i));
                        } else {
                            ratingList.get(i).setPosition(i + 1);
                            //ratingList.set(i, i + 1 + " " + ratingList.get(i));
                        }
                    }

                    for(PlayerRating i : ratingList){
                        editableRatingList.add(i);
                    }
                    adapter = new PlayerRatingAdapter(getActivity(), R.layout.rating_adapter_item, ratingList);
                    adapter.notifyDataSetChanged();
                    ViewGroup header = (ViewGroup) inflater.inflate(R.layout.rating_listview_header,
                            listview, false);
                    listview.addHeaderView(header);
                    listview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();




                }
                else {
                    String monthNumber = ratingAnswer.get("Период времени");
                    String kind = ratingAnswer.get("Вид соревнований");
                    String mode = ratingAnswer.get("Режим соревнований"); //получаем ответы от фильтров
                    editableRatingList.clear();
                    finalRatingList.clear();
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    Log.d("okey", "cleared!");
                    adapter.notifyDataSetChanged();
                    adapter = new PlayerRatingAdapter(getActivity(), R.layout.rating_adapter_item, finalRatingList);
                    adapter.notifyDataSetChanged();
                    ViewGroup header = (ViewGroup) inflater.inflate(R.layout.rating_listview_header,
                            listview, false);
                    listview.addHeaderView(header);
                    listview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    Log.d("interestingfact", finalRatingList.size() + "");
                    Log.d("fckngslv", "yes!!");
                    /*
                    for(TournamentParameters tournament : tournamentsList){
                        Log.d("checkkind", tournament.getKind() + " " + answer.get(0));
                        if(!(answer.get(0).equals("все")) && !((tournament.getKind()).equals(answer.get(0)))){
                            Log.d("checkkind", "yes!!!");
                            editableTournamentsList.remove(tournament);
                        }
                        else if(!(answer.get(1).equals("все")) && !((tournament.getMode()).equals(answer.get(1)))){
                            editableTournamentsList.remove(tournament);
                        }

                    }
                     */
                    for(TournamentParameters tournament : tournamentsList){ //фильтрация


                        for (String i : ratingAnswer.keySet()){
                        }
                        if (!(monthNumber == null) && !(monthNumber.equals("все"))){
                            if(monthNumber.equals("3 месяца") && tournament.getMonthNumber() >=3 ){
                                editableTournamentsList.remove(tournament);
                            }
                            else if(monthNumber.equals("6 месяцев") && tournament.getMonthNumber() >= 6){
                                editableTournamentsList.remove(tournament);
                            }
                            else if(monthNumber.equals("12 месяцев") && tournament.getMonthNumber() >= 12){
                                editableTournamentsList.remove(tournament);
                            }

                        }
                        else if (!(kind == null) && !(kind.equals("все")) && !(kind.equals(tournament.getKind()))){
                            editableTournamentsList.remove(tournament);
                        }
                        else if (!(mode == null) && !((mode).equals("все")) && !(mode.equals(tournament.getMode()))) {
                            editableTournamentsList.remove(tournament);
                        }
                    }
                    for(TournamentParameters tournament : editableTournamentsList){
                    }
                    HashMap<String, Integer> rating = new HashMap<String, Integer>();
                    for (TournamentParameters j : editableTournamentsList) {
                        for (String i : j.getTournamentRating().keySet()) {
                            boolean flag = true;
                            String[] checkArray = i.split("-");
                            if (checkArray[checkArray.length - 1].equals("country")) {
                                flag = false;
                            }
                            if (flag) {
                                if (rating.containsKey(i)) {
                                    int prev_value = rating.get(i);
                                    rating.put(i, prev_value + j.getTournamentRating().get(i));
                                } else {
                                    rating.put(i, j.getTournamentRating().get(i));
                                }
                            }
                        }
                    }
                    for (String i : rating.keySet()) {
                        PlayerRating rating_of_player = new PlayerRating(i, rating.get(i));
                        ratingList.add(rating_of_player);
                    }
                    Comparator<PlayerRating> comp = new Compare();
                    Collections.sort(ratingList, comp);
                    if (ratingList.size() != 0) ratingList.get(0).setPosition(1);
                    for (int i = 1; i < ratingList.size(); i++) {
                        int prev_number = ratingList.get(i - 1).getRating();
                        int number;
                        number = ratingList.get(i).getRating();
                        if (number == prev_number) {
                            //String[] array = new String[1];
                            //array = ratingList.get(i-1).split("");
                            ratingList.get(i).setPosition(ratingList.get(i - 1).getPosition());
                            //ratingList.set(i, ratingList.get(i-1).split(" ")[0] +" " +ratingList.get(i));
                        } else {
                            ratingList.get(i).setPosition(i + 1);
                            //ratingList.set(i, i + 1 + " " + ratingList.get(i));
                        }
                    }
                    for(PlayerRating i : ratingList){
                        editableRatingList.add(i);
                    }
                    for(PlayerRating i : editableRatingList){
                    }
                    String city = ratingAnswer.get("Город участников");
                    String age = ratingAnswer.get("Возраст участников");
                    for(PlayerRating i : ratingList){
                        //try {

                            PlayerParameters player = findPlayer(i.getName(), playersList);
                            if (player != null) Log.d("checkwtfhapenning", player.getName() + " "
                                    + city + " " + player.getCity() + " " + age +
                                    " " + player.getAge());
                            if (player != null) Log.d("whoisplayer", i.getName() + " " + player.getAge());
                            if (player == null){
                                editableRatingList.remove(i);
                            }
                            else if(player.getAge().equals(Character.toString('-')) || player.getAge().equals("-")){
                                editableRatingList.remove(i);
                            }
                            else if (!(city == null) && !(city.equals("все")) && !(city.equals(player.getCity()))){
                                editableRatingList.remove(i);
                            }
                            else if (age == null || age.equals("все")){

                            }
                            else if (age.equals("0 - 15")){
                                if(Integer.parseInt(player.getAge()) > 15){
                                    editableRatingList.remove(i);
                                }
                            }
                            else if (age.equals("16 - 18")){
                                if(Integer.parseInt(player.getAge()) > 18 || Integer.parseInt(player.getAge()) < 16){
                                    editableRatingList.remove(i);
                                }
                            }
                            else if (age.equals("19+")){
                                if(Integer.parseInt(player.getAge()) < 19){
                                    editableRatingList.remove(i);
                                }
                            }


                    }
                    for(PlayerRating i : editableRatingList){
                        finalRatingList.add(i);
                    }

                    }

                filtersTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFragment();
                        filtersTV.setVisibility(View.INVISIBLE);
                        button.setVisibility(View.INVISIBLE);
                    }
                });
                ratingAnswer.clear();

                }


        });
        return view;
    }
    public void alertSingleChoiceItems(){ //конвертация в pdf/xls

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Выберите формат")
                .setSingleChoiceItems(R.array.choices, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        selectedIndex = arg1;


                    }


                })

                .setPositiveButton("Скачать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (selectedIndex == 0) { // выбран первый вариант, pdf
                            File pdfFile = new File("/storage/emulated/0/Download/results.pdf");
                            if (!pdfFile.exists()) {
                                try {
                                    pdfFile.createNewFile();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }

                            }


                            try {
                                PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
                                PdfDocument pdfDoc = new PdfDocument(writer);
                                Document document = new Document(pdfDoc);
                                float[] pointColumnWidths = {150F, 150F, 150F};
                                Table table = new Table(pointColumnWidths);

                                // Adding cells to the table
                                table.addCell(new Cell().add("Position"));
                                table.addCell(new Cell().add("Nickname"));
                                table.addCell(new Cell().add("Points"));

                                for (PlayerRating i : editableRatingList) {
                                    table.addCell(String.valueOf(i.getPosition()));
                                    table.addCell(String.valueOf(i.getName()));
                                    table.addCell(String.valueOf(i.getRating()));

                                }
                                document.add(table);
                                document.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                        else{ //создаем xls
                            WritableWorkbook myFirstWbook = null;
                            try {

                                myFirstWbook = Workbook.createWorkbook(new File("/storage/emulated/0/Download/results.xls"));

                                //create an Excel sheet
                                WritableSheet excelSheet = myFirstWbook.createSheet("Sheet 1", 0);

                                //add something into the Excel sheet
                                Label label = new Label(0, 0, "Место");
                                int row = 1;
                                excelSheet.addCell(label);
                                for(PlayerRating i : editableRatingList){
                                    Number number = new Number(0, row, i.getPosition());
                                    Label nickname = new Label(1, row, i.getName());
                                    Number points = new Number(2, row, i.getRating());
                                    excelSheet.addCell(number);
                                    excelSheet.addCell(nickname);
                                    excelSheet.addCell(points);
                                    row++;
                                }


                                label = new Label(1, 0, "Никнейм");
                                excelSheet.addCell(label);

                                label = new Label(2, 0, "Очки");
                                excelSheet.addCell(label);


                                myFirstWbook.write();


                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (WriteException e) {
                                e.printStackTrace();
                            } finally {

                                if (myFirstWbook != null) {
                                    try {
                                        myFirstWbook.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (WriteException e) {
                                        e.printStackTrace();
                                    }
                                }


                            }





                        }
                    }
                })

                .setNegativeButton("Назад", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // removes the dialog from the screen

                    }
                })

                .show();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RatingViewModel.class);
        // TODO: Use the ViewModel
    }

}
