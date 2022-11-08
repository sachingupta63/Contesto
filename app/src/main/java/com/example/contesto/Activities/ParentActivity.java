package com.example.contesto.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.contesto.Models.ContestObject;
import com.example.contesto.R;
import com.example.contesto.Utils.Constants;
import com.example.contesto.Utils.Methods;
import com.example.contesto.ViewModel.ApiViewModel;
import com.example.contesto.ViewModel.RoomViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class ParentActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    ApiViewModel apiViewModel;
    RoomViewModel mRoomViewModel;
    boolean doubleBackPressExitOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        NavigationView navigationView = findViewById(R.id.nav_view);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(com.google.android.material.R.color.design_default_color_on_primary));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        navigationView.setNavigationItemSelectedListener
                (new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                        switch (item.getItemId()) {

                            case R.id.codeforces:
                                drawerLayout.closeDrawers();
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        Uri uri = Uri.parse("https://codeforces.com/contests");
                                        viewContest(uri);
                                    }
                                }, 500);
                                break;

                            case R.id.codechef:
                                drawerLayout.closeDrawers();
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        Uri uri = Uri.parse("https://www.codechef.com/contests?itm_medium=home&itm_campaign=allcontests");
                                        viewContest(uri);
                                    }
                                }, 500);
                                break;

                            case R.id.leetcode:
                                drawerLayout.closeDrawers();
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        Uri uri = Uri.parse("https://leetcode.com/contest/");
                                        viewContest(uri);
                                    }
                                }, 500);
                                break;

                            case R.id.spoj:
                                drawerLayout.closeDrawers();
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        Uri uri = Uri.parse("https://www.spoj.com/contests/");
                                        viewContest(uri);
                                    }
                                }, 500);
                                break;
                            case R.id.google:
                                drawerLayout.closeDrawers();
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        Uri uri = Uri.parse("https://codingcompetitions.withgoogle.com/");
                                        viewContest(uri);
                                    }
                                }, 500);
                                break;

                            case R.id.topcoder:
                                drawerLayout.closeDrawers();
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        Uri uri = Uri.parse("https://www.topcoder.com/challenges?tracks[DS]=true&tracks[Des]=true&tracks[Dev]=true&tracks[QA]=true&types[]=CH&types[]=F2F&types[]=TSK");
                                        viewContest(uri);
                                    }
                                }, 500);
                                break;

                            case R.id.atcoder:
                                drawerLayout.closeDrawers();
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        Uri uri = Uri.parse("https://atcoder.jp/contests/");
                                        viewContest(uri);
                                    }
                                }, 500);
                                break;
                        }
                        return true;
                    }
                });

        mRoomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        apiViewModel = new ViewModelProvider(this).get(ApiViewModel.class);
        apiViewModel.init();

        new Methods.InternetCheck(this).isInternetConnectionAvailable(new Methods.InternetCheck.InternetCheckListener() {
            @Override
            public void onComplete(boolean connected) {
                if(connected){
                    Log.e("INTERNET","CONNECTED");
                    Methods.setPreferences(ParentActivity.this, Constants.ISINTERNET, Constants.ISINTERNET,1);
                    apiViewModel.fetchContestFromApi();
                }else{
                    Methods.setPreferences(ParentActivity.this,Constants.ISINTERNET, Constants.ISINTERNET,0);
                }
            }
        });

        apiViewModel.getAllContests().observe(ParentActivity.this, new Observer<List<ContestObject>>() {
            @Override
            public void onChanged(List<ContestObject> contestObjects) {
                mRoomViewModel.deleteAndAddAllTuples(contestObjects);
            }
        });

    }

    private void viewContest(Uri uri){
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if(doubleBackPressExitOnce)
            {
                super.onBackPressed();
                return;
            }
            this.doubleBackPressExitOnce = true;
            Methods.showToast(this,"Press Back Again to Exit");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    doubleBackPressExitOnce = false;
                }
            },2000);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void finish() {
        super.finish();

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.appbar_menu, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       //TODO Add search  view over tool bar
        return true;
    }
}