package com.applicationcourse.mobile.graphmaster.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.applicationcourse.mobile.graphmaster.Database.DatabaseHandler;
import com.applicationcourse.mobile.graphmaster.Database.Help;
import com.applicationcourse.mobile.graphmaster.R;

public class ShowTextActivity extends AppCompatActivity {
    private TextView textView;
    private String test= "this is only a test text.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text);

        textView = (TextView)findViewById(R.id.textView);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int mid = extras.getInt("mid");
            int subid = extras.getInt("subid");
            //String subid = extras.getString("subid");
            Help help = new Help();
            help = DatabaseHandler.getTextHelp(mid,subid);
            textView.setText(help.getValue());
        }


    }
}
