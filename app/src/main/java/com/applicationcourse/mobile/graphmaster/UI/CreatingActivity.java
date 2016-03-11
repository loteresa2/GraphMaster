package com.applicationcourse.mobile.graphmaster.UI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
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
public class CreatingActivity extends AppCompatActivity {

    List<MainQues> mainQuesList;
    int qid=0;
    int i = 1;
    int subQuesCount=0;
    int optionCount = 0;
    MainQues currentQ;
    TextView txtQuestion;
    TextView txtSubQues;
    TextView txtExplanation;
    Button btnNext;
    Button btnSubmit;
    List<MainQuesHeading> mainQuesHeadingList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtQuestion=(TextView)findViewById(R.id.txtMainQues);
        txtSubQues=(TextView)findViewById(R.id.txtSubQues);
        txtExplanation=(TextView)findViewById(R.id.txtExpl);
        btnNext=(Button)findViewById(R.id.btnNext);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

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
                    btnNext.setEnabled(true);
                    btnSubmit.setEnabled(false);
                }else{
                    txtExplanation.setText(currentQ.getSubQuestionList().get(subQuesCount).getExplainList().get(0));
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subQuesCount < (currentQ.getSubQuestionList().size()-1)) {
                    ++subQuesCount;
                    RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioSetupSel);
                    radioGroup.clearCheck();
                    radioGroup.removeAllViews();
                    TableLayout tableLayout = (TableLayout)findViewById(R.id.table);
                    tableLayout.removeAllViews();
                    setQuestionView();
                    btnSubmit.setEnabled(true);
                }else{
                    Toast.makeText(getBaseContext(),"Need to further implement",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(),DrawGraphActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
    private void setQuestionView()
    {
        txtQuestion.setText(currentQ.getQuestion());
        txtSubQues.setText(currentQ.getSubQuestionList().get(subQuesCount).getSubQuestion());

/////////////////////////////////generate the option buttons//////////////////////////////////////
        List<Options> optionsList = currentQ.getSubQuestionList().get(subQuesCount).getOptionsList();
        for (Options options : optionsList){
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(0 + i);
            if(optionCount ==0) {
                radioButton.setChecked(true);
            }
            radioButton.setText(options.getOptionValue());
            ((ViewGroup) findViewById(R.id.radioSetupSel)).addView(radioButton);

            i++;
            optionCount++;
        }
        optionCount = 0;
        ////////////////////////////////////////generate the table/////////////////////////////////////////

        int rowNum = currentQ.getMainQuesHeadList().get(0).getMainQuesHDataList().size();
        int colNum = currentQ.getMainQuesHeadList().size();
        TableLayout tableLayout = (TableLayout)findViewById(R.id.table);
        //set the header of the table
        TableRow rowHeader = new TableRow(this);
        rowHeader.setId(0);
        rowHeader.setBackgroundColor(Color.GRAY);
        //rowHeader.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        for (int x = 1; x<=colNum;x++){
            TextView headerName = new TextView(this);
            headerName.setId(0 + i);
            headerName.setText(currentQ.getMainQuesHeadList().get(x - 1).getHeading());
            //headerName.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
            headerName.setTextColor(Color.WHITE);
            headerName.setPadding(5, 5, 5, 5);
            rowHeader.addView(headerName);
            i++;
        }
        tableLayout.addView(rowHeader);
        for(int x = 1; x<=rowNum;x++){
            TableRow tableRow = new TableRow(this);
            //tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            //column
            int headingNo = 0;
            for (int y = 1;y<=colNum;y++){
                TextView textView = new TextView(this);
                textView.setId(0+i);
                //textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
                textView.setText(currentQ.getMainQuesHeadList().get(headingNo).getMainQuesHDataList().get(x - 1).getData());
                headingNo++;
                tableRow.addView(textView);
                i++;
            }
            tableLayout.addView(tableRow);

        }

        txtExplanation.setText(null);
        btnNext.setEnabled(false);
    }
    public void getQuestions(){
        List<MainQuesHData> mainQuesHDataList;
        List<SubQuestion> subQuestionList;
        List<Options> optionsList;
        List<String> answerList;
        List<String> explanationList;
        mainQuesList = DatabaseHandler.getAllMainQVal("Create", "Line", 6);
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
            }
            //set the subquestion
            main.setSubQuestionList(subQuestionList);
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
        }
    }
}
