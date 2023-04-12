package com.hc.hicareservices.data.model.weeks;

import org.joda.time.DateTime;

public class WeekModel {
    public String days;

    public String date;


    public String getDays() {

        return days;
    }

    public DateTime dateTime;

    public void setDays(String days) {
        this.days = days;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
