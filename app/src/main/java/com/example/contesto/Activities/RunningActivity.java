package com.example.contesto.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;

import com.example.contesto.Adapters.CardAdapter;
import com.example.contesto.Adapters.RunningContestAdapter;
import com.example.contesto.Models.ContestObject;
import com.example.contesto.R;
import com.example.contesto.Utils.Methods;
import com.example.contesto.ViewModel.RoomViewModel;

import org.greenrobot.eventbus.EventBus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RunningActivity extends AppCompatActivity {

    private RecyclerView mRecyclerContest;
    private RunningContestAdapter mRunningContestAdapter;
    RoomViewModel mRoomViewModel;
    List<ContestObject> contestByRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        mRecyclerContest = findViewById(R.id.RunningContestRecycler);
        mRecyclerContest.setLayoutManager(new LinearLayoutManager(this));
        contestByRunning = new ArrayList<>();
        mRunningContestAdapter = new RunningContestAdapter(this,contestByRunning);
        mRecyclerContest.setAdapter(mRunningContestAdapter);
        mRunningContestAdapter.setData(contestByRunning);

        mRoomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        mRoomViewModel.getAllContests().observe(this, new Observer<List<ContestObject>>() {

            @Override
            public void onChanged(List<ContestObject> contestObjects) {
                EventBus.getDefault().post(contestObjects);
                for(ContestObject co:contestObjects){
                    //if(filtertime(co)){
                        co.setStart(Methods.utcToLocalTimeZone(RunningActivity.this,co.getStart()));
                        co.setEnd(Methods.utcToLocalTimeZone(RunningActivity.this,co.getEnd()));
                        co.setDuration(Methods.secondToFormatted(co.getDuration()));

                            contestByRunning.add(co);

                    //}
                }

                mRunningContestAdapter.setData(contestByRunning);
            }
        });




    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private boolean durationMin(ContestObject co){
//        String formatDuration = "";
//        LocalDateTime start = null,end=null;
//        start = LocalDateTime.parse(co.getStart().substring(0, co.getStart().length() - 2));
//        end=LocalDateTime.parse(co.getEnd().substring(0, co.getEnd().length() - 2));
//        LocalTime time=LocalTime.now();
//
//        if( start.get end.getHour()>=currentDate.getHours())
//            return true;
//        return false;
//    }

//    private boolean filtertime(ContestObject contest) {
//        String strDate = contest.getStart().substring(0,10);
//
//        long noOfDaysBetween;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            LocalDate currentDate= null;
//            currentDate = LocalDate.now();
//            LocalDate contestDate=LocalDate.parse(strDate);
//            LocalDate contestEndDate=LocalDate.parse((contest.getEnd().substring(0,10)));
//
//            if(contestDate.equals(currentDate) && contestEndDate.equals(currentDate)){
//                return true;
//            }
//
//
//        } else {
//            Calendar cal = Calendar.getInstance();
//            Date currentDate  =  Calendar.getInstance().getTime();
//            cal.set(Calendar.YEAR, Integer.parseInt(strDate.substring(0,4)));
//            cal.set(Calendar.MONTH, Integer.parseInt(strDate.substring(5,7)) -1);
//            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strDate.substring(8,10)));
//            Date contestDate = cal.getTime();
//
//            Calendar cal2 = Calendar.getInstance();
//            cal2.set(Calendar.YEAR, Integer.parseInt(strDate.substring(0,4)));
//            cal2.set(Calendar.MONTH, Integer.parseInt(strDate.substring(5,7)) -1);
//            cal2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strDate.substring(8,10)));
//            Date contestEndDate = cal2.getTime();
//            // dividing by / 24*60*60*1000
//            noOfDaysBetween = (contestDate.getTime() - contestEndDate.getTime())/86400000;
//
//            if(noOfDaysBetween==0)
//                return true;
//        }
//
//        return false;
//
//
//
//    }
}