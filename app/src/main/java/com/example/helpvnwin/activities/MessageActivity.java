package com.example.helpvnwin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.helpvnwin.R;
import com.example.helpvnwin.adapters.MessageAdapter;
import com.example.helpvnwin.messageGenerator.Encode_Decode;
import com.example.helpvnwin.models.AES;
import com.example.helpvnwin.models.Chat;
import com.example.helpvnwin.models.Firebase;
import com.example.helpvnwin.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    CircleImageView profile_image;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference reference;
    Intent intent;

    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;
    List<String> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // and thisx
                startActivity(new Intent(MessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent = getIntent();
        String userID = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users = new ArrayList<>();
                String msg = text_send.getText().toString();
                if (!msg.equals("")){
                    msg = AES.encrypt(msg, AES.key);
                    getListUsers( userID);
                    addStringID(3, userID);

                    users.add(0, userID);
                    msg = Encode_Decode.encrypt(msg, AES.key, users.toArray(new String[0]));
                    Log.d("nameUser", String.valueOf(users.toArray(new String[0]).length));
                    for (int i = 0 ; i <users.size(); i++){
                        Log.d("userID", users.get(i));
                    }


                    sendMessage(fuser.getUid(), users.get(users.size()-1), msg);
                }
                else{
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });


        reference = FirebaseDatabase.getInstance(Firebase.DBLINK).getReference("Users").child(userID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_image);
                }
                readMessages(fuser.getUid(), userID, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance(Firebase.DBLINK).getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);


        reference.child("Chats").push().setValue(hashMap);
    }

    private void updateMessage(Chat chat, String key){
        DatabaseReference reference = FirebaseDatabase.getInstance(Firebase.DBLINK).getReference().child("Chats").child(key);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", chat.getSender());
        hashMap.put("receiver", chat.getReceiver());
        hashMap.put("message", chat.getMessage());
        reference.updateChildren(hashMap);
    }

    private void readMessages(String myid, String userid, String imageuri){
        mchat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance(Firebase.DBLINK).getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mchat.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String chatID = dataSnapshot.getKey();
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid)&&chat.getSender().equals(userid)|| chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        String[] decrypt = Encode_Decode.decrypt(chat.getMessage(), AES.key);
                        if(!chat.getMessage().equals("Ban duoc chuyen tiep mot tin nhan!")){
                            if (chat.getReceiver().equals(myid)){
                                if (decrypt[1].equals("end")){
                                    chat.setMessage(AES.decrypt(chat.getMessage(), AES.key));
                                }
                                else{
                                    chat.setMessage("Ban duoc chuyen tiep mot tin nhan!");

                                        sendMessage(myid, decrypt[1], decrypt[0]);
                                        updateMessage(chat, chatID);
                                }


                            }
                            if (chat.getSender().equals(myid)){
                                chat.setMessage("Ban duoc chuyen tiep mot tin nhan!");
                            }
                        }


                        mchat.add(chat);
                    }
                }
                messageAdapter = new MessageAdapter(MessageActivity.this, mchat, imageuri);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void status(String status){
        reference = FirebaseDatabase.getInstance(Firebase.DBLINK).getReference("Users").child(fuser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
    private void getListUsers(String userReceiverID){
        ArrayList<String> userListID = new ArrayList<>();
        reference = FirebaseDatabase.getInstance(Firebase.DBLINK).getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AES.userList = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    User user = dataSnapshot.getValue(User.class);
                    String userIDString = user.getId();
                    if (!userIDString.equals(userReceiverID) && !userIDString.equals(fuser.getUid())){
                        AES.userList.add(user);
                        Log.d("size1", String.valueOf(AES.userList.size()));
                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void addStringID(int userNum, String userReceiverID){
        users.add("0507jrtGi0Z39aVDY8dzXb7A8qd2");
        users.add("0yi2NojEy3PhiWul1VUiXNvanYw1");
        users.add("2xy4ULumBZgU3m7Tz4sBr65E3wn1");
        users.add("43HuVD7XbXRqFKfBSiK5zG5SvIM2");
        users.add("dI0mw8WNN1XEWt6Qi8s15tDPSYU2");
        users.add("fo3lAu0mhOWrxcizpjDoiU5Trik2");
        users.add("hhxXzZl1qKgP66fusguciqc83jo1");
        for (int i = 0; i< users.size(); i ++){
            if (users.get(i).equals(fuser.getUid()) || users.get(i).equals(userReceiverID)){
                users.remove(i);
            }
        }
        while(users.size() > userNum){
            Random ran = new Random();
            int index = ran.nextInt(users.size()) +0;
            users.remove(index);

        }

    }
}