package com.example.earthquakes.api;

public class Geometry {
    private double[] coordinates;

    public double getLongitude(){
        return coordinates[0];
    }

    public double getLatitude(){
        return coordinates[1];
    }
}
