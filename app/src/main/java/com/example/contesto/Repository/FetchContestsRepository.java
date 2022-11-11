package com.example.contesto.Repository;

import android.net.DnsResolver;
import android.os.AsyncTask;
import android.os.Build;
import android.telecom.Call;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.contesto.Models.ContestObject;
import com.example.contesto.RetrofitService.APIClient;
import com.example.contesto.RetrofitService.ApiInterface;

import java.util.List;
import java.util.Objects;

import retrofit2.Callback;
import retrofit2.Response;



public class FetchContestsRepository {

    private ApiInterface apiInterface;
    private FetchApiAsyncTask fetchApiAsyncTask;

    public FetchContestsRepository(){

        apiInterface = APIClient.getClient().create(ApiInterface.class);
        retrofit2.Call<List<ContestObject>> call = apiInterface.getAllContestsFromApi();

        //Creating Instance of Async Task that fetch data from api
        fetchApiAsyncTask = new FetchApiAsyncTask(call);

    }

    public LiveData<List<ContestObject>> getContestsListAsync(){
        //Calling the function in FetchApiAsyncTask Class to return list of Contest
        return fetchApiAsyncTask.getLiveContestsList();
    }

    //Executing the FetchApiAsyncTask that fetch data from api
    public void fetchContestFromApi(){
        fetchApiAsyncTask.execute();
    }

    private static class FetchApiAsyncTask extends AsyncTask<Void,Void,Void> {

        private retrofit2.Call<List<ContestObject>> call;
        private MutableLiveData<List<ContestObject>> liveContestList;

        private FetchApiAsyncTask(retrofit2.Call<List<ContestObject>> call){
            this.call = call;
            liveContestList = new MutableLiveData<>();
        }

        //Returning Api Response
        private MutableLiveData<List<ContestObject>> getLiveContestsList () {
            return  liveContestList;
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        protected Void doInBackground(Void... voids) {
            call.enqueue(new Callback<List<ContestObject>>() {
                @Override
                public void onResponse(retrofit2.Call<List<ContestObject>> call, Response<List<ContestObject>> response) {

                    //Getting API Response

                    List<ContestObject> apiResponse = response.body();



                    assert apiResponse != null;

                    //Updating api response to mutable live data
                    liveContestList.postValue(response.body());

                }

                @Override
                public void onFailure(retrofit2.Call<List<ContestObject>> call, Throwable t) {
                    Log.e("API FETCH ERROR>>>", Objects.requireNonNull(t.getMessage()));
                }
            });


            return null;
        }
    }

}
