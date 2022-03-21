package com.over.parkulting.object;

public class GeoPoint {
    private String name;
    private double h;
    private double d;

    public GeoPoint(String name, double h, double d) {
        this.name = name;
        this.h = h;
        this.d = d;
    }

    public String getName() {
        return name;
    }

    public double getH() {
        return h;
    }

    public double getD() {
        return d;
    }
}
