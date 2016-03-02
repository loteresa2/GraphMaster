package com.applicationcourse.mobile.graphmaster.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class DrawGraphActivity  extends AppCompatActivity implements View.OnTouchListener  {
    private int lineDensity = 10;
    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
    int width = (metrics.widthPixels);
    int height = (int) ((metrics.heightPixels)/2);
    int eachBoxX;
    int eachBoxY;
    private Bitmap staticBitmap = null;
    Bitmap bitmap;
    private boolean usingImage = false;
    ImageView mImageView;
    Canvas canvas;
    Paint paint;
    float downx = 0, downy = 0, upx = 0, upy = 0;
    int currentColor = -3355444;
    float XEnd;
    float YEnd;
    List<MainQues> mainQuesList;
    int score=0;
    int qid=0;
    int i = 1;
    int subQuesCount=0;
    int optionCount = 0;
    MainQues currentQ;
    TextView txtQuestion;
    TextView txtSubQues;
    TextView txtOptions;
    TextView txtExplanation;
    RadioButton rda, rdb, rdc;
    Button btnNext;
    Button btnSubmit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_graph);
        bitmap = Bitmap.createBitmap((int) width, (int) height,
                Bitmap.Config.ARGB_8888);
        // Any implementation of ImageView can be used!

        mImageView = (ImageView) findViewById(R.id.ImageView01);
        generateGridLines();
        drawAxes();
        //The Question and Answer portion
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
                   // Intent intent = new Intent(getBaseContext(),DrawGraphActivity.class);
                    //startActivity(intent);
                }
            }
        });

    }
    public void drawAxes(){
        eachBoxX = width/10;
        eachBoxY = height /10;
        XEnd = eachBoxX * 9;
        YEnd = eachBoxY * 9;
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);
        canvas.drawLine(eachBoxX * 1, YEnd, XEnd, YEnd, paint);
        canvas.drawLine(eachBoxX * 1, YEnd, eachBoxX * 1, eachBoxY * 1, paint);
        paint.setStrokeWidth(1);
    }
    public void generateGridLines() {
        //updateHeightWidth();
        int rgbSize = width * height;
        int[] rgbValues = new int[rgbSize];
        for (int i = 0; i < rgbSize; i++) {
            rgbValues[i] = calculatePixelValue(i);
        }
        staticBitmap = Bitmap.createBitmap(rgbValues, width, height,
                Bitmap.Config.RGB_565);
        Bitmap mutableBitmap = staticBitmap.copy(Bitmap.Config.ARGB_8888, true);
        // set the imageview to the static
        mImageView.setImageBitmap(mutableBitmap);
        mImageView.setOnTouchListener(this);
        canvas = new Canvas(mutableBitmap);
        //mImageView.setImageBitmap(staticBitmap);
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }
    public Integer calculatePixelValue(int location)
    {
        // get our x and y location
        int xLocation = (int) location%width;
        int yLocation = (int) Math.floor(location/width);

        // set our white and black values
        int White = Integer.MAX_VALUE;

        // get our spacing values
        int xSpacing = (int) Math.floor(width/lineDensity);
        int ySpacing = (int) Math.floor(height/lineDensity);

        // test for x line
        if((xLocation % xSpacing) == 0)
        {
            return currentColor;
        }
        // test for y line
        if((yLocation % ySpacing) == 0)
        {
            return currentColor;
        }


        return White;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        int MIN_DISTANCE = 250;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downx = event.getX();
                downy = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                upx = event.getX();
                upy = event.getY();
                Log.i("width :",width+"");
                Log.i("height: ",height+"");
                Log.i("downx: ", downx + "");
                Log.i("downy: ", downy + "");
                Log.i("upx: ", upx + "");
                Log.i("upy: ", upy + "");
                Log.i("EachBoxX: ", eachBoxX + "");
                Log.i("EachBoxY: ", eachBoxY + "");
                drawXAxisPoints(downx, downy, eachBoxX, (eachBoxY * 9));
                drawYAxisPoints(downx,downy,eachBoxY,eachBoxX);

                mImageView.invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

    public void drawXAxisPoints(final float x, final float y, float xInitial,float yValue){
        if((x > xInitial)&&(y > yValue - (yValue)/20 )&&( y < (yValue + (yValue)/20 ))){
            //Debug purpose
            Log.i("Touch pts: ", x + "");
            Log.i("Touch pts: ", y + "");
            Log.i("Variation Y touch ", String.valueOf((yValue) / 20));



            final float usrYPosDisply= (yValue + (yValue)/20 );  //For displaying the entered point by user at pos lower than the x-axis
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Enter Point");
            // Set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String inputVal = input.getText().toString();
                    paint.setColor(Color.RED);
                    float xIntepretedPos = interpretTouchPosition(x,eachBoxX,eachBoxX);
                    float xIntepretedVal = interpretXInterval(x, eachBoxX, eachBoxX, 1);
                    Log.i("Intepreted X: ",xIntepretedPos+"");
                    Log.i("Intepr Val: ",xIntepretedVal+"");
                    if(Integer.parseInt(inputVal) != xIntepretedVal){
                        Toast.makeText(getBaseContext(), "Wrong input:Correct is: " + xIntepretedVal, Toast.LENGTH_SHORT).show();
                    }
                    canvas.drawCircle(xIntepretedPos,(eachBoxY*9),4,paint);
                    canvas.drawText(inputVal, 0, inputVal.length(), xIntepretedPos, usrYPosDisply, paint);
                    dialog.dismiss();
                    paint.setColor(Color.BLACK);
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }else{
            Toast.makeText(getBaseContext(),"Learn to plot X-Axis. Click near the X-axis",Toast.LENGTH_SHORT).show();
        }
    }

    public float interpretTouchPosition(float x, float initialVal, float eachBoxVal){
        /* This function will try to interpret the x value touched on the x-axis.
        example: each box is 100unit, then if a user touched x value is 200-220 then it interpret it as 200,
        if its 230-270 then interpret it as 250 and 270-300 interpret it as 300 and so on..
         */
        float difference = Math.abs(x - initialVal);
        int boxAwayFromAxis = Math.round(difference/eachBoxVal);
        float intepretedVal = (eachBoxVal*boxAwayFromAxis)+initialVal;
        return intepretedVal;
    }
    public int interpretXInterval(float x, float initial, float eachBoxVal, int multiple){
        /* This function will try to interpret the x interval..
         */
        float difference = x - initial;
        int boxAwayFromAxis = (Math.round(difference/eachBoxVal))*multiple;
        return boxAwayFromAxis;
    }
    public int interpretYInterval(float y, float initial, float eachBoxVal, int multiple){
        /* This function will try to interpret the x interval..
         */
        float difference = (height - y) - initial;
        int boxAwayFromAxis = (Math.round(difference/eachBoxVal))*multiple;
        return boxAwayFromAxis;
    }
    public void drawYAxisPoints(final float x, final float y, final float yInitial,float xValue){
        if((y > yInitial)&&(x > xValue - (xValue)/2 )&&( x < (xValue + (xValue)/2 ))){
            //Debug purpose
            Log.i("Touch pts: ", x + "");
            Log.i("Touch pts: ", y + "");
            Log.i("Variation X touch ", String.valueOf((eachBoxX) / 2));
            final float xInitial = eachBoxX;
            final float usrXPosDisply= (xInitial - (xInitial)/4 );  //For displaying the entered point by user at pos lower than the x-axis
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Enter Point");
            // Set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String inputVal = input.getText().toString();
                    paint.setColor(Color.RED);
                    float yIntepretedPos = interpretTouchPosition(y,yInitial,eachBoxY);
                    float yIntepretedVal = interpretYInterval(y, yInitial, eachBoxY, 1);
                    Log.i("Intepreted Y: ",yIntepretedPos+"");
                    Log.i("Intepr Val: ",yIntepretedVal+"");
                    if(Integer.parseInt(inputVal) != yIntepretedVal){
                        Toast.makeText(getBaseContext(),"Wrong input:Correct is: "+yIntepretedVal,Toast.LENGTH_SHORT).show();
                    }
                    canvas.drawCircle(xInitial,yIntepretedPos,4,paint);
                    canvas.drawText(inputVal, 0, inputVal.length(), usrXPosDisply,yIntepretedPos,  paint);
                    dialog.dismiss();
                    paint.setColor(Color.BLACK);
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }else{
            Toast.makeText(getBaseContext(),"Learn to plot Y-Axis. Click near the Y-axis",Toast.LENGTH_SHORT).show();
        }
    }
    public void drawPoints(float x, float y, float xInitial, float yInitial, int multiple){
        /* This function is used to drawPoints in the area in the axis*/
        if( (x > xInitial && y >  yInitial) && (x < XEnd) && y < YEnd) {
            int intepretedXVal = interpretXInterval(x, xInitial, eachBoxX, multiple);
            int intepretedYVal = interpretYInterval(y, yInitial, eachBoxY, multiple);
            Toast.makeText(getBaseContext(), "X: " + intepretedXVal, Toast.LENGTH_SHORT).show();
            Toast.makeText(getBaseContext(), "Y: " + intepretedYVal, Toast.LENGTH_SHORT).show();
        }
    }
    private void setQuestionView()
    {
        txtQuestion.setText(currentQ.getQuestion());
        txtSubQues.setText(currentQ.getSubQuestionList().get(subQuesCount).getSubQuestion());

        //Need to dynamically generate the radio options
        // txtOptions.setText(currentQ.getSubQuestionList().get(subQuesCount).getOptionsList().toString());
        //rda.setText(currentQ.getSubQuestionList().get(subQuesCount).getOptionsList().get(0).getOptionValue());
        //rdb.setText(currentQ.getSubQuestionList().get(subQuesCount).getOptionsList().get(1).getOptionValue());
//        rdc.setText(currentQ.getSubQuestionList().get(qid).getOptionsList().get(2).getOptionValue());
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
}
