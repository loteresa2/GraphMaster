package com.applicationcourse.mobile.graphmaster.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.applicationcourse.mobile.graphmaster.Database.DatabaseHandler;
import com.applicationcourse.mobile.graphmaster.Database.MainQues;
import com.applicationcourse.mobile.graphmaster.Database.MainQuesHData;
import com.applicationcourse.mobile.graphmaster.Database.MainQuesHeading;
import com.applicationcourse.mobile.graphmaster.Database.Options;
import com.applicationcourse.mobile.graphmaster.Database.SubQuestion;
import com.applicationcourse.mobile.graphmaster.R;

/**
 * Created by teresa on 15/02/16.
 */
public class MainActivity extends Activity {
    private DatabaseHandler databaseHandler;
    private static Context mContext = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        //initialization
        databaseHandler = DatabaseHandler.getHandler(mContext);
        DatabaseHandler.deleteDatabase();
        Button btnRead = (Button) findViewById(R.id.btnRead);
        Button btnCreate = (Button) findViewById(R.id.btnCreate);
        Button btnOwn = (Button) findViewById(R.id.btnOwn);
        Button LoadDB = (Button)findViewById(R.id.btnLoadDB);
        LoadDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadDatabase(mContext);
            }
        });
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
    public void LoadDatabase(Context context){

        //add data
        //Main Question
        MainQues mainQues = new MainQues();
        mainQues.setQuestion("Alice is very fond of growing plants. She measures the height of her plant as shown");
        mainQues.setFunction("Create");
        mainQues.setType("Line");
        databaseHandler.addQuestionValue(mainQues);
        //subquestion
        SubQuestion subQuestion = new SubQuestion();
        subQuestion.setFunction("Create");
        subQuestion.setType("Line");
        subQuestion.setSubQuestion("Which type of graph are you going to draw for the given data?");
        databaseHandler.addSubQValue(subQuestion);
        SubQuestion subQuestion1 = new SubQuestion();
        subQuestion1.setFunction("Create");
        subQuestion1.setType("Line");
        subQuestion1.setSubQuestion("What kind of data are you comparing ?");
        databaseHandler.addSubQValue(subQuestion1);
        //option
        Options value = new Options();
        value.setMqId(1);
        value.setSubQuesId(1);
        value.setOptionValue("LINE OR SCATTER");
        value.setAnswer("T");
        value.setExplanation("Great Work! Correct Answer!");
        databaseHandler.addOption(value);

        Options value1 = new Options();
        value1.setMqId(1);
        value1.setSubQuesId(1);
        value1.setOptionValue("BAR OR PIE");
        value1.setAnswer("F");
        value1.setExplanation("It cannot be this answer because...");
        databaseHandler.addOption(value1);

        //option
        value = new Options();
        value.setMqId(1);
        value.setSubQuesId(2);
        value.setOptionValue("NUMBER VS NUMBER");
        value.setAnswer("T");
        value.setExplanation("Great Work! Correct Answer!");
        databaseHandler.addOption(value);

        value1 = new Options();
        value1.setMqId(1);
        value1.setSubQuesId(2);
        value1.setOptionValue("NUMBER VS CATEGORY");
        value1.setAnswer("F");
        value1.setExplanation("It cannot be this answer because...");
        databaseHandler.addOption(value1);

        //subquestion
        subQuestion = new SubQuestion();
        subQuestion.setFunction("Create");
        subQuestion.setType("Line");
        subQuestion.setSubQuestion("What will be in you x-axis?");
        databaseHandler.addSubQValue(subQuestion);
        subQuestion1 = new SubQuestion();
        subQuestion1.setFunction("Create");
        subQuestion1.setType("Line");
        subQuestion1.setSubQuestion("What will be in your y-axis ?");
        databaseHandler.addSubQValue(subQuestion1);
        //option
         value = new Options();
        value.setMqId(1);
        value.setSubQuesId(3);
        value.setOptionValue("Water");
        value.setAnswer("T");
        value.setExplanation("Great Work! Correct Answer!");
        databaseHandler.addOption(value);

        value1 = new Options();
        value1.setMqId(1);
        value1.setSubQuesId(3);
        value1.setOptionValue("Height");
        value1.setAnswer("F");
        value1.setExplanation("It cannot be this answer because...");
        databaseHandler.addOption(value1);
        //option
        value = new Options();
        value.setMqId(1);
        value.setSubQuesId(4);
        value.setOptionValue("Height");
        value.setAnswer("T");
        value.setExplanation("Great Work! Correct Answer!");
        databaseHandler.addOption(value);

        value1 = new Options();
        value1.setMqId(1);
        value1.setSubQuesId(4);
        value1.setOptionValue("Water");
        value1.setAnswer("F");
        value1.setExplanation("It cannot be this answer because...");
        databaseHandler.addOption(value1);

        //MainQuesHeading
        MainQuesHeading heading = new MainQuesHeading();
        heading.setMqId(1);
        heading.setHeading("Water(ml)");
        databaseHandler.addHeading(heading);

        MainQuesHeading heading1 = new MainQuesHeading();
        heading1.setMqId(1);
        heading1.setHeading("Height(cm)");
        databaseHandler.addHeading(heading1);

        //Table Data
        MainQuesHData data = new MainQuesHData();
        data.setMqId(1);
        data.sethId(1);
        data.setOrdering(1);
        data.setData("0");
        databaseHandler.addHData(data);

        MainQuesHData data1 = new MainQuesHData();
        data1.setMqId(1);
        data1.sethId(1);
        data1.setData("1");
        data1.setOrdering(2);
        databaseHandler.addHData(data1);

        MainQuesHData data2 = new MainQuesHData();
        data2.setMqId(1);
        data2.sethId(1);
        data2.setData("2");
        data2.setOrdering(3);
        databaseHandler.addHData(data2);

        MainQuesHData data3 = new MainQuesHData();
        data3.setMqId(1);
        data3.sethId(1);
        data3.setData("3");
        data3.setOrdering(4);
        databaseHandler.addHData(data3);

        MainQuesHData data4 = new MainQuesHData();
        data4.setMqId(1);
        data4.sethId(2);
        data4.setData("5");
        data4.setOrdering(1);
        databaseHandler.addHData(data4);

        MainQuesHData data5 = new MainQuesHData();
        data5.setMqId(1);
        data5.sethId(2);
        data5.setData("6");
        data5.setOrdering(2);
        databaseHandler.addHData(data5);

        MainQuesHData data6 = new MainQuesHData();
        data6.setMqId(1);
        data6.sethId(2);
        data6.setData("10");
        data6.setOrdering(3);
        databaseHandler.addHData(data6);

        MainQuesHData data7 = new MainQuesHData();
        data7.setMqId(1);
        data7.sethId(2);
        data7.setData("6");
        data7.setOrdering(4);
        databaseHandler.addHData(data7);

    }
}