package com.pixlogy.pixlogy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.pixlogy.pixlogy.auth.Login;

public class Home extends AppCompatActivity {
    Button Signout;
    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    RelativeLayout imgStegnographyBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.theme));
            mAuth = FirebaseAuth.getInstance();
            Signout = findViewById(R.id.signout);
            Signout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth.signOut();

                    requestgooglesignout();




                    startActivity(new Intent(Home.this, Login.class));
                    Toast.makeText(Home.this, "Log out successful", Toast.LENGTH_SHORT).show();

                }
            });


       }

        imgStegnographyBtn = findViewById(R.id.imgStegnographyBtn);
        imgStegnographyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, ImageStegnography.class));
            }
        });





        }

    private void requestgooglesignout() {
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        googleSignInClient.revokeAccess().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // User has been signed out and access revoked
                    // Clear any user data stored in your app
                }
            }
        });


    }
}
