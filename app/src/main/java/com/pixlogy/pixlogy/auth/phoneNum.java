package com.pixlogy.pixlogy.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.pixlogy.pixlogy.R;

import java.util.concurrent.TimeUnit;

public class phoneNum extends AppCompatActivity {
    Button Phone;
    EditText phonenumber;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_num);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.theme));
            Phone = findViewById(R.id.loginBtn);
            phonenumber = findViewById(R.id.et_phone);
            progressBar = findViewById(R.id.progressbar);
            mAuth = FirebaseAuth.getInstance();


            ImageView gifImageView = findViewById(R.id.gifImageView);
            Glide.with(this).load(R.drawable.otpanim).into(gifImageView);

            Phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (phonenumber.getText().toString().trim().isEmpty()){
                        phonenumber.setError(" Phone number Invalid");
                        phonenumber.requestFocus();
                    }
                    else if (phonenumber.getText().toString().trim().length() <10){
                        phonenumber.setError(" Phone number Invalid");
                        phonenumber.requestFocus();

                    }
                    else{
                        sendOtp();

                    }
                }
            });
        }
    }

    private void sendOtp() {



            progressBar.setVisibility(View.VISIBLE);
            Phone.setVisibility(View.INVISIBLE);


            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {




                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {

                    progressBar.setVisibility(View.GONE);

                    Phone.setVisibility(View.VISIBLE);
                    Toast.makeText(phoneNum.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();




                }

                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    progressBar.setVisibility(View.GONE);
                    Phone.setVisibility(View.VISIBLE);
                    Intent i = new Intent(phoneNum.this,Otp.class);
                    i.putExtra("phone",phonenumber.getText().toString().trim());
                    i.putExtra("verficationId",verificationId);
                    startActivity(i);


                }
            };
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber("+91"+phonenumber.getText().toString())       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);

        }
    }


