package com.applicationcourse.mobile.graphmaster.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.applicationcourse.mobile.graphmaster.Database.DatabaseHandler;
import com.applicationcourse.mobile.graphmaster.Database.MainQues;
import com.applicationcourse.mobile.graphmaster.Database.MainQuesHData;
import com.applicationcourse.mobile.graphmaster.Database.MainQuesHeading;
import com.applicationcourse.mobile.graphmaster.Database.Options;
import com.applicationcourse.mobile.graphmaster.Database.SubQuestion;
import com.applicationcourse.mobile.graphmaster.R;

import java.util.List;
//Pending -- needs to implement multiple correct answers
public class ReadingActivity extends AppCompatActivity {

    List<MainQues> mainQuesList;
    int score=0;
    int qid=0;
    int subQuesCount=0;
    MainQues currentQ;
    TextView txtQuestion;
    TextView txtSubQues;
    TextView txtOptions;
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
        List<MainQuesHeading> mainQuesHeadingList;
        List<MainQuesHData> mainQuesHDataList;
        List<SubQuestion> subQuestionList;
        List<Options> optionsList;
        List<String> answerList;
        List<String> explanationList;
        mainQuesList = DatabaseHandler.getAllMainQVal("Create", "Line");
        subQuestionList = DatabaseHandler.getSubQValueList("Create", "Line");
        for(MainQues main : mainQuesList){
            mainQuesHeadingList = DatabaseHandler.getHeadingList(main.getMqId());
            //prepare the main question heading and options
            for(MainQuesHeading heading: mainQuesHeadingList) {
                mainQuesHDataList = DatabaseHandler.getHDataList(heading.getMqId(), heading.gethId());
                heading.setMainQuesHDataList(mainQuesHDataList);
            }
            //set the heading list with the data to main question
            main.setMainQuesHeadList(mainQuesHeadingList);
            ///////////////////Now for each main question and subquestion find the options////////////////////////////
            for(SubQuestion subQuestion : subQuestionList){
                optionsList = DatabaseHandler.getOptionList(main.getMqId(),subQuestion.getSubQuesId());
                subQuestion.setOptionsList(optionsList);
                answerList = DatabaseHandler.getAnswerList(main.getMqId(),subQuestion.getSubQuesId());
                subQuestion.setAnswerList(answerList);
                explanationList = DatabaseHandler.getExplanationList(main.getMqId(),subQuestion.getSubQuesId());
                subQuestion.setExplainList(explanationList);
            }
            //set the subquestion
            main.setSubQuestionList(subQuestionList);
        }
        txtQuestion=(TextView)findViewById(R.id.txtMainQues);
        txtSubQues=(TextView)findViewById(R.id.txtSubQues);
       // txtOptions=(TextView)findViewById(R.id.txtOptions);
        rda=(RadioButton)findViewById(R.id.radio0);
        rdb=(RadioButton)findViewById(R.id.radio1);
        rdc=(RadioButton)findViewById(R.id.radio2);
        txtExplanation=(TextView)findViewById(R.id.txtExpl);
        btnNext=(Button)findViewById(R.id.btnNext);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        //For Logging purpose, will remove it later.
        for(MainQues mainQues : mainQuesList){
            Log.i("Main Question : ", mainQues.getQuestion());
            for(MainQuesHeading mainQuesHeading: mainQues.getMainQuesHeadList()){
                Log.i("Main Heading : ",mainQuesHeading.getHeading());
                for(MainQuesHData mainQuesHData: mainQuesHeading.getMainQuesHDataList()){
                    Log.i("HData Order: ",mainQuesHData.getOrdering()+"");
                    Log.i("Main HData: ",mainQuesHData.getData());
                }
            }
            for(SubQuestion subQuestion: mainQues.getSubQuestionList()) {
                Log.i("Sub Question : ", subQuestion.getSubQuestion());
                for (Options options : subQuestion.getOptionsList()) {
                    if ((options.getMqId() == mainQues.getMqId()) && (options.getSubQuesId() == subQuestion.getSubQuesId())) {
                        Log.i("Options : ", options.getOptionValue());
                    }
                }
            }
        }
        //To generate view for each main question
        //qid will keep track of current main question, subQuesCount will keep track of current subquestion
        currentQ= mainQuesList.get(qid);
        setQuestionView();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup grp = (RadioGroup) findViewById(R.id.radioSetupSel);
                RadioButton answer = (RadioButton) findViewById(grp.getCheckedRadioButtonId());
                String selected = answer.getText().toString();
                if(selected.equals(currentQ.getSubQuestionList().get(subQuesCount).getAnswerList().get(0))){
                    txtExplanation.setText("Good Job! Correct Answer");
                }else{
                    txtExplanation.setText(currentQ.getSubQuestionList().get(subQuesCount).getExplainList().get(0));
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subQuesCount < currentQ.getSubQuestionList().size()) {
                    ++subQuesCount;
                    setQuestionView();
                }else{
                    Toast.makeText(getBaseContext(),"Need to further implement",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void setQuestionView()
    {
        txtQuestion.setText(currentQ.getQuestion());
        txtSubQues.setText(currentQ.getSubQuestionList().get(subQuesCount).getSubQuestion());

        //Need to dynamically generate the radio options
        // txtOptions.setText(currentQ.getSubQuestionList().get(subQuesCount).getOptionsList().toString());
        rda.setText(currentQ.getSubQuestionList().get(subQuesCount).getOptionsList().get(0).getOptionValue());
        rdb.setText(currentQ.getSubQuestionList().get(subQuesCount).getOptionsList().get(1).getOptionValue());
//        rdc.setText(currentQ.getSubQuestionList().get(qid).getOptionsList().get(2).getOptionValue());



        txtExplanation.setText(null);
        btnNext.setEnabled(true);
    }
}
