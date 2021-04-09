package com.example.studentadmin.Model;

import android.net.Uri;

public class CommentModel {
    private long Comment_id;
    private String Name;
    private String Comment;
    private Uri Image;
    private String Time;
    private String Counter;

    public CommentModel() {
    }

    public CommentModel(long comment_id, String name, String comment, Uri image, String time, String counter) {
        Comment_id = comment_id;
        Name = name;
        Comment = comment;
        Image = image;
        Time = time;
        Counter = counter;
    }

    public long getComment_id() {
        return Comment_id;
    }

    public void setComment_id(long comment_id) {
        Comment_id = comment_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public Uri getImage() {
        return Image;
    }

    public void setImage(Uri image) {
        Image = image;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }
}