package com.applicationcourse.mobile.graphmaster.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.applicationcourse.mobile.graphmaster.Database.DatabaseHandler;
import com.applicationcourse.mobile.graphmaster.Database.MainQues;
import com.applicationcourse.mobile.graphmaster.Database.MainQuesHData;
import com.applicationcourse.mobile.graphmaster.Database.MainQuesHeading;
import com.applicationcourse.mobile.graphmaster.Database.Options;
import com.applicationcourse.mobile.graphmaster.Database.SubQuestion;
import com.applicationcourse.mobile.graphmaster.R;

import java.util.List;

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

        final RippleView rippleView = (RippleView)findViewById(R.id.level1);
        final RippleView rippleView2 = (RippleView)findViewById(R.id.level2);
        final RippleView rippleView3 = (RippleView)findViewById(R.id.level3);
        final RippleView rippleView4 = (RippleView)findViewById(R.id.level4);


        rippleView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
        rippleView.setOnRippleCompleteListener(
                new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {

                        Intent intent = new Intent(MainActivity.this, DrawGraphActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
        );

        rippleView2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
        rippleView2.setOnRippleCompleteListener(
                new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        List<MainQues> list = DatabaseHandler.getAllMainQVal("Create", "Line", 1);
                    }
                }
        );

        rippleView3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
        rippleView3.setOnRippleCompleteListener(
                new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        Toast.makeText(getApplicationContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        rippleView4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
        rippleView4.setOnRippleCompleteListener(
                new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                          }
                }
        );



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
        mainQues.setGrade("1");
        databaseHandler.addQuestionValue(mainQues);
        //subquestion
        SubQuestion subQuestion = new SubQuestion();
        subQuestion.setFunction("Create");
        subQuestion.setType("Line");
        subQuestion.setOptionType("Radio");
        subQuestion.setSubQuestion("1.What type of graph should Alice create to show this data?");
        databaseHandler.addSubQValue(subQuestion);
        SubQuestion subQuestion1 = new SubQuestion();
        subQuestion1.setFunction("Create");
        subQuestion1.setType("Line");
        subQuestion1.setOptionType("Radio");
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
        value1.setOptionValue("Bar graph");
        value1.setAnswer("F");
        value1.setExplanation("Oops.  When graphing two sets of numerical data (amount of water versus height of plant) we use a line graph or scatter plot.  Bar graphs are used to graph categories against a set of numbers.  For example, a bar graph would be perfect if you were graphing favourite type of smooth (a category) versus number of people who choose to drink that smoothie at the school fair (numerical data).");
        databaseHandler.addOption(value1);

        value1 = new Options();
        value1.setMqId(1);
        value1.setSubQuesId(1);
        value1.setOptionValue("Pie graph");
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
        subQuestion.setOptionType("Radio");
        subQuestion.setSubQuestion("Which axis does the independent variable go on?");
        databaseHandler.addSubQValue(subQuestion);
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

        //subquestion
        subQuestion = new SubQuestion();
        subQuestion.setFunction("Create");
        subQuestion.setType("Line");
        subQuestion.setOptionType("LabelAxis");
        subQuestion.setSubQuestion("Place your axes labels on the x and y axes.  Don’t forget to include your units in brackets!");
        databaseHandler.addSubQValue(subQuestion);
        value1 = new Options();
        value1.setMqId(1);
        value1.setSubQuesId(4);
        value1.setOptionValue("DoneLabel");
        value1.setAnswer("T");
        value1.setExplanation("Excellent! Now that you’ve labelled your axes it’s time to create intervals for them.");
        databaseHandler.addOption(value1);

        //subquestion
        subQuestion = new SubQuestion();
        subQuestion.setFunction("Create");
        subQuestion.setType("Line");
        subQuestion.setOptionType("TextBox");
        subQuestion.setSubQuestion("Calculate the interval that you will use for the x axis");
        databaseHandler.addSubQValue(subQuestion);
        value1 = new Options();
        value1.setMqId(1);
        value1.setSubQuesId(5);
        value1.setOptionValue("1");
        value1.setAnswer("T");
        value1.setExplanation("Correct! 1 is the nearest correct interval ");
        databaseHandler.addOption(value1);

        value1 = new Options();
        value1.setMqId(1);
        value1.setSubQuesId(5);
        value1.setOptionValue("2");
        value1.setAnswer("T");
        value1.setExplanation("Correct! 2 is the correct interval ");
        databaseHandler.addOption(value1);

        //subquestion
        subQuestion = new SubQuestion();
        subQuestion.setFunction("Create");
        subQuestion.setType("Line");
        subQuestion.setOptionType("TextBox");
        subQuestion.setSubQuestion("Now that you’ve found the interval for the x-axis, let’s find it for the y-axis! Calculate the interval that you will use for the y axis.");
        databaseHandler.addSubQValue(subQuestion);
        value1 = new Options();
        value1.setMqId(1);
        value1.setSubQuesId(6);
        value1.setOptionValue("1");
        value1.setAnswer("T");
        value1.setExplanation("Correct! 1 is the correct interval  ");
        databaseHandler.addOption(value1);

        value1 = new Options();
        value1.setMqId(1);
        value1.setSubQuesId(6);
        value1.setOptionValue("2");
        value1.setAnswer("T");
        value1.setExplanation("Correct! 2 is the correct interval  ");
        databaseHandler.addOption(value1);

        //subquestion
        subQuestion = new SubQuestion();
        subQuestion.setFunction("Create");
        subQuestion.setType("Line");
        subQuestion.setOptionType("None");
        subQuestion.setSubQuestion("Titles can be tricky to write. The most important thing is to describe to the person reading your graph exactly what the graph is about.");
        databaseHandler.addSubQValue(subQuestion);
        value1 = new Options();
        value1.setMqId(1);
        value1.setSubQuesId(7);
        value1.setOptionValue("None");
        value1.setAnswer("T");
        value1.setExplanation("Testing ");
        databaseHandler.addOption(value1);

        //subquestion
        subQuestion = new SubQuestion();
        subQuestion.setFunction("Create");
        subQuestion.setType("Line");
        subQuestion.setOptionType("None");
        subQuestion.setSubQuestion("Place your points on the graph.");
        databaseHandler.addSubQValue(subQuestion);
        value1 = new Options();
        value1.setMqId(1);
        value1.setSubQuesId(8);
        value1.setOptionValue("None");
        value1.setAnswer("T");
        value1.setExplanation("Testing ");
        databaseHandler.addOption(value1);

        //MainQuesHeading
        MainQuesHeading heading = new MainQuesHeading();
        heading.setMqId(1);
        heading.setHeading("A");
        heading.setAxis("x");
        databaseHandler.addHeading(heading);

        MainQuesHeading heading1 = new MainQuesHeading();
        heading1.setMqId(1);
        heading1.setHeading("B");
        heading1.setAxis("y");
        databaseHandler.addHeading(heading1);

        //Table Data
        MainQuesHData data = new MainQuesHData();
        data.setMqId(1);
        data.sethId(1);
        data.setOrdering(1);
        data.setData("1.1");
        databaseHandler.addHData(data);

        MainQuesHData data1 = new MainQuesHData();
        data1.setMqId(1);
        data1.sethId(1);
        data1.setData("2");
        data1.setOrdering(2);
        databaseHandler.addHData(data1);

        MainQuesHData data2 = new MainQuesHData();
        data2.setMqId(1);
        data2.sethId(1);
        data2.setData("3");
        data2.setOrdering(3);
        databaseHandler.addHData(data2);

        MainQuesHData data3 = new MainQuesHData();
        data3.setMqId(1);
        data3.sethId(1);
        data3.setData("5");
        data3.setOrdering(4);
        databaseHandler.addHData(data3);

        data3 = new MainQuesHData();
        data3.setMqId(1);
        data3.sethId(1);
        data3.setData("6");
        data3.setOrdering(5);
        databaseHandler.addHData(data3);

        data3 = new MainQuesHData();
        data3.setMqId(1);
        data3.sethId(1);
        data3.setData("7");
        data3.setOrdering(6);
        databaseHandler.addHData(data3);

        MainQuesHData data4 = new MainQuesHData();
        data4.setMqId(1);
        data4.sethId(2);
        data4.setData("1.2");
        data4.setOrdering(1);
        databaseHandler.addHData(data4);

        MainQuesHData data5 = new MainQuesHData();
        data5.setMqId(1);
        data5.sethId(2);
        data5.setData("2");
        data5.setOrdering(2);
        databaseHandler.addHData(data5);

        MainQuesHData data6 = new MainQuesHData();
        data6.setMqId(1);
        data6.sethId(2);
        data6.setData("3");
        data6.setOrdering(3);
        databaseHandler.addHData(data6);

        MainQuesHData data7 = new MainQuesHData();
        data7.setMqId(1);
        data7.sethId(2);
        data7.setData("1");
        data7.setOrdering(4);
        databaseHandler.addHData(data7);

        data7 = new MainQuesHData();
        data7.setMqId(1);
        data7.sethId(2);
        data7.setData("2");
        data7.setOrdering(5);
        databaseHandler.addHData(data7);

        data7 = new MainQuesHData();
        data7.setMqId(1);
        data7.sethId(2);
        data7.setData("3");
        data7.setOrdering(6);
        databaseHandler.addHData(data7);

        //Main Question 2

        //Main Question
        mainQues = new MainQues();
        mainQues.setQuestion("Britany loves to water plants.  She is trying to find out what amount of water would help her rose bushes grow the tallest.  She conducts an experiment and measures the data in the table below.");
        mainQues.setFunction("Create");
        mainQues.setType("Line");
        mainQues.setGrade("1");
        databaseHandler.addQuestionValue(mainQues);

        //option
        value = new Options();
        value.setMqId(2);
        value.setSubQuesId(1);
        value.setOptionValue("Line graph or scatter plot");
        value.setAnswer("T");
        value.setExplanation("Correct.  When graphing two sets of numerical data we show this data as a line graph or scatter plot.");
        databaseHandler.addOption(value);

        value1 = new Options();
        value1.setMqId(2);
        value1.setSubQuesId(1);
        value1.setOptionValue("Test1");
        value1.setAnswer("F");
        value1.setExplanation("Oops.  When graphing two sets of numerical data (amount of water versus height of plant) we use a line graph or scatter plot.  Bar graphs are used to graph categories against a set of numbers.  For example, a bar graph would be perfect if you were graphing favourite type of smooth (a category) versus number of people who choose to drink that smoothie at the school fair (numerical data).");
        databaseHandler.addOption(value1);

        value1 = new Options();
        value1.setMqId(2);
        value1.setSubQuesId(1);
        value1.setOptionValue("T");
        value1.setAnswer("F");
        value1.setExplanation("Oops.  When graphing two sets of numerical data (amount of water versus height of plant) we use a line graph or scatter plot.  Pie graphs are used to graph categories against a set of numbers, when those numbers can be represented as percentages.  For example, a pie graph would be perfect if you were graphing the favourite animal of students in a class.  The animals are a category and the number of students is your numerical data that can be represented as a percentage.   ");
        databaseHandler.addOption(value1);

        //option
        value = new Options();
        value.setMqId(2);
        value.setSubQuesId(2);
        value.setOptionValue("Height of plant is the independent variable. Amount of water added is the dependent variable.");
        value.setAnswer("F");
        value.setExplanation("Oops.The height of the plant depends on the amount of water that is added to it.  Because it depends on the amount of water it is the dependent variable.  The amount of water is the independent variable.");
        databaseHandler.addOption(value);

        value1 = new Options();
        value1.setMqId(2);
        value1.setSubQuesId(2);
        value1.setOptionValue("Amount of water added is the independent variable.  Height of plant is the dependent variable.");
        value1.setAnswer("T");
        value1.setExplanation("Correct! Good job.  The height of the plant depends on the amount of water, so it is the dependent variable and the independent variable is the amount of water added.");
        databaseHandler.addOption(value1);

        //option
        value = new Options();
        value.setMqId(2);
        value.setSubQuesId(3);
        value.setOptionValue("The x-axis");
        value.setAnswer("T");
        value.setExplanation("Great Work! Correct Answer!");
        databaseHandler.addOption(value);

        value1 = new Options();
        value1.setMqId(2);
        value1.setSubQuesId(3);
        value1.setOptionValue("The y-axis");
        value1.setAnswer("F");
        value1.setExplanation("Incorrect. The x-axis is where the independent variable is located.  The dependent variable goes on the y- axis.");
        databaseHandler.addOption(value1);

        value1 = new Options();
        value1.setMqId(2);
        value1.setSubQuesId(4);
        value1.setOptionValue("DoneLabel");
        value1.setAnswer("T");
        value1.setExplanation("Excellent! Now that you’ve labelled your axes it’s time to create intervals for them.");
        databaseHandler.addOption(value1);


        value1 = new Options();
        value1.setMqId(2);
        value1.setSubQuesId(5);
        value1.setOptionValue("2");
        value1.setAnswer("T");
        value1.setExplanation("Correct! 2 is the nearest correct interval ");
        databaseHandler.addOption(value1);

        value1 = new Options();
        value1.setMqId(2);
        value1.setSubQuesId(5);
        value1.setOptionValue("5");
        value1.setAnswer("T");
        value1.setExplanation("Correct! 5 is the correct interval ");
        databaseHandler.addOption(value1);

        value1 = new Options();
        value1.setMqId(2);
        value1.setSubQuesId(6);
        value1.setOptionValue("2");
        value1.setAnswer("T");
        value1.setExplanation("Correct! 2 is the correct interval  ");
        databaseHandler.addOption(value1);

        value1 = new Options();
        value1.setMqId(2);
        value1.setSubQuesId(6);
        value1.setOptionValue("10");
        value1.setAnswer("T");
        value1.setExplanation("Correct! 10 is the correct interval  ");
        databaseHandler.addOption(value1);

        value1 = new Options();
        value1.setMqId(2);
        value1.setSubQuesId(7);
        value1.setOptionValue("None");
        value1.setAnswer("T");
        value1.setExplanation("Testing ");
        databaseHandler.addOption(value1);

        value1 = new Options();
        value1.setMqId(2);
        value1.setSubQuesId(8);
        value1.setOptionValue("None");
        value1.setAnswer("T");
        value1.setExplanation("Testing ");
        databaseHandler.addOption(value1);
        //MainQuesHeading
        heading = new MainQuesHeading();
        heading.setMqId(2);
        heading.setHeading("AA");
        heading.setAxis("x");
        databaseHandler.addHeading(heading);

        heading1 = new MainQuesHeading();
        heading1.setMqId(2);
        heading1.setHeading("BB");
        heading1.setAxis("y");
        databaseHandler.addHeading(heading1);

        //Table Data. Remember to change the heading id when adding new heading data to correspoond to tht headingid
        data = new MainQuesHData();
        data.setMqId(2);
        data.sethId(3);
        data.setOrdering(1);
        data.setData("10");
        databaseHandler.addHData(data);

        data1 = new MainQuesHData();
        data1.setMqId(2);
        data1.sethId(3);
        data1.setData("20");
        data1.setOrdering(2);
        databaseHandler.addHData(data1);

        data2 = new MainQuesHData();
        data2.setMqId(2);
        data2.sethId(3);
        data2.setData("30");
        data2.setOrdering(3);
        databaseHandler.addHData(data2);

        data3 = new MainQuesHData();
        data3.setMqId(2);
        data3.sethId(3);
        data3.setData("40");
        data3.setOrdering(4);
        databaseHandler.addHData(data3);

        data3 = new MainQuesHData();
        data3.setMqId(2);
        data3.sethId(3);
        data3.setData("50");
        data3.setOrdering(5);
        databaseHandler.addHData(data3);

        data3 = new MainQuesHData();
        data3.setMqId(2);
        data3.sethId(3);
        data3.setData("60");
        data3.setOrdering(6);
        databaseHandler.addHData(data3);

        data4 = new MainQuesHData();
        data4.setMqId(2);
        data4.sethId(4);
        data4.setData("100");
        data4.setOrdering(1);
        databaseHandler.addHData(data4);

        data5 = new MainQuesHData();
        data5.setMqId(2);
        data5.sethId(4);
        data5.setData("200");
        data5.setOrdering(2);
        databaseHandler.addHData(data5);

        data6 = new MainQuesHData();
        data6.setMqId(2);
        data6.sethId(4);
        data6.setData("300");
        data6.setOrdering(3);
        databaseHandler.addHData(data6);

        data7 = new MainQuesHData();
        data7.setMqId(2);
        data7.sethId(4);
        data7.setData("400");
        data7.setOrdering(4);
        databaseHandler.addHData(data7);

        data7 = new MainQuesHData();
        data7.setMqId(2);
        data7.sethId(4);
        data7.setData("500");
        data7.setOrdering(5);
        databaseHandler.addHData(data7);

        data7 = new MainQuesHData();
        data7.setMqId(2);
        data7.sethId(4);
        data7.setData("600");
        data7.setOrdering(6);
        databaseHandler.addHData(data7);
    }
}