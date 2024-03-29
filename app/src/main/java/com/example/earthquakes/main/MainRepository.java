package com.example.earthquakes.main;

import androidx.lifecycle.LiveData;

import com.example.earthquakes.Earthquake;
import com.example.earthquakes.api.ApiClient;
import com.example.earthquakes.api.EarthquakeJSONResponse;
import com.example.earthquakes.api.Feature;
import com.example.earthquakes.database.EarthquakeDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {

    private final EarthquakeDatabase database;

    public MainRepository(EarthquakeDatabase database) {
        this.database = database;
    }

    public LiveData<List<Earthquake>> getEarthquakesList(){
        return database.earthquakeDAO().getEarthquakes();
    }

    public void downloadAndSaveEarthquakes() {
        ApiClient.Service service = ApiClient.getInstance().getService();

        service.getEarthquakes().enqueue(new Callback<EarthquakeJSONResponse>() {
            @Override
            public void onResponse(Call<EarthquakeJSONResponse> call, Response<EarthquakeJSONResponse> response) {
                List<Earthquake> earthquakeList = getEarthquakesWithMoshi(response.body());

                EarthquakeDatabase.databaseWriteExecutor.execute(()-> {
                    database.earthquakeDAO().insertAll(earthquakeList);
                });
            }

            @Override
            public void onFailure(Call<EarthquakeJSONResponse> call, Throwable t) {

            }
        });
    }

    public interface DownloadEqsListener {
        void onEqsDownloaded(List<Earthquake> eqList);
    }

    private List<Earthquake> getEarthquakesWithMoshi(EarthquakeJSONResponse body) {
        ArrayList<Earthquake> eqList = new ArrayList<>();

        List<Feature> features = body.getFeatures();
        for(Feature feature: features) {
            String id = feature.getId();
            double magnitude = feature.getProperties().getMagnitude();
            String place = feature.getProperties().getPlace();
            long time = feature.getProperties().getTime();

            double longitude = feature.getGeometry().getLongitude();
            double latitude = feature.getGeometry().getLatitude();

            Earthquake earthquake = new Earthquake(id,place,magnitude,time,longitude,latitude);
            eqList.add(earthquake);
        }

        return eqList;
    }

    public void getEarthquakes(DownloadEqsListener downloadEqsListener) {
        ApiClient.Service service = ApiClient.getInstance().getService();

        service.getEarthquakes().enqueue(new Callback<EarthquakeJSONResponse>() {
            @Override
            public void onResponse(Call<EarthquakeJSONResponse> call,
                                   Response<EarthquakeJSONResponse> response) {
                List<Earthquake> earthquakeList = getEarthquakesWithMoshi(response.body());
                downloadEqsListener.onEqsDownloaded(earthquakeList);
            }

            @Override
            public void onFailure(Call<EarthquakeJSONResponse> call, Throwable t) {
                return;
            }
        });
    }
}
