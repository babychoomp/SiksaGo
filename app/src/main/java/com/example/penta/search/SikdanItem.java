package com.example.penta.search;

public class SikdanItem {
    public int _id;
    public String korean;
    public String grams;
    public String calories;
    public String carbs;
    public String proteins;
    public String fats;
    public String date;


    public SikdanItem(int _id, String korean, String grams, String calories, String carbs, String proteins, String fats, String date) {
        this._id = _id;
        this.korean = korean;
        this.grams = grams;
        this.calories = calories;
        this.carbs = carbs;
        this.proteins = proteins;
        this.fats = fats;
        this.date = date;
    }


    public SikdanItem() {

    }
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getKorean() {
        return korean;
    }

    public void setKorean(String korean) {
        this.korean = korean;
    }

    public String getGrams() {
        return grams;
    }

    public void setGrams(String grams) {
        this.grams = grams;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getProteins() {
        return proteins;
    }

    public void setProteins(String proteins) {
        this.proteins = proteins;
    }

    public String getFats() {
        return fats;
    }

    public void setFats(String fats) {
        this.fats = fats;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
