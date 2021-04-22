package com.example.studentadmin.Model;

public class LessonModel {
    private String nameLesson, imageLesson;
    private String Key;



    public LessonModel() {
    }

    public LessonModel(String nameLesson, String imageLesson, String key) {
        this.nameLesson = nameLesson;
        this.imageLesson = imageLesson;
        Key = key;
    }

    public String getNameLesson() {
        return nameLesson;
    }

    public void setNameLesson(String nameLesson) {
        this.nameLesson = nameLesson;
    }

    public String getImageLesson() {
        return imageLesson;
    }

    public void setImageLesson(String imageLesson) {
        this.imageLesson = imageLesson;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

}