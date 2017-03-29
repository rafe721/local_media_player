package com.gainxposure.local.models;

/**
 * Created by Rahul on 26/02/2017.
 */

public class Schedule {

    private String day;

    private String start_time;

    private String end_time;

    /* constructors */
    public Schedule() {

    }

    public Schedule(String day, String start_time, String end_time) {
        this.day = day;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    /* Getters and Setters */

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
