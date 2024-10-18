package me.katze.powerantirelog.data;


import java.time.LocalTime;

public class CooldownData {
    private LocalTime time;
    private String item;

    public CooldownData(LocalTime time, String item) {
        this.time = time;
        this.item = item;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

}
