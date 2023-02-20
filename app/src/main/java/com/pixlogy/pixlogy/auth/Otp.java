package com.pixlogy.pixlogy.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.pixlogy.pixlogy.Home;
import com.pixlogy.pixlogy.R;

public class Otp extends AppCompatActivity {
    TextView phone, resendOtp ;
    EditText otp;
    Button verfiyOtp;
    ProgressBar progressBar;

    String VerficationID;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.theme));
            phone = findViewById(R.id.phoneNUm);
            otp = findViewById(R.id.et_otp);
            verfiyOtp = findViewById(R.id.btn_verify);
            resendOtp = findViewById(R.id.tv_resend);
            progressBar = findViewById(R.id.progressbar);
            phone.setText("+91" + getIntent().getStringExtra("phone"));
            VerficationID = getIntent().getStringExtra("verficationId");
            resendOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(Otp.this, "otp resend successfully ", Toast.LENGTH_SHORT).show();
                }
            });
            verfiyOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    verfiyOtp.setVisibility(View.INVISIBLE);
                    if(otp.getText().toString().trim().isEmpty()){
                        otp.setError("otp ccant be empty");
                        otp.requestFocus();
                    }
                    else{
                        if(VerficationID!= null){
                            String code = otp.getText().toString().trim();
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerficationID, code);
                            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                 if (task.isSuccessful()){
                                     Intent intent = new Intent(Otp.this , Home.class);
                                     intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                     progressBar.setVisibility(View.VISIBLE);
                                     verfiyOtp.setVisibility(View.INVISIBLE);

                                     startActivity(intent);

                                 }
                                 else{
                                     progressBar.setVisibility(View.GONE);
                                     verfiyOtp.setVisibility(View.VISIBLE);
                                     Toast.makeText(Otp.this, "otp not valid", Toast.LENGTH_SHORT).show();

                                 }                                }
                            });
                        }

                    }
                }
            });




        }
    }
}