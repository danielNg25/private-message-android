package com.example.helpvnwin.models;

public class run {
      
    public static void main(String[] args) {
        Sender Park  = new Sender("Park");
        Sender Lee = new Sender("Lee");
        Account F1 = new User("F1");
        Account F2 = new User("F2");
        Account F3 = new User("F3");
        
        Park.send("Hello", F1);
        
        String decryptedf1 = F1.engine.decrypt(F1.box, "f1key");
        F1.forward(decryptedf1, F2);
        String decryptedf2 = F2.engine.decrypt(F2.box, "f2key");
        F2.forward(decryptedf2, F3);
        
        String decryptedf3 = F3.engine.decrypt(decryptedf2, "f3key");
        F3.forward(decryptedf3, Lee);
        
        
        
        
        
    }
    
}
