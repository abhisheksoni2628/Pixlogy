package com.pixlogy.pixlogy.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pixlogy.pixlogy.Home;
import com.pixlogy.pixlogy.R;

public class Signup extends Activity {

    TextView loginText;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

         loginText = findViewById(R.id.login);
         signUpBtn = findViewById(R.id.signUpBtn);

        loginText.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(Signup.this, Login.class);
                 startActivity(intent);
             }
         });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, Home.class);
                startActivity(intent);
            }
        });



    }
}