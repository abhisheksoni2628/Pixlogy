package com.pixlogy.pixlogy.auth;

import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pixlogy.pixlogy.Home;
import com.pixlogy.pixlogy.R;

public class Signup extends AppCompatActivity {
    RelativeLayout progresslayout;

    TextView loginText;
    private Object signInRequest;
    private  static  final  int RC_SIGN_IN = 123 ;
    private GoogleSignInClient mGoogleSignInClient;
    EditText username;
    EditText email;
    EditText password;
    EditText confirmPassword;
    Button signUpBtn;
    Button googleSignin;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
//
        progresslayout = findViewById(R.id.progress_layout);
        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.et_username);
        email = findViewById(R.id.et_email);
       password = findViewById(R.id.et_password);
       googleSignin = findViewById(R.id.signupWithGoogle);
       confirmPassword = findViewById(R.id.et_confirmPassword);

         loginText = findViewById(R.id.login);
         signUpBtn = findViewById(R.id.signUpBtn);

        loginText.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(Signup.this, Login.class);
                 startActivity(intent);
             }
         });

        requestGoogleSignin();
        googleSignin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });

        signUpBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();

            }
        });





    }
    private void requestGoogleSignin() {
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);



    }



    private void createUser() {

        String em = email.getText().toString();
        String nm = username.getText().toString();
        String pas = password.getText().toString();
        String cp = confirmPassword.getText().toString();

        if(TextUtils.isEmpty(nm)){
            username.setError("username cant be empty");
            username.requestFocus();

        }
        else if(TextUtils.isEmpty( em)){
            email.setError("email cant be empty");
            email.requestFocus();


        }
        else if (TextUtils.isEmpty(pas))
        {
            password.setError("password  cant be empty");
            password.requestFocus();

        }
        else if(!cp.equals(pas)){
            Toast.makeText(Signup.this, "password do not match try again !", Toast.LENGTH_SHORT).show();





        }
        else {
//
//        }
            mAuth.createUserWithEmailAndPassword(em,pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(Signup.this, "SignUp Successfull", Toast.LENGTH_SHORT).show();
                        progresslayout.setVisibility(VISIBLE);
            Thread th = new Thread() {

                @Override
                public void run() {

                    try {
                        sleep(2000);

                    } catch (Exception ex) {
                        ex.printStackTrace();

                    } finally {
                        Intent intent = new Intent( Signup.this , Login.class);
                        startActivity(intent);
                        finish();
                    }

                }
            };


            th.start();

                    }else{
                        Toast.makeText(Signup.this, "Sing Up error ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating user with firebase using received token id
                firebaseAuthWithGoogle(account.getIdToken());




            } catch (ApiException e) {

            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {



        //getting user credentials with the help of AuthCredential method and also passing user Token Id.
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        //trying to sign in user using signInWithCredential and passing above credentials of user.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {



                            // Sign in success, navigate user to Profile Activity
                            Intent intent = new Intent(Signup.this, Home.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Signup.this, "User authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(Signup.this,Home.class));
        }
    }


}
