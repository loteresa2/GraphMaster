package com.applicationcourse.mobile.graphmaster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ReadingActivity extends AppCompatActivity {

    List<CGQuestion> quesList;
    int score=0;
    int qid=0;
    CGQuestion currentQ;
    TextView txtQuestion;
    TextView txtSubQues;
    TextView txtExplanation;
    RadioButton rda, rdb, rdc;
    Button btnNext;
    Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  DbHelper db=new DbHelper(this);
        quesList = new ArrayList<CGQuestion>();
        List<String> value= new ArrayList<String>();
        //List of all sub question for a given question
        List<CGSubQuestion> subQuesList= new ArrayList<CGSubQuestion>();
        //List of all the values for a given sub question
        List<CGValues> cgValuesList = new ArrayList<CGValues>();
        CGQuestion q1=new CGQuestion();
        CGSubQuestion subQues = new CGSubQuestion();
        CGValues cgValues = new CGValues();
        CGValues cgValues1 = new CGValues();
        q1.setQuestion("Alice is very fond of plants. She measures the height of her plant as shown");
        q1.setType("Line");
        value.add("0");
        value.add("1");
        value.add("2");
        value.add("3");
        q1.setxValue(value);
        value.clear();
        value.add("5");
        value.add("6");
        value.add("10");
        value.add("6");
        q1.setyValue(value);
        subQues.setSubQuestion("Which type of graph are you going to draw for the given data?");
        cgValues.setMainQuestionNo(0);
        cgValues.setSubQuestionNo(0);
        cgValues.setOptionValue("LINE OR SCATTER");
        cgValues.setAnswer(true);
        cgValues.setExplanation("Great Work! Correct Answer!");
        cgValuesList.add(cgValues);

        cgValues1.setMainQuestionNo(0);
        cgValues1.setSubQuestionNo(0);
        cgValues1.setOptionValue("BAR OR PIE");
        cgValues1.setAnswer(false);
        cgValues1.setExplanation("It cannot be this answer because...");
        cgValuesList.add(cgValues1);
        subQues.setCgValuesList(cgValuesList);
        subQuesList.add(subQues);
        q1.setSubQuestionList(subQuesList);
        //2nd sub questions**************************************************************
        cgValuesList = new ArrayList<CGValues>();
        subQues = new CGSubQuestion();
        subQues.setSubQuestion("What kind of data are you comparing ?");
        cgValues = new CGValues();
        cgValues.setMainQuestionNo(0);
        cgValues.setSubQuestionNo(1);
        cgValues.setOptionValue("NUMBER VS NUMBER");
        cgValues.setAnswer(true);
        cgValues.setExplanation("Great Work! Correct Answer!");
        cgValuesList.add(cgValues);

        cgValues1 = new CGValues();
        cgValues1.setMainQuestionNo(0);
        cgValues1.setSubQuestionNo(1);
        cgValues1.setOptionValue("NUMBER VS CATEGORY");
        cgValues1.setAnswer(false);
        cgValues1.setExplanation("It cannot be this answer because...");
        cgValuesList.add(cgValues1);
        subQues.setCgValuesList(cgValuesList);
        subQuesList.add(subQues);
        q1.setSubQuestionList(subQuesList);

        //3rd sub questions**************************************************************
        cgValuesList = new ArrayList<CGValues>();
        subQues = new CGSubQuestion();
        subQues.setSubQuestion("What will be in x-axis ?");
        cgValues = new CGValues();
        cgValues.setMainQuestionNo(0);
        cgValues.setSubQuestionNo(2);
        cgValues.setOptionValue("Height of the plant");
        cgValues.setAnswer(true);
        cgValues.setExplanation("Great Work! Correct Answer!");
        cgValuesList.add(cgValues);

        cgValues1 = new CGValues();
        cgValues1.setMainQuestionNo(0);
        cgValues1.setSubQuestionNo(2);
        cgValues1.setOptionValue("Amount of water added");
        cgValues1.setAnswer(false);
        cgValues1.setExplanation("It cannot be this answer because...");
        cgValuesList.add(cgValues1);
        subQues.setCgValuesList(cgValuesList);
        subQuesList.add(subQues);
        q1.setSubQuestionList(subQuesList);
        //4th sub questions**************************************************************
        cgValuesList = new ArrayList<CGValues>();
        subQues = new CGSubQuestion();
        subQues.setSubQuestion("What will be in y-axis ?");
        cgValues = new CGValues();
        cgValues.setMainQuestionNo(0);
        cgValues.setSubQuestionNo(3);
        cgValues.setOptionValue("Amount of water added");
        cgValues.setAnswer(true);
        cgValues.setExplanation("Great Work! Correct Answer!");
        cgValuesList.add(cgValues);

        cgValues1 = new CGValues();
        cgValues1.setMainQuestionNo(0);
        cgValues1.setSubQuestionNo(3);
        cgValues1.setOptionValue("Height of the plant");
        cgValues1.setAnswer(false);
        cgValues1.setExplanation("It cannot be this answer because...");
        cgValuesList.add(cgValues1);
        subQues.setCgValuesList(cgValuesList);
        subQuesList.add(subQues);
        q1.setSubQuestionList(subQuesList);

        //5th sub questions**************************************************************
        cgValuesList = new ArrayList<CGValues>();
        subQues = new CGSubQuestion();
        subQues.setSubQuestion("What will be appropriate interval in x-axis ?");
        cgValues = new CGValues();
        cgValues.setMainQuestionNo(0);
        cgValues.setSubQuestionNo(4);
        cgValues.setOptionValue("10");
        cgValues.setAnswer(true);
        cgValues.setExplanation("Great Work! Correct Answer!");
        cgValuesList.add(cgValues);

        cgValues1 = new CGValues();
        cgValues1.setMainQuestionNo(0);
        cgValues1.setSubQuestionNo(4);
        cgValues1.setOptionValue("200");
        cgValues1.setAnswer(false);
        cgValues1.setExplanation("It cannot be this answer because...");
        cgValuesList.add(cgValues1);

        cgValues = new CGValues();
        cgValues.setMainQuestionNo(0);
        cgValues.setSubQuestionNo(4);
        cgValues.setOptionValue("300");
        cgValues.setAnswer(false);
        cgValues.setExplanation("It cannot be this answer because...");
        cgValuesList.add(cgValues);
        subQues.setCgValuesList(cgValuesList);
        subQuesList.add(subQues);
        q1.setSubQuestionList(subQuesList);
        //6th sub questions**************************************************************
        cgValuesList = new ArrayList<CGValues>();
        subQues = new CGSubQuestion();
        subQues.setSubQuestion("In line graph, do you need to connect the dots on graph ?");
        cgValues = new CGValues();
        cgValues.setMainQuestionNo(0);
        cgValues.setSubQuestionNo(5);
        cgValues.setOptionValue("Yes");
        cgValues.setAnswer(true);
        cgValues.setExplanation("Great Work! Correct Answer!");
        cgValuesList.add(cgValues);

        cgValues1 = new CGValues();
        cgValues1.setMainQuestionNo(0);
        cgValues1.setSubQuestionNo(5);
        cgValues1.setOptionValue("No");
        cgValues1.setAnswer(false);
        cgValues1.setExplanation("It cannot be this answer because...");
        cgValuesList.add(cgValues1);
        subQues.setCgValuesList(cgValuesList);
        subQuesList.add(subQues);
        q1.setSubQuestionList(subQuesList);
        //Data setup done.

        quesList.add(q1);
        currentQ=quesList.get(qid);
        txtQuestion=(TextView)findViewById(R.id.txtMainQues);
        txtSubQues=(TextView)findViewById(R.id.txtSubQues);
        rda=(RadioButton)findViewById(R.id.radio0);
        rdb=(RadioButton)findViewById(R.id.radio1);
        rdc=(RadioButton)findViewById(R.id.radio2);
        txtExplanation=(TextView)findViewById(R.id.txtExpl);
        btnNext=(Button)findViewById(R.id.btnNext);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        if(qid == 0){
            setQuestionView();
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup grp = (RadioGroup) findViewById(R.id.radioSetupSel);
                RadioButton answer = (RadioButton) findViewById(grp.getCheckedRadioButtonId());
                String selected = answer.getText().toString();
                if(selected.equals("LINE OR SCATTER") && qid == 0){
                    btnNext.setEnabled(true);
                    btnSubmit.setEnabled(true);
                }else  if(!selected.equals("LINE OR SCATTER") && qid == 0) {
                    txtExplanation.setText("Explanation...");
                }if (selected.equals("NUMBER VS NUMBER") && qid == 1){
                        btnNext.setEnabled(true);
                        btnSubmit.setEnabled(true);
                    }else if(selected.equals("Height of the plant") && qid == 2) {
                        btnNext.setEnabled(true);
                        btnSubmit.setEnabled(true);
                    }else if(selected.equals("Amount of water added") && qid == 3) {
                        btnNext.setEnabled(true);
                        btnSubmit.setEnabled(true);
                    }else if(selected.equals("10") && qid == 4){
                        btnNext.setEnabled(true);
                    }else if(selected.equals("Yes") && qid == 5){
                        btnNext.setEnabled(true);
                        btnSubmit.setEnabled(true);
                    }
                }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++qid;
                setQuestionView();
            }
        });

    }
    private void setQuestionView()
    {
        txtQuestion.setText(currentQ.getQuestion());
        txtSubQues.setText(currentQ.getSubQuestionList().get(qid).getSubQuestion());
        rda.setText(currentQ.getSubQuestionList().get(qid).getCgValuesList().get(0).getOptionValue());
        rdb.setText(currentQ.getSubQuestionList().get(qid).getCgValuesList().get(1).getOptionValue());
        if((currentQ.getSubQuestionList().get(qid).getCgValuesList().size()) > 2 ) {
            rdc.setText(currentQ.getSubQuestionList().get(qid).getCgValuesList().get(2).getOptionValue());
        }
        txtExplanation.setText(null);
        btnNext.setEnabled(false);

    }
}
