package com.over.parkulting.object;

public class GeoPoint {
    private String name;
    private double h;
    private double d;
    private boolean posit;

    public GeoPoint(String name, double h, double d, boolean posit) {
        this.name = name;
        this.h = h;
        this.d = d;
        this.posit = posit;
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

    public boolean isPosit() { return posit; }
}
