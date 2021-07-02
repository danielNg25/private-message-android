package com.example.helpvnwin.models;
import java.util.List;
import java.util.Vector;


public class User {
    private String id;
    private String username;
    private String imageURL;
    private AES engine;
    private String box;


    public User(String id, String username, String imageURL) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.engine = new AES();
    }
    public User(String username){
        this.username = username;
        this.engine = new AES();
    }

    public User(){
        this.engine = new AES();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public void forward(String message, User receiver){
        String log = String.format("Forward %s from %s to %s", message, this.username, receiver.username);
        receiver.box = message;
        System.err.println(log);
    }
}
