package com.example.earthquakes.main;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.earthquakes.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MainViewModel viewModel = new ViewModelProvider(this,
                new MainViewModelFactory(getApplication()))
                .get(MainViewModel.class);
        binding.eqRecycler.setLayoutManager(new LinearLayoutManager(this));
        EarthquakeAdapter adapter = new EarthquakeAdapter();
        adapter.setOnItemClickListener(earthquake ->
                Toast.makeText(MainActivity.this,
                        earthquake.getPlace(),
                        Toast.LENGTH_SHORT).show());
        binding.eqRecycler.setAdapter(adapter);
        viewModel.downloadEarthquakes();
        viewModel.getEqList().observe(this, adapter::submitList);

    }
}