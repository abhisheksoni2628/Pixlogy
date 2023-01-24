package com.pixlogy.pixlogy.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pixlogy.pixlogy.Home;
import com.pixlogy.pixlogy.R;

public class Login extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText Email;
    EditText password;

    TextView signup;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.theme));
        }
        mAuth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);


        signup = findViewById(R.id.newUserSignup);
        loginBtn = findViewById(R.id.loginBtn);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  loginuser();
            }
        });


    }
    private void loginuser() {
        String em = Email.getText().toString();

        String pas = password.getText().toString();

        if (TextUtils.isEmpty(em)) {
            Email.setError("email cant be empty");
            Email.requestFocus();


        } else if (TextUtils.isEmpty(pas)) {
            password.setError("password cant be empty");
            password.requestFocus();


        } else {
            mAuth.signInWithEmailAndPassword(em, pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this, "Log in Successfull", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, Home.class));
                    } else {
                        Toast.makeText(Login.this, "Sing in  error ", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}