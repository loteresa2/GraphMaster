package com.applicationcourse.mobile.graphmaster.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applicationcourse.mobile.graphmaster.Database.DatabaseHandler;
import com.applicationcourse.mobile.graphmaster.Database.MainQues;
import com.applicationcourse.mobile.graphmaster.Database.MainQuesHData;
import com.applicationcourse.mobile.graphmaster.Database.MainQuesHeading;
import com.applicationcourse.mobile.graphmaster.Database.Options;
import com.applicationcourse.mobile.graphmaster.Database.SubQuestion;
import com.applicationcourse.mobile.graphmaster.R;

import java.util.List;

public class ReadingActivity extends AppCompatActivity {

    List<MainQues> mainQuesList;
    int score=0;
    int qid=0;
    MainQues currentQ;
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
        List<MainQuesHeading> mainQuesHeadingList;
        List<MainQuesHData> mainQuesHDataList;
        List<SubQuestion> subQuestionList;
        List<Options> optionsList;
        mainQuesList = DatabaseHandler.getAllMainQVal("Create","Line");
        subQuestionList = DatabaseHandler.getSubQValueList("Create","Line");
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
            }
            //set the subquestion
            main.setSubQuestionList(subQuestionList);
        }

        currentQ= mainQuesList.get(qid);
        txtQuestion=(TextView)findViewById(R.id.txtMainQues);
        txtSubQues=(TextView)findViewById(R.id.txtSubQues);

        txtExplanation=(TextView)findViewById(R.id.txtExpl);
        btnNext=(Button)findViewById(R.id.btnNext);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        if(qid == 0){
            setQuestionView();
        }
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        for(MainQues mainQues : mainQuesList){
            Log.i("Main Question : ", mainQues.getQuestion());
            txtQuestion.setText(mainQues.getQuestion());
            for(MainQuesHeading mainQuesHeading: mainQues.getMainQuesHeadList()){
                Log.i("Main Heading : ",mainQuesHeading.getHeading());
                for(MainQuesHData mainQuesHData: mainQuesHeading.getMainQuesHDataList()){
                    Log.i("HData Order: ",mainQuesHData.getOrdering()+"");
                    Log.i("Main HData: ",mainQuesHData.getData());
                }
            }
            for(SubQuestion subQuestion: mainQues.getSubQuestionList()) {
                Log.i("Sub Question : ",subQuestion.getSubQuestion());
                txtSubQues.setText(subQuestion.getSubQuestion());
                // create radio button
                final RadioButton[] rb = new RadioButton[subQuestion.getOptionsList().size()];
                RadioGroup rg = new RadioGroup(this);
                rg.setOrientation(RadioGroup.VERTICAL);
                int i =0;
                for (Options options : subQuestion.getOptionsList()) {
                    if ((options.getMqId() == mainQues.getMqId()) && (options.getSubQuesId() == subQuestion.getSubQuesId())) {
                        Log.i("Options : ", options.getOptionValue());
                        rb[i] = new RadioButton(this);
                        rg.addView(rb[i]);
                        rb[i].setText(options.getOptionValue());
                        i++;
                    }
                }
                relativeLayout.addView(rg);
            }
        }
       /* btnSubmit.setOnClickListener(new View.OnClickListener() {
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
        });*/

    }
    private void setQuestionView()
    {
        //txtQuestion.setText(currentQ.getQuestion());
        //txtSubQues.setText(currentQ.getSubQuestionList().get(qid).getSubQuestion());
        /*rda.setText(currentQ.getSubQuestionList().get(qid).getOptionsList().get(0).getOptionValue());
        rdb.setText(currentQ.getSubQuestionList().get(qid).getOptionsList().get(1).getOptionValue());
        if((currentQ.getSubQuestionList().get(qid).getOptionsList().size()) > 2 ) {
            rdc.setText(currentQ.getSubQuestionList().get(qid).getOptionsList().get(2).getOptionValue());
        }
        txtExplanation.setText(null);
        btnNext.setEnabled(false);*/
    }
}
