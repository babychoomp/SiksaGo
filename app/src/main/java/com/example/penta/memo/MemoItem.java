package com.example.penta.memo;

public class MemoItem {
    public static int _id;
    public String title;
    public String description;
    public String type;
    public String time;
    public String date;


    public MemoItem(int _id, String title, String description, String type, String time, String date) {
        this._id = _id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.time = time;
        this.date = date;
    }


    public MemoItem() {

    }


    public int get_Id() {
        return _id;
    }

    public void set_Id(int id) {
        this._id = id;
    }

    public String getMemoTitle() {
        return title;
    }

    public void setMemoTitle(String korean) {
        this.title = title;
    }

    public String getMemoDescription() {
        return description;
    }

    public void setMemoDescription(String grams) {
        this.description = description;
    }

    public String getMemoType() {
        return type;
    }

    public void setMemoType(String calories) {
        this.type = type;
    }

    public String getMemoTime() {
        return time;
    }

    public void setMemoTime(String carbs) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
