package com.example.helpvnwin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.helpvnwin.R;
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

public class NewMessageActivity extends AppCompatActivity {

    EditText usernameET;
    Button newMessageBTN;
    ArrayList<User> users = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Message");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usernameET = findViewById(R.id.new_message_username_et);
        newMessageBTN = findViewById(R.id.new_message_btn);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        DatabaseReference reference = FirebaseDatabase.getInstance(Firebase.DBLINK).getReference("Users");
        newMessageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameET.getText().toString();
                reference.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            User user = dataSnapshot.getValue(User.class);

                            assert  user != null;
                            assert  firebaseUser != null;
                            if (!user.getId().equals(firebaseUser.getUid())){
                                users.add(user);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                boolean isExist = false;
                String chosenUserID = null;
                for (User user: users){
                    if (user.getUsername().equals(username)){
                        Log.d("USERNAME: ", user.getUsername());
                        Log.d("USERNAMEET: ", username);
                        chosenUserID = user.getId();
                        isExist = true;
                        break;
                    }
                }

                if (isExist){

                    Intent intent = new Intent(NewMessageActivity.this, MessageActivity.class);
                    intent.putExtra("userid", chosenUserID);
                    startActivity(intent);
                }
                else{

                }
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}