package com.example.helpvnwin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.helpvnwin.R;
import com.example.helpvnwin.models.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameET, emailET, passwordET;
    Button registerBTN;
    TextView loginTV;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");

        auth = FirebaseAuth.getInstance();

        usernameET = findViewById(R.id.et_username_register);
        emailET = findViewById(R.id.et_email_register);
        passwordET = findViewById(R.id.et_password_register);
        registerBTN = findViewById(R.id.btn_register);
        loginTV = findViewById(R.id.tv_login);

        loginTV.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        registerBTN.setOnClickListener(v -> {
            String username = usernameET.getText().toString();
            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();

            if (TextUtils.isEmpty(username) ||TextUtils.isEmpty(email) ||TextUtils.isEmpty(password) ){
                Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            }
            else if (password.length() <6){
                Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            }
            else{
                register(username, email, password);
            }
        });
    }

    private void register(String username, String email, String password){

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        String userID = firebaseUser.getUid();

                        reference = FirebaseDatabase.getInstance(Firebase.DBLINK).getReference("Users").child(userID);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userID);
                        hashMap.put("username", username);
                        hashMap.put("imageURL", "default");

                        reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                    else{

                        Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                    }
                });



    }
}