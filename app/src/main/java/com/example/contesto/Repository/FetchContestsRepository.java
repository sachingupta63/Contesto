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
        fetchApiAsyncTask = new FetchApiAsyncTask(call);

    }

    public LiveData<List<ContestObject>> getContestsListAsync(){
        return fetchApiAsyncTask.getLiveContestsList();
    }

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

        private MutableLiveData<List<ContestObject>> getLiveContestsList () {
            return  liveContestList;
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        protected Void doInBackground(Void... voids) {
            call.enqueue(new Callback<List<ContestObject>>() {
                @Override
                public void onResponse(retrofit2.Call<List<ContestObject>> call, Response<List<ContestObject>> response) {

                    Log.e("APIFETCHEDREPOASYNC>>",response.code()+" ");

                    List<ContestObject> apiResponse = response.body();
                    Log.e("RESPONSE BODY>>",response.body().size()+" ");
                    assert apiResponse != null;
                    liveContestList.postValue(response.body());
//                    liveContestList.postValue(apiResponse.getObjects());
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
