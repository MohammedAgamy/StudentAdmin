package com.example.studentadmin.Model;

public class ResultModel {
    private String name;
    private int score, total;

    public ResultModel() {
    }

    public ResultModel(String name, int score, int total) {
        this.name = name;
        this.score = score;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
