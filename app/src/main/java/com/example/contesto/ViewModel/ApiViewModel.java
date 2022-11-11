package com.example.contesto.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.contesto.Models.ContestObject;
import com.example.contesto.Repository.FetchContestsRepository;

import java.util.List;

public class ApiViewModel extends ViewModel {

    private FetchContestsRepository repository;
    private LiveData<List<ContestObject>> liveContestList;

    public ApiViewModel() {
        super();
    }

    public void init() {
        repository = new FetchContestsRepository();
        liveContestList = repository.getContestsListAsync();
    }

    public void fetchContestFromApi() {

        //Executing to get Response
        repository.fetchContestFromApi();
    }

    public LiveData<List<ContestObject>> getAllContests() {

        return liveContestList;
    }
}
