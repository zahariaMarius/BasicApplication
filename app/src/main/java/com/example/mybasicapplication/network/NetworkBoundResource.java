package com.example.mybasicapplication.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;


import com.example.mybasicapplication.data.Resource;

import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class NetworkBoundResource<ResultType, RequestType> {
    private static final String TAG = "NetworkBoundResource";

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();


    @MainThread
    public NetworkBoundResource() {
        result.setValue(Resource.loading(null));
        Log.d(TAG, "NetworkBoundResource: sono nel costruttore");
        LiveData<ResultType> dbSource = loadFromDb();
        Log.d(TAG, "NetworkBoundResource: " + dbSource);
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                Log.d(TAG, "NetworkBoundResource: fetch is true");
                fetchFromNetwork(dbSource);
            } else {
                Log.d(TAG, "NetworkBoundResource: fetch is false");
                result.addSource(dbSource, newData -> {
                    result.setValue(Resource.success(newData));
                });
            }
        });
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        Log.d(TAG, "fetchFromInternet: prendo da internet");
        result.addSource(dbSource, newData -> result.setValue(Resource.loading(newData)));
        createCall().enqueue(new Callback<RequestType>() {
            @Override
            public void onResponse(Call<RequestType> call, Response<RequestType> response) {
                result.removeSource(dbSource);
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.body());
                    Executors.newSingleThreadExecutor().execute(() -> {
                        saveCallResult(response.body());
                        new Handler(Looper.getMainLooper()).post(() -> result.addSource(loadFromDb(), newData -> result.setValue(Resource.success(newData))));
                    });
                } else {
                    result.addSource(dbSource, error -> {
                        result.setValue(Resource.error("error message", null));
                    });
                }
            }

            @Override
            public void onFailure(Call<RequestType> call, Throwable t) {
                result.removeSource(dbSource);
                result.addSource(dbSource, error -> result.setValue(Resource.error("error message", null)));
            }
        });

    }

    // Called to create the API call.
    @MainThread
    protected abstract Call<RequestType> createCall();

    // Called to save data from API to database
    @WorkerThread
    protected abstract void saveCallResult(RequestType data);

    // Called to get the cached data from the database.
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    public LiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }
}
