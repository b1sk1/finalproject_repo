package com.SamsungProject.SamsungProject;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.SamsungProject.SamsungProject.ui.chats.ChatFragment;
import com.SamsungProject.SamsungProject.ui.chats.ChatRoomFragment;
import com.SamsungProject.SamsungProject.ui.players.PlayersFragment;
import com.SamsungProject.SamsungProject.ui.rating.RatingFragment;
import com.SamsungProject.SamsungProject.ui.tournaments.TournamentsFragment;

public class NavigationDrawerActivity extends AppCompatActivity {
    static int lastAttachedFragment = 0;
    Fragment ratingFiltersFragment = new RatingFiltersFragment();
    public void dismissRatingFiltersFragment() { //убрать панель фильтров
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!ratingFiltersFragment.isAdded()) {
            transaction.add(R.id.ratingframe, ratingFiltersFragment, "filter");
        }
        transaction.replace(R.id.ratingframe, new RatingFragment());
        transaction.addToBackStack(null);
        transaction.commit();
        //FragmentManager fm = getSupportFragmentManager();
        //fm.popBackStackImmediate();
    }

    /*Fragment tournamentsFiltersFragment = new TournamentFiltersFragment();
    public void dismissTournamentsFiltersFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!tournamentsFiltersFragment.isAdded()) {
            transaction.add(R.id.tournamentsframe, tournamentsFiltersFragment, "filter");
        }
        transaction.replace(R.id.tournamentsframe, new TournamentsFragment());
        transaction.addToBackStack(null);
        transaction.commit();
        //FragmentManager fm = getSupportFragmentManager();
        //fm.popBackStackImmediate();
    }

     */
    Fragment chatFragment = new ChatFragment();
    public void dismissChatFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(!chatFragment.isAdded()){
            transaction.add(R.id.fragment_container, chatFragment, "MyFragment");
        }
        transaction.replace(R.id.fragment_container, new ChatRoomFragment());
        transaction.commit();
    }



    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FloatingActionButton fab = findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_tournaments, R.id.nav_players, R.id.nav_organizations,
                R.id.nav_rating, R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }*/

    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
    @Override
    public void onBackPressed() {
        if (lastAttachedFragment == 2131361896){
           dismissChatFragment();
        }



    }
    @Override
    public void onAttachFragment(Fragment fragment) {
        // TODO Auto-generated method stub
        super.onAttachFragment(fragment);
        //lastAttachedFragment = fragment.getId();
        //Toast.makeText(getApplicationContext(), String.valueOf(fragment.getId()), Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

}
