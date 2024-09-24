package com.example.Chasse.Model;

import java.util.ArrayList;

public class Course {
    private ArrayList<Point> points;
    private ArrayList<Enigma> enigmas;
    public Course() {}

    public ArrayList<Point> getPoints() {
        return points;
    }

    public ArrayList<Enigma> getEnigmas() {
        return enigmas;
    }

    public boolean newPoint(Point point) {
        if (this.points.contains(point)) return false;
        else this.points.add(point); return true;
    }

    public boolean newEnigma(Enigma enigma) {
        if (this.enigmas.contains(enigma)) return false;
        else this.enigmas.add(enigma); return true;
    }
}
