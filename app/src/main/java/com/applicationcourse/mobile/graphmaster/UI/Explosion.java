package com.applicationcourse.mobile.graphmaster.UI;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.applicationcourse.mobile.graphmaster.R;

import tyrantgit.explosionfield.ExplosionField;

public class Explosion extends AppCompatActivity {
    private ExplosionField mExplosionField;
    private CountDownTimer countDownTimer;
    private final long startTimer = 1100;

    //private Button test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_explosion);
        mExplosionField = ExplosionField.attach2Window(this);
        addListener(findViewById(R.id.root));
    }

    private void addListener(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                addListener(parent.getChildAt(i));
            }
        } else {
            root.setClickable(true);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExplosionField.explode(v);
                    v.setOnClickListener(null);
                    //timer

                    countDownTimer = new CountDownTimer(startTimer,startTimer) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            Intent intent = new Intent(Explosion.this, LevelActivity.class);
                            startActivity(intent);
                        }
                    }.start();

                }
            });
        }
    }
}
