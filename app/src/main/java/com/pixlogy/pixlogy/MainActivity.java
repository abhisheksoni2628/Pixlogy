package com.pixlogy.pixlogy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);

                } catch (Exception ex) {
                    ex.printStackTrace();

                } finally {

                        Intent intent = new Intent(MainActivity.this, Home.class);
                        startActivity(intent);
                        finish();

                    }
                }
        };
        th.start();

    }
}