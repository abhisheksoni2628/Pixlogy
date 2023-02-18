package com.pixlogy.pixlogy.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pixlogy.pixlogy.R;

public class phoneNum extends AppCompatActivity {
    Button Phone;
    EditText phonenumber;


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
                }
            });
        }
    }
}