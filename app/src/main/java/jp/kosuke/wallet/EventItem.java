package jp.kosuke.wallet;

import net.cattaka.util.cathandsgendroid.annotation.DataModel;
import net.cattaka.util.cathandsgendroid.annotation.DataModelAttrs;

/**
 * Created by kosuke on 16/07/10.
 */


@DataModel(find = {"change", "description", "year", "month", "day"},
        unique = {"description"})

public class EventItem {
    @DataModelAttrs(primaryKey = true)

    private int change;
    private int year;
    private int month;
    private int day;
    private String description;


    public int getChange() {
        return change;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getDescription() {
        return description;
    }


    public void setChange(int change) {
        this.change = change;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
