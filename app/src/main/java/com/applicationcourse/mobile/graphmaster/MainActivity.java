package com.applicationcourse.mobile.graphmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by teresa on 15/02/16.
 */
public class MainActivity extends Activity {

    List<CGQuestion> quesList;
    int score=0;
    int qid=0;
    CGQuestion currentQ;
    TextView txtQuestion;
    TextView txtClue;
    RadioButton rda, rdb, rdc;
    Button butNext;
    Button btnSubmit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRead = (Button) findViewById(R.id.btnRead);
        Button btnCreate = (Button) findViewById(R.id.btnCreate);
        Button btnOwn = (Button) findViewById(R.id.btnOwn);
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Not yet implemented!",Toast.LENGTH_SHORT).show();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReadingActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        btnOwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Not yet implemented!",Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}