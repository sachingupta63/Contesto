package com.example.contesto.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.contesto.Adapters.PlatformsListAdapter;
import com.example.contesto.Models.ContestObject;
import com.example.contesto.R;
import com.example.contesto.Utils.Constants;
import com.example.contesto.Utils.Methods;
import com.example.contesto.ViewModel.ApiViewModel;
import com.example.contesto.ViewModel.RoomViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.schibsted.spain.parallaxlayerlayout.ParallaxLayerLayout;
import com.schibsted.spain.parallaxlayerlayout.SensorTranslationUpdater;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ParentActivity {

    //Contest Tab Item
    ArrayList<String> mTabItemList;

    ApiViewModel apiViewModel;
    RoomViewModel mRoomViewModel;

    RecyclerView titlesRecycler;
    PlatformsListAdapter platformsListAdapter;

    //Floating Action Button
    TextView contestText,settingText;
    FloatingActionButton setting,contest;
    ExtendedFloatingActionButton action;

    //Parallax Layout
    ParallaxLayerLayout mParallaxLayout;
    SensorTranslationUpdater sensorTranslationUpdater;

    boolean isFabOpen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_main, content);


        mParallaxLayout = findViewById(R.id.mainActivityParrallax);
        sensorTranslationUpdater = new SensorTranslationUpdater(this);
        mParallaxLayout.setTranslationUpdater(sensorTranslationUpdater);

        mRoomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);

        contestText=findViewById(R.id.contestText);
        settingText=findViewById(R.id.settingText);
        setting=findViewById(R.id.setting);
        contest=findViewById(R.id.contest);
        action=findViewById(R.id.add_fab);

        action.shrink();
        contestText.setVisibility(View.GONE);
        settingText.setVisibility(View.GONE);
        setting.setVisibility(View.GONE);
        contest.setVisibility(View.GONE);

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isFabOpen) {
                    action.shrink();
                    setting.setVisibility(View.GONE);
//                    contest.setVisibility(View.GONE);
//                    contestText.setVisibility(View.GONE);
                    settingText.setVisibility(View.GONE);
                    isFabOpen=false;
                }else{
                    action.extend();
                    setting.setVisibility(View.VISIBLE);
//                    contest.setVisibility(View.VISIBLE);
//                    contestText.setVisibility(View.VISIBLE);
                    settingText.setVisibility(View.VISIBLE);
                    isFabOpen=true;
                }
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                action.shrink();
                setting.setVisibility(View.GONE);
                contest.setVisibility(View.GONE);
                contestText.setVisibility(View.GONE);
                settingText.setVisibility(View.GONE);
                isFabOpen=false;
            }
        });

//        contest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this,MainActivity2.class));
//                action.shrink();
//                setting.setVisibility(View.GONE);
//                contest.setVisibility(View.GONE);
//                contestText.setVisibility(View.GONE);
//                settingText.setVisibility(View.GONE);
//                isFabOpen=false;
//            }
//        });


        //Getting All Platform Contest List

        mRoomViewModel.getAllContests().observe(this, new Observer<List<ContestObject>>() {
            @Override
            public void onChanged(List<ContestObject> contestObjects) {
                EventBus.getDefault().post(contestObjects);
                //Log.e("Objs From DB>>>>",String.valueOf(contestObjects.size()));
                if(Methods.getIntPreferences(MainActivity.this, Constants.ISINTERNET,Constants.ISINTERNET)==0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("No Internet Connection")
                            .setMessage("You are not connected to internet, so the App will be showing cached Entries.Try Again Restarting App with Internet.")
                            .setPositiveButton("OK", null)
                            .setIcon(R.drawable.ic_baseline_arrow_forward_ios_24);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });


        //Initially When App we Install all tab item will be empty
        //Give error so we add all Platform and save it
        try {
            mTabItemList = (ArrayList<String>) Methods.fetchTabItems(this);

        }catch (NullPointerException e) {
            e.printStackTrace();
            // Displays all tabs by Default.
            mTabItemList = new ArrayList<>();
            mTabItemList.add(Constants.CODEFORCES);
            mTabItemList.add(Constants.HACKERRANK);
            mTabItemList.add(Constants.HACKEREARTH);
            mTabItemList.add(Constants.SPOJ);
            mTabItemList.add(Constants.ATCODER);
            mTabItemList.add(Constants.LEETCODE);
            mTabItemList.add(Constants.GOOGLE);
            mTabItemList.add(Constants.CODECHEF);

            // Bug fixed below : When App launches for first time Setting checkboxes remaining unmarked.
            Methods.saveTabItems(this,mTabItemList);
        }

        //Recycler View
        titlesRecycler = findViewById(R.id.titles_recycler_view);
        titlesRecycler.setLayoutManager(new LinearLayoutManager(this));
        platformsListAdapter = new PlatformsListAdapter(this,mTabItemList);
        titlesRecycler.setAdapter(platformsListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorTranslationUpdater.registerSensorManager();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorTranslationUpdater.unregisterSensorManager();
    }
}