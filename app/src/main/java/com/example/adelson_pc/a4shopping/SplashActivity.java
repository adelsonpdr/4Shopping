package com.example.adelson_pc.a4shopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLAH_TIME_OUT = 3000;

    FirebaseAuth firebaseAuth;
    ImageView img_animation;
    TextView welcome;
    TextView slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TranslateAnimation animation1 = new TranslateAnimation(400.0f, 0.0f,
                0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation1.setDuration(2000);  // animation duration
        animation1.setRepeatCount(0);  // animation repeat count
        animation1.setRepeatMode(0);   // repeat animation (left to right, right to left )
        //animation.setFillAfter(true);

        TranslateAnimation animation2 = new TranslateAnimation(-400.0f, 0.0f,
                0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation2.setDuration(2000);  // animation duration
        animation2.setRepeatCount(0);  // animation repeat count
        animation2.setRepeatMode(0);   // repeat animation (left to right, right to left )
        //animation.setFillAfter(true);

        img_animation = (ImageView) findViewById(R.id.imageView);
        welcome = (TextView) findViewById(R.id.welcome);
        slogan = (TextView) findViewById(R.id.slogan);

        img_animation.startAnimation(animation1);
        welcome.startAnimation(animation2);
        slogan.startAnimation(animation2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logUser();
            }
        }, SPLAH_TIME_OUT);
    }

    protected void onStart() {
        super.onStart();
        firebaseAuth = Connection.getFirebaseAuth();
    }

    private void logUser() {
        SharedPreferences pref = getApplicationContext()
                .getSharedPreferences("Save user credentials", 0);
        String email = pref.getString("email", null);
        String pass = pref.getString("password", null);
        if (email != null || pass != null) {
            login(email, pass);
        } else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).
                addOnCompleteListener(SplashActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(SplashActivity.this, Lists.class);
                            startActivity(intent);
                            finish();
                        } else {
                            alert("Checar conexão com Internet");
                        }
                    }
                });

    }

    private void alert(String msg) {
        Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
