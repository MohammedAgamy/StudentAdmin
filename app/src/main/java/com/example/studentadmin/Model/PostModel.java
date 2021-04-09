package com.example.studentadmin.Model;


import android.net.Uri;

//  Model to send data post to fireStore .........................
public class  PostModel
{
    private long Post_id;
    private String Name ;
    private String Post ;
    private Uri Image ;
    private String Time ;
    private String Counter ;
    private long like ;


    public PostModel() {
    }

    public PostModel(long postid, String name, String post, Uri image, String time , String Counter ,long like) {
        Post_id = postid;
        Name = name;
        Post = post;
        Image = image;
        Time = time;
        this.like=like ;
        this.Counter =Counter ;
    }

    public long getLike() {
        return like;
    }

    public void setLike(long like) {
        this.like = like;
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public long getPost_id() {
        return Post_id;
    }

    public String getName() {
        return Name;
    }

    public String getPost() {
        return Post;
    }

    public Uri getImage() {
        return Image;
    }

    public String getTime() {
        return Time;
    }

    public void setPost_id(long post_id) {
        Post_id = post_id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPost(String post) {
        Post = post;
    }

    public void setImage(Uri image) {
        Image = image;
    }

    public void setTime(String time) {
        Time = time;
    }
}
