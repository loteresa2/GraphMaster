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
        mainQues.setQuestion("Alice loves to grow rose bushes.  She is trying to find out what amount of water would help her rose bushes grow the tallest.  She conducts an experiment and measures the data in the table below.");
        mainQues.setFunction("Create");
        mainQues.setType("Line");
        databaseHandler.addQuestionValue(mainQues);
        //subquestion
        SubQuestion subQuestion = new SubQuestion();
        subQuestion.setFunction("Create");
        subQuestion.setType("Line");
        subQuestion.setSubQuestion("1.What type of graph should Alice create to show this data?");
        databaseHandler.addSubQValue(subQuestion);
        SubQuestion subQuestion1 = new SubQuestion();
        subQuestion1.setFunction("Create");
        subQuestion1.setType("Line");
        subQuestion1.setSubQuestion("2.Now that you’ve decided to create a line graph or scatter plot, you have to decide which variable is the independent and dependent variable.");
        databaseHandler.addSubQValue(subQuestion1);
        //option
        Options value = new Options();
        value.setMqId(1);
        value.setSubQuesId(1);
        value.setOptionValue("Line graph or scatter plot");
        value.setAnswer("T");
        value.setExplanation("Correct.  When graphing two sets of numerical data we show this data as a line graph or scatter plot.");
        databaseHandler.addOption(value);

        Options value1 = new Options();
        value1.setMqId(1);
        value1.setSubQuesId(1);
        value1.setOptionValue("Pie graph");
        value1.setAnswer("F");
        value1.setExplanation("Oops.  When graphing two sets of numerical data (amount of water versus height of plant) we use a line graph or scatter plot.  Bar graphs are used to graph categories against a set of numbers.  For example, a bar graph would be perfect if you were graphing favourite type of smooth (a category) versus number of people who choose to drink that smoothie at the school fair (numerical data).");
        databaseHandler.addOption(value1);

        value1 = new Options();
        value1.setMqId(1);
        value1.setSubQuesId(1);
        value1.setOptionValue("Bar graph");
        value1.setAnswer("F");
        value1.setExplanation("Oops.  When graphing two sets of numerical data (amount of water versus height of plant) we use a line graph or scatter plot.  Pie graphs are used to graph categories against a set of numbers, when those numbers can be represented as percentages.  For example, a pie graph would be perfect if you were graphing the favourite animal of students in a class.  The animals are a category and the number of students is your numerical data that can be represented as a percentage.   ");
        databaseHandler.addOption(value1);

        //option
        value = new Options();
        value.setMqId(1);
        value.setSubQuesId(2);
        value.setOptionValue("Height of plant is the independent variable. Amount of water added is the dependent variable.");
        value.setAnswer("F");
        value.setExplanation("Oops.The height of the plant depends on the amount of water that is added to it.  Because it depends on the amount of water it is the dependent variable.  The amount of water is the independent variable.");
        databaseHandler.addOption(value);

        value1 = new Options();
        value1.setMqId(1);
        value1.setSubQuesId(2);
        value1.setOptionValue("Amount of water added is the independent variable.  Height of plant is the dependent variable.");
        value1.setAnswer("T");
        value1.setExplanation("Correct! Good job.  The height of the plant depends on the amount of water, so it is the dependent variable and the independent variable is the amount of water added.");
        databaseHandler.addOption(value1);

        //subquestion
        subQuestion = new SubQuestion();
        subQuestion.setFunction("Create");
        subQuestion.setType("Line");
        subQuestion.setSubQuestion("Which axis does the independent variable go on?");
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
        value.setOptionValue("The x-axis");
        value.setAnswer("T");
        value.setExplanation("Great Work! Correct Answer!");
        databaseHandler.addOption(value);

        value1 = new Options();
        value1.setMqId(1);
        value1.setSubQuesId(3);
        value1.setOptionValue("The y-axis");
        value1.setAnswer("F");
        value1.setExplanation("Incorrect. The x-axis is where the independent variable is located.  The dependent variable goes on the y- axis.");
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
        data1.setData("4");
        data1.setOrdering(2);
        databaseHandler.addHData(data1);

        MainQuesHData data2 = new MainQuesHData();
        data2.setMqId(1);
        data2.sethId(1);
        data2.setData("8");
        data2.setOrdering(3);
        databaseHandler.addHData(data2);

        MainQuesHData data3 = new MainQuesHData();
        data3.setMqId(1);
        data3.sethId(1);
        data3.setData("10");
        data3.setOrdering(4);
        databaseHandler.addHData(data3);

        data3 = new MainQuesHData();
        data3.setMqId(1);
        data3.sethId(1);
        data3.setData("12");
        data3.setOrdering(4);
        databaseHandler.addHData(data3);

        data3 = new MainQuesHData();
        data3.setMqId(1);
        data3.sethId(1);
        data3.setData("17");
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
        data7.setData("35");
        data7.setOrdering(4);
        databaseHandler.addHData(data7);

        data7 = new MainQuesHData();
        data7.setMqId(1);
        data7.sethId(2);
        data7.setData("52");
        data7.setOrdering(4);
        databaseHandler.addHData(data7);

        data7 = new MainQuesHData();
        data7.setMqId(1);
        data7.sethId(2);
        data7.setData("40");
        data7.setOrdering(4);
        databaseHandler.addHData(data7);
    }
}