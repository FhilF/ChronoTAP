package com.switso.itap;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView splashScreenLogo = (ImageView) findViewById(R.id.SplashScreenLogo);
        Picasso.with(this).load(R.drawable.itaplogo).into(splashScreenLogo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent myIntent = new Intent(SplashScreen.this, LoginActivity.class);
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(SplashScreen.this, android.R.anim.fade_in, android.R.anim.fade_out);
                SplashScreen.this.startActivity(myIntent, options.toBundle());

            }
        }, 2 * 1000);
    }
}
