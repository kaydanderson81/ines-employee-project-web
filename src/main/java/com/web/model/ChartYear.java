package com.web.model;


import java.util.List;

public class ChartYear {
    private String year;
    private List<ChartYear> yearList;

    public ChartYear() {
        super();
    }

    public ChartYear(String year) {
        this.year = year;
    }

    public ChartYear(List<ChartYear> yearList) {
        this.yearList = yearList;
    }

    public ChartYear(String year, List<ChartYear> yearList) {
        this.year = year;
        this.yearList = yearList;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<ChartYear> getYearList() {
        return yearList;
    }

    public void setYearList(List<ChartYear> yearList) {
        this.yearList = yearList;
    }

    @Override
    public String toString() {
        return "ChartYear{" +
                "year='" + year + '\'' +
                ", yearList=" + yearList +
                '}';
    }
}
