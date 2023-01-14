package com.example.mystudents.struct;

import java.util.ArrayList;

public class TimeTableStruct {

    String day;
    ArrayList<String> periods;

    public TimeTableStruct() {
    }

    public TimeTableStruct(String day, ArrayList<String> periods) {
        this.day = day;
        this.periods = periods;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<String> getPeriods() {
        return periods;
    }

    public void setPeriods(ArrayList<String> periods) {
        this.periods = periods;
    }

}
