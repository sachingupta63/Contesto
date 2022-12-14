package com.example.contesto.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.contesto.Adapters.CardAdapter;
import com.example.contesto.Models.ContestObject;
import com.example.contesto.R;
import com.example.contesto.Utils.Constants;
import com.example.contesto.Utils.Methods;
import com.example.contesto.ViewModel.ApiViewModel;
import com.example.contesto.ViewModel.RoomViewModel;

import org.greenrobot.eventbus.EventBus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ShowContestActivity extends AppCompatActivity {

    private RecyclerView mRecyclerCodeforces;
    private CardAdapter mCardAdapter;
    private ImageView mContestImage;
    RoomViewModel mRoomViewModel;
    //ApiViewModel apiViewModel;
    List<ContestObject> contestByPlatform;
    String website;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contest);

        mContestImage = findViewById(R.id.contest_image);
        website = getIntent().getStringExtra(Constants.WEBSITE);

        if(website.equals(Constants.GOOGLE))
        {
            website="Kick Start";       // due to API result ,for Google
        }

        mRecyclerCodeforces = findViewById(R.id.ContestCardsRecycler);
        mRecyclerCodeforces.setLayoutManager(new LinearLayoutManager(this));
        contestByPlatform = new ArrayList<>();
        mCardAdapter = new CardAdapter(this,contestByPlatform);
        mRecyclerCodeforces.setAdapter(mCardAdapter);
        mCardAdapter.setData(contestByPlatform);


        mRoomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        mRoomViewModel.getAllContests().observe(this, new Observer<List<ContestObject>>() {
            @Override
            public void onChanged(List<ContestObject> contestObjects) {
                EventBus.getDefault().post(contestObjects);
                boolean running,rated,hour24,week1,week2,month1;
                running= Methods.getIntPreferences(ShowContestActivity.this, Constants.SWITCH_RUNNING, Constants.SWITCH_RUNNING)!=0;
                rated=Methods.getIntPreferences(ShowContestActivity.this, Constants.SWITCH_RATED, Constants.SWITCH_RATED)!=0;
                hour24=Methods.getIntPreferences(ShowContestActivity.this, Constants.SWITCH_24HOURS, Constants.SWITCH_24HOURS)!=0;
                week1=Methods.getIntPreferences(ShowContestActivity.this, Constants.SWITCH_UPCOMING_SEVEN_DAYS, Constants.SWITCH_UPCOMING_SEVEN_DAYS)!=0;
                week2=Methods.getIntPreferences(ShowContestActivity.this, Constants.SWITCH_2WEEKS, Constants.SWITCH_2WEEKS)!=0;
                month1=Methods.getIntPreferences(ShowContestActivity.this, Constants.SWITCH_1MONTH, Constants.SWITCH_1MONTH)!=0;
                mRecyclerCodeforces.setAdapter(mCardAdapter);
                for(ContestObject contest :contestObjects)
                {
                    if(contest.getPlatform().toLowerCase().equals(website.toLowerCase()))
                    {
                        if(running || rated || hour24 || week1 || week2 || month1)
                        {
                            /** for filtering RATED Contest*/
                            if(rated)
                            {
                                if(running)
                                {
                                    if(contest.getStatus().equals("CODING"))
                                    {
                                        filterRatedContest(contest);
                                    }
                                }
                                else if(hour24)
                                {
                                    if(filtertime(contest,1))
                                    {
                                        filterRatedContest(contest);
                                    }
                                }
                                else if(week1)
                                {
                                    if(filtertime(contest,7))
                                    {
                                        filterRatedContest(contest);
                                    }
                                }
                                else if(week2)
                                {
                                    if(filtertime(contest,14))
                                    {
                                        filterRatedContest(contest);
                                    }
                                }
                                else if(month1)
                                {
                                    if(filtertime(contest,30))
                                    {
                                        filterRatedContest(contest);
                                    }
                                }
                                else
                                {
                                    filterRatedContest(contest);
                                }
                            }
                            /** for filtering UN-RATED Contest*/
                            else
                            {
                                if(running)
                                {
                                    if(contest.getStatus().equals("CODING"))
                                    {
                                        contestByPlatform.add(contest);
                                    }
                                }
                                else if(hour24)
                                {
                                    if(filtertime(contest,1))
                                    {
                                        contestByPlatform.add(contest);
                                    }
                                }
                                else if(week1)
                                {
                                    if(filtertime(contest,7))
                                    {
                                        contestByPlatform.add(contest);
                                    }
                                }
                                else if(week2)
                                {
                                    if(filtertime(contest,14))
                                    {
                                        contestByPlatform.add(contest);
                                    }
                                }
                                else if(month1)
                                {
                                    if(filtertime(contest,30))
                                    {
                                        contestByPlatform.add(contest);
                                    }
                                }
                            }
                        }
                        else
                        {
                            contestByPlatform.add(contest);
                        }
                    }
                }
                mCardAdapter.setData(contestByPlatform);

            }
        });
        assert website != null;
        switch (website) {
            case Constants.CODEFORCES:
                mContestImage.setImageDrawable(getResources().getDrawable(R.drawable.codeforces2));
                break;
            case Constants.CODECHEF:
                mContestImage.setImageDrawable(getResources().getDrawable(R.drawable.codechef2));
                break;
            case Constants.HACKEREARTH:
                mContestImage.setImageDrawable(getResources().getDrawable(R.drawable.hackerearth2));
                break;
            case Constants.HACKERRANK:
                mContestImage.setImageDrawable(getResources().getDrawable(R.drawable.hackerrank2));
                break;
            case Constants.LEETCODE:
                mContestImage.setImageDrawable(getResources().getDrawable(R.drawable.leetcode2));
                break;
            case Constants.SPOJ:
                mContestImage.setImageDrawable(getResources().getDrawable(R.drawable.spoj2));
                break;
            case Constants.GOOGLE2:
                mContestImage.setImageDrawable(getResources().getDrawable(R.drawable.google2));
                break;
            case Constants.ATCODER:
                mContestImage.setImageDrawable(getResources().getDrawable(R.drawable.atcoder2));
                break;
        }

        contestByPlatform = mRoomViewModel.findContestByPlatform(Methods.getSiteName(website));
        for(ContestObject co: contestByPlatform){
            co.setStart(Methods.utcToLocalTimeZone(ShowContestActivity.this,co.getStart()));
            co.setEnd(Methods.utcToLocalTimeZone(ShowContestActivity.this,co.getEnd()));
            co.setDuration(Methods.secondToFormatted(co.getDuration()));
        }
        mCardAdapter.setData(contestByPlatform);
    }

    private boolean filtertime(ContestObject contest,long i) {
        String strDate = contest.getStart().substring(0,10);

        long noOfDaysBetween;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate currentDate= null;
            currentDate = LocalDate.now();
            LocalDate contestDate=LocalDate.parse(strDate);

            //calculating number of days remaining  for Contest
            noOfDaysBetween = ChronoUnit.DAYS.between(currentDate, contestDate);
        } else {
            Calendar cal = Calendar.getInstance();
            Date currentDate  =  Calendar.getInstance().getTime();
            cal.set(Calendar.YEAR, Integer.parseInt(strDate.substring(0,4)));
            cal.set(Calendar.MONTH, Integer.parseInt(strDate.substring(5,7)) -1);
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strDate.substring(8,10)));
            Date contestDate = cal.getTime();
            // dividing by / 24*60*60*1000
            noOfDaysBetween = (contestDate.getTime() - currentDate.getTime())/86400000;
        }

        if(noOfDaysBetween>0 && noOfDaysBetween<=i)
            return true;
        else
            return false;

    }

    private void filterRatedContest(ContestObject contest) {

        String platform=contest.getPlatform().toLowerCase();
        //FOR CODECHEF
        if(platform.equals("codechef"))
        {
            String[] keywords={"Cook-Off","Lunchtime","Starters","Challenge"};
            for(String x: keywords)
            {
                if(contest.getTitle().contains(x))
                    contestByPlatform.add(contest);
            }
        }
        // FOR  CODEFORCES
        else if (platform.equals("codeforces"))
        {
            String[] keywords={"#"};
            for(String x: keywords)
            {
                if(contest.getTitle().contains(x))
                    contestByPlatform.add(contest);
            }
        }
        else if (platform.equals("hackerrank"))
        {
            //Toast.makeText(this, "NO RATED CONTEST:(", Toast.LENGTH_SHORT).show();
           String[] keywords={"#"};
           for(String x: keywords)
           {
               if(contest.getTitle().contains(x))
                   contestByPlatform.add(contest);
           }
        }
        else if (platform.equals("hackerearth"))
        {
            String[] keywords={"hiring", "challenge"};
            for(String x: keywords)
            {
                if(contest.getTitle().contains(x))
                    contestByPlatform.add(contest);
            }
        }
        else if (platform.equals("spoj"))
        {
            //Toast.makeText(this, "NO RATED CONTEST:(", Toast.LENGTH_SHORT).show();
           String[] keywords={"#"};
           for(String x: keywords)
           {
               if(contest.getTitle().contains(x))
                   contestByPlatform.add(contest);
           }
        }
        else if (platform.equals("atcoder"))
        {
            String[] keywords={"heuristic","beginner","grand","regular"};
            for(String x: keywords)
            {
                if(contest.getTitle().contains(x))
                    contestByPlatform.add(contest);
            }
        }
        else if (platform.equals("leetcode"))
        {
            String[] keywords={"weekly","biweekly"};
            for(String x: keywords)
            {
                if(contest.getTitle().contains(x))
                    contestByPlatform.add(contest);
            }
        }
        else if (platform.equals("kick start"))
        {
            String[] keywords={"Round"};
            for(String x: keywords)
            {
                if(contest.getTitle().contains(x))
                    contestByPlatform.add(contest);
            }
        }


    }



}