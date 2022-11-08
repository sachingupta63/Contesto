package com.example.contesto.RoomDataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.contesto.Models.ContestObject;
import com.example.contesto.Utils.Constants;

import java.util.List;

@Dao
public interface RoomDAO {

    @Insert
    void addContest(ContestObject contestObject);

    @Insert
    void addAllContest(List<ContestObject> AllContest);

    @Query("SELECT * FROM "+ Constants.CONTEST_OBJECT_TABLE_NAME)
    LiveData<List<ContestObject>> getAllContests();

    @Query("SELECT * FROM "+ Constants.CONTEST_OBJECT_TABLE_NAME +" WHERE platform = :Platform")
    List<ContestObject> findContestByPlatform(String Platform);

    @Query("DELETE FROM "+Constants.CONTEST_OBJECT_TABLE_NAME)
    void deleteAllTuples();

    @Query("SELECT * FROM "+ Constants.CONTEST_OBJECT_TABLE_NAME+" WHERE start BETWEEN :start_date AND :end_date")
    List<ContestObject> getContestByTime(String start_date, String end_date);

}