package com.example.studentadmin.Model;

public class LessonModel {
    private String nameLesson;
    private String Key;



    public LessonModel() {
    }

    public LessonModel(String nameLesson,String key) {
        this.nameLesson = nameLesson;
        Key = key;
    }

    public String getNameLesson() {
        return nameLesson;
    }

    public void setNameLesson(String nameLesson) {
        this.nameLesson = nameLesson;
    }
    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

}