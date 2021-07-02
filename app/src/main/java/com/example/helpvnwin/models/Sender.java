package com.example.helpvnwin.models;


import java.util.List;
import java.util.Vector;


public class Sender extends Account{
    
    private List<String> listOfKeys  = new Vector<String>();

    public Sender() {
        super();
    }
    public Sender(String id, String username, String imageURL) {
        super(id, username, imageURL);
    }    
    
    public String compress(String message){
        listOfKeys.add("f3key");
        listOfKeys.add("f2key");
        listOfKeys.add("f1key");
        String cipher = message;
        for(String key: listOfKeys){
            cipher = engine.encrypt(cipher, key);
        }
        return cipher;
    }
    
    public void send(String message, Account F){
        String cipher = compress(message);
        
        String log = String.format("Send %s from %s to %s", cipher, this.username, F.username);
        
        F.box  =  cipher;
        System.out.println(log);
    }
    
}
