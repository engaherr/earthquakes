package com.example.earthquakes.earthquakes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.example.earthquakes.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.eqRecycler.setLayoutManager(new LinearLayoutManager(this));

        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        EarthquakeAdapter adapter = new EarthquakeAdapter();
        binding.eqRecycler.setAdapter(adapter);

        viewModel.getEqList().observe(this, earthquakesList -> {
            for(Earthquake eq : earthquakesList) {
                Log.d("eq", eq.getMagnitude() + " " + eq.getPlace());
            }
            adapter.submitList(earthquakesList);
        });
        viewModel.getEarthquakes();
    }
}