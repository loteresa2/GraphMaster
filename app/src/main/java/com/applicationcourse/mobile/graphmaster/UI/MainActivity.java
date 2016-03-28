package com.applicationcourse.mobile.graphmaster.UI;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.applicationcourse.mobile.graphmaster.R;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Intent intent = new Intent(MainActivity.this, Explosion.class);
            startActivity(intent);
            //Intent intent = new Intent(MainActivity.this, LevelActivity.class);
            //startActivity(intent);
        }

    }

}