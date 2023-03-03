package com.pixlogy.pixlogy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.pixlogy.pixlogy.auth.Signup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.theme));
        }



        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);

                } catch (Exception ex) {
                    ex.printStackTrace();

                } finally {

                        Intent intent = new Intent(MainActivity.this, Signup.class);
                        startActivity(intent);
                        finish();

                    }
                }
        };
        th.start();

    }
}