
package aes;

import java.util.List;
import java.util.Vector;


public class Account {
    String name;
    AES engine;
    String box;
    
    public Account() {
        this.engine = new AES();
        this.box = new String();
     }
    public Account(String name){
        this.engine = new AES();
        this.name = name;    
        this.box = new String();
    }
    
    public void forward(String message, Account receiver){
        String log = String.format("Forward %s from %s to %s", message, this.name, receiver.name);
        receiver.box = message;
        System.err.println(log);
    }
    
    
}
