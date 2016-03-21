package com.freeoda.pharmacist.thepharmacist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Lakna on 3/17/2016.
 */
public class SplashscreenActivity  extends Activity {
    private long splashDelay = 3000;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        /*
         * this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
         * WindowManager.LayoutParams.FLAG_FULLSCREEN);
        */

        setContentView(R.layout.activity_splashscreen);
        imgView = (ImageView)findViewById(R.id.splash_logo);

//        final Animation animation = new AlphaAnimation(1, 0);
//        animation.setDuration(1000);
//        animation.setInterpolator(new LinearInterpolator());
//        animation.setRepeatCount(Animation.INFINITE);
//        animation.setRepeatMode(Animation.REVERSE);
//        imgView.startAnimation(animation);
//
//
//        imgView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //startAnimations();
//                Intent intent = new Intent(SplashscreenActivity.this, LoginActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
//
//            }
//        }, 5000);


        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashscreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }




    private void startAnimations(){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash_logo);
        iv.clearAnimation();
        iv.startAnimation(anim);
    }


}


