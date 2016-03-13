package com.applicationcourse.mobile.graphmaster.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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

public class DrawGraphActivity  extends AppCompatActivity implements View.OnTouchListener,OnSeekBarChangeListener {
    private int lineDensity = 10;
    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
    int width = (metrics.widthPixels);
    int height = (int) ((metrics.heightPixels) / 2);
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
    int score = 0;
    int qid = 0;
    int i = 1;
    int subQuesCount = 0;
    int optionCount = 0;
    MainQues currentQ;
    TextView txtQuestion;
    TextView txtSubQues;
    TextView txtExplanation;
    Button btnNext;
    Button btnSubmit;
    EditText editTextDyn;

    //check if we finish ploting all the intervals for x axis and then we'll set y axis interval touch available
    float checkx = 0;   //first enable x axis
    float checky = 0;    //then enable y axis
    float checkpoint = 0;    // then enable areas between 2 axis
    boolean checkXLabel = false;
    boolean checkYLabel = false;
    String XLabel ="none";
    String YLabel ="none";
    //check if the point has already been plotted
    int[] xpointTwice;
    int[] ypointTwice;
    int[] pointTwice;
    //since there are some bugs that I can not get table value from database,so I just set a temporary array to store the data
    int[][] tableValue;

    //ZOOM AND DRAG
    private int mode = 0;//initialization
    private static final int MODE_DRAG = 1;
    private static final int MODE_ZOOM = 2;

    //the distance between two fingers at the beginning
    private float startDis,endDis;
    //the midpoint of two fingers
    private PointF midPoint;

    //scroll vectors
    private float scrollx,scrolly;

    //to store the minimum interval No after each zoom
    private int interXNO = 0;
    private int interYNO = 0;
    private int interXMAXNO = 0;
    private int interYMAXNO = 0;

    private float newscale = 1;
    private float orgscale = 1;

    private boolean zoom = false;
    private boolean touch = false;
    private boolean disable = false;

    private SeekBar seekBarX,seekBarY;
    private TextView tvXMax,tvYMax;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_graph);
        bitmap = Bitmap.createBitmap((int) width, (int) height,
                Bitmap.Config.ARGB_8888);
        // Any implementation of ImageView can be used!
        tvXMax = (TextView)findViewById(R.id.tvXMax);
        tvYMax = (TextView)findViewById(R.id.tvYMax);

        seekBarX = (SeekBar)findViewById(R.id.seekBarX);
        seekBarY = (SeekBar)findViewById(R.id.seekBarY);

        //first the seek bar can not be moved
        seekBarY.setEnabled(false);
        seekBarX.setEnabled(false);
        //initialize the seekbar
        seekBarX.setProgress(0);
        seekBarY.setProgress(0);
        //set the temporary table value
        tableValue = new int[2][6];
        tableValue[0][0] = 1;        tableValue[0][1] = 2;
        tableValue[0][2] = 3;
        tableValue[0][3] = 4;
        tableValue[0][4] = 5;
        tableValue[0][5] = 6;
        tableValue[1][0] = 1;
        tableValue[1][1] = 5;
        tableValue[1][2] = 1;
        tableValue[1][3] = 5;
        tableValue[1][4] = 1;
        tableValue[1][5] = 5;

        checkx = 7;
        checky = 7;
        checkpoint = 6;
        //initialize the checkTwice array;
        xpointTwice = new int[(int) checkx];
        ypointTwice = new int[(int) checky];
        //0 means that this point has not been plotted in
        for (int i = 0; i < xpointTwice.length; i++) {
            xpointTwice[i] = 0;
            ypointTwice[i] = 0;
        }
        pointTwice = new int[(int) checkpoint];
        for (int j = 0; j < pointTwice.length; j++) {
            pointTwice[i] = 0;
        }

        //checkpoint = DatabaseHandler.getDataCount(
        mImageView = (ImageView) findViewById(R.id.ImageView01);
        generateGridLines();
        drawAxes();

        //The Question and Answer portion
        List<MainQuesHeading> mainQuesHeadingList;
        List<MainQuesHData> mainQuesHDataList;
        List<SubQuestion> subQuestionList;
        List<Options> optionsList;
        List<String> answerList;
        int grade = 1;
        mainQuesList = DatabaseHandler.getAllMainQVal("Create", "Line",grade);
        subQuestionList = DatabaseHandler.getSubQValueList("Create", "Line");
        for (MainQues main : mainQuesList) {
            mainQuesHeadingList = DatabaseHandler.getHeadingList(main.getMqId());
            //prepare the main question heading and options
            for (MainQuesHeading heading : mainQuesHeadingList) {
                mainQuesHDataList = DatabaseHandler.getHDataList(heading.getMqId(), heading.gethId());
                heading.setMainQuesHDataList(mainQuesHDataList);
            }
            //set the heading list with the data to main question
            main.setMainQuesHeadList(mainQuesHeadingList);
            ///////////////////Now for each main question and subquestion find the options////////////////////////////
            for (SubQuestion subQuestion : subQuestionList) {
                optionsList = DatabaseHandler.getOptionList(main.getMqId(), subQuestion.getSubQuesId());
                subQuestion.setOptionsList(optionsList);
                answerList = DatabaseHandler.getAnswerList(main.getMqId(), subQuestion.getSubQuesId());
                subQuestion.setAnswerList(answerList);
            }
            //set the subquestion
            main.setSubQuestionList(subQuestionList);
        }
        txtQuestion = (TextView) findViewById(R.id.txtMainQues);
        txtSubQues = (TextView) findViewById(R.id.txtSubQues);
        // txtOptions=(TextView)findViewById(R.id.txtOptions);

        txtExplanation = (TextView) findViewById(R.id.txtExpl);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        //For Logging purpose, will remove it later.
        for (MainQues mainQues : mainQuesList) {
            Log.i("Main Question : ", mainQues.getQuestion());
            for (MainQuesHeading mainQuesHeading : mainQues.getMainQuesHeadList()) {
                Log.i("Main Heading : ", mainQuesHeading.getHeading());
                for (MainQuesHData mainQuesHData : mainQuesHeading.getMainQuesHDataList()) {
                    Log.i("HData Order: ", mainQuesHData.getOrdering() + "");
                    Log.i("Main HData: ", mainQuesHData.getData());
                }
            }
            for (SubQuestion subQuestion : mainQues.getSubQuestionList()) {
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
        currentQ = mainQuesList.get(qid);
        //get the number of points the students need to plot
        //checkpoint = currentQ.getMainQuesHeadList().get(0).getMainQuesHDataList().size();
        setQuestionView();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected = "none";
                String optionType = currentQ.getSubQuestionList().get(subQuesCount).getOptionType();
                if (optionType.equals("Radio")) {
                    RadioGroup grp = (RadioGroup) findViewById(R.id.radioSetupSel);
                    RadioButton answer = (RadioButton) findViewById(grp.getCheckedRadioButtonId());
                    selected = answer.getText().toString();
                    selected = selected.trim();
                } else if (optionType.equals("TextBox")) {
                    selected = editTextDyn.getText().toString();
                    Toast.makeText(getBaseContext(), selected, Toast.LENGTH_SHORT).show();
                }
                int subid = (int) currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId();
                if(subid==4){
                    if ((checkXLabel==true)&&(checkYLabel==true)){
                        btnSubmit.setEnabled(false);
                        btnNext.setEnabled(true);
                        Toast.makeText(getBaseContext(), "Correct!", Toast.LENGTH_SHORT).show();
                    }else{
                        btnNext.setEnabled(false);
                    }
                }else {
                    String optionExpl = DatabaseHandler.getOptionExpl(currentQ.getMqId(), currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId(), selected);

                    if (selected.equals(currentQ.getSubQuestionList().get(subQuesCount).getAnswerList().get(0))) {
                        txtExplanation.setText(optionExpl);
                        btnNext.setEnabled(true);
                        //Toast.makeText(getBaseContext(), "Please type in the label value for both X and Y axis before coming to the next question! ", Toast.LENGTH_SHORT).show();
                        btnSubmit.setEnabled(false);

                    } else {

                        txtExplanation.setText(optionExpl);
                    }
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subQuesCount < (currentQ.getSubQuestionList().size() - 1)) {
                    String optionType = currentQ.getSubQuestionList().get(subQuesCount).getOptionType();
                    if (optionType.equals("Radio")) {
                        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioSetupSel);
                        radioGroup.clearCheck();
                        radioGroup.removeAllViews();
                    } else if (optionType.equals("TextBox")) {
                        View editTextRemove = (EditText) findViewById(R.id.edittext_dynamic);
                        ViewGroup parent = (ViewGroup) editTextRemove.getParent();
                        if (parent != null) {
                            parent.removeView(editTextRemove);
                        }
                    }
                    ++subQuesCount;
                    TableLayout tableLayout = (TableLayout) findViewById(R.id.table);
                    tableLayout.removeAllViews();
                    setQuestionView();
                    btnSubmit.setEnabled(true);
                } else {
                    Toast.makeText(getBaseContext(), "Need to further implement", Toast.LENGTH_SHORT).show();
                    // Intent intent = new Intent(getBaseContext(),DrawGraphActivity.class);
                    //startActivity(intent);
                }
            }
        });

    }

    public void drawAxes() {
        eachBoxX = width / 10;
        eachBoxY = height / 10;
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

    //for scrolling
    public void generateGridLinesScroll() {
        //updateHeightWidth();
        int rgbSize = width * height;
        int[] rgbValues = new int[rgbSize];
        for (int i = 0; i < rgbSize; i++) {
            rgbValues[i] = calculatePixelValueScroll(i);
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
        drawXIntervallScroll();
        drawYIntervallScroll();
    }

    public Integer calculatePixelValueScroll(int location) {
        // get our x and y location
        int xLocation = (int) location % width;
        int yLocation = (int) Math.floor(location / width);

        // set our white and black values
        int White = Integer.MAX_VALUE;

        // get our spacing values
        int xSpacing = (int) Math.floor(width / lineDensity*orgscale);
        int ySpacing = (int) Math.floor(height / lineDensity*orgscale);

        // test for x line
        if ((Math.abs(xLocation + scrollx) % xSpacing) == 0) {
            return currentColor;
        }
        // test for y line
        if ((Math.abs(yLocation + scrolly) % ySpacing) == 0) {
            return currentColor;
        }


        return White;
    }


    public Integer calculatePixelValue(int location) {
        // get our x and y location
        int xLocation = (int) location % width;
        int yLocation = (int) Math.floor(location / width);

        // set our white and black values
        int White = Integer.MAX_VALUE;

        // get our spacing values
        int xSpacing = (int) Math.floor(width / lineDensity*orgscale);
        int ySpacing = (int) Math.floor(height / lineDensity*orgscale);

        // test for x line
        if ((xLocation % xSpacing) == 0) {
            return currentColor;
        }
        // test for y line
        if ((yLocation % ySpacing) == 0) {
            return currentColor;
        }


        return White;
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        //generate the graph again
        generateGridLines();
        drawAxes();

        //draw all the interval values
        for (int i = 0;i<8;i++){
            canvas.drawCircle(eachBoxX * (i + 1), (eachBoxY * 9), 4, paint);
            canvas.drawText(String.valueOf(0 + i), eachBoxX  * (i + 1), (eachBoxY * 9 + eachBoxY / 4), paint);
            canvas.drawCircle(eachBoxX , eachBoxY *(9-i) , 4, paint);
            canvas.drawText(String.valueOf(0 + i), eachBoxX*3/4  , eachBoxY * (9 -i), paint);
        }

        //redraw the point we've putted on the graph
        for (int i = 0;i<pointTwice.length;i++){
            if (pointTwice[i]==1){
                paint.setColor(Color.GREEN);
                canvas.drawCircle(eachBoxX * (tableValue[0][i] + 1), eachBoxY * (9 - tableValue[1][i]), 4, paint);
                paint.setColor(Color.BLACK);
                canvas.drawText("("+tableValue[0][i]+","+tableValue[1][i]+")",eachBoxX*(tableValue[0][i]+1),eachBoxY*(9-tableValue[1][i])+14,paint);
            }
        }

        float valueX = (float) seekBarX.getProgress() / 10;
        float valueY = (float) seekBarY.getProgress() / 10;

        tvXMax.setText("" + valueX);
        tvYMax.setText("" + valueY);
        //DRAW THE RED LINE FOR BOTH X AND Y SCROLL BAR
        Paint dashLine = new Paint();
        dashLine.setARGB(255, 0, 0, 0);
        dashLine.setStyle(Paint.Style.STROKE);
        dashLine.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        dashLine.setColor(Color.RED);
        dashLine.setStrokeWidth(2);

        canvas.drawLine((float) (eachBoxX * (valueX+1.0)), eachBoxY, (float) (eachBoxX * (valueX+1.0)), eachBoxY*9, dashLine);
        canvas.drawLine(eachBoxX * 1, (float) (eachBoxY*(9.0-valueY)), eachBoxX * 9,  (float) (eachBoxY*(9.0-valueY)) , dashLine);

        //redraw
        mImageView.invalidate();

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onTouch(View v, final MotionEvent event) {
        int subid = (int) currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId();
        if (subid > 3) {
            int action = event.getAction();
            switch (action&MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    downx = event.getX();
                    downy = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    //scrollx = event.getX();
                    //scrolly = event.getY();
                    /*if (Math.abs(event.getX() - downx)>8||Math.abs(event.getY()-downy)>8) {
                        if (!disable) {
                            touch = true;
                            if (mode == MODE_ZOOM) {
                                endDis = distance(event);
                                if ((endDis > 10f)) {
                                    zoom = true;
                                    newscale = (float) ((endDis / startDis));// get zoom param
                                    orgscale *= newscale;
                                    if (orgscale > 1 && orgscale < 2.5) {
                                        final Runnable r = new Runnable() {
                                            public void run(){
                                                generateGridLines();
                                                drawAxes();
                                                drawXInterval();
                                                drawYInterval();
                                            }

                                        };
                                        r.run();
                                        //Toast.makeText(getBaseContext(), "we are zoom in and out! ", Toast.LENGTH_SHORT).show();
                                    } else if (orgscale >= 2.5) {
                                        final Runnable r = new Runnable() {
                                            public void run(){
                                        orgscale = (float) 2.5;
                                        generateGridLines();
                                        drawAxes();
                                        drawXInterval();
                                        drawYInterval();
                                            }

                                        };
                                        r.run();

                                    } else if (orgscale <= 1) {
                                        final Runnable r = new Runnable() {
                                            public void run(){
                                        orgscale = 1;
                                        generateGridLines();
                                        drawAxes();
                                        drawXInterval();
                                        drawYInterval();
                                            }

                                        };
                                        r.run();

                                    }
                                    //matrix.set(currentMatrix);
                                    //matrix.postScale(scale, scale,midPoint.x,midPoint.y);
                                }
                            } else {
                                if (!zoom) {
                                    if (orgscale != 1) {
                                        final Runnable r = new Runnable() {
                                            public void run(){
                                        scrollx = event.getX() - downx;
                                        scrolly = event.getY() - downy;
                                                generateGridLinesScroll();
                                        drawAxes();

                                    }

                                };
                                r.run();
                                    }
                                }
                                //DRAG FUNCTION
                                // Toast.makeText(getBaseContext(), "we are scrolling! ", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }*/
                    break;
                case MotionEvent.ACTION_UP:
                    mode = 0;
                    upx = event.getX();
                    upy = event.getY();
                    Log.i("width :", width + "");
                    Log.i("height: ", height + "");
                    Log.i("downx: ", downx + "");
                    Log.i("downy: ", downy + "");
                    Log.i("upx: ", upx + "");
                    Log.i("upy: ", upy + "");
                    Log.i("EachBoxX: ", eachBoxX + "");
                    Log.i("EachBoxY: ", eachBoxY + "");
                    //checkx = 0;
                    //checky = 0;
                    //disable = false;
                    //if (!touch) {
                    if (subid == 4) {
                        if (checkXLabel == false)
                            drawXAxisLabel(downx, downy, eachBoxX, (eachBoxY * 9));
                        else {
                            if (checkYLabel == false) {
                                drawYAxisLabel(downx, downy, eachBoxY, eachBoxX);
                            }
                        }
                    } else {
                        if (checkx != 0) {
                            drawXAxisPoints(downx, downy, eachBoxX, (eachBoxY * 9));
                        } else {
                            if (checky != 0) {
                                drawYAxisPoints(downx, downy, eachBoxY, eachBoxX);
                            } else {
                                seekBarX.setEnabled(true);
                                seekBarY.setEnabled(true);
                                seekBarX.setOnSeekBarChangeListener(this);
                                seekBarY.setOnSeekBarChangeListener(this);
                                seekBarX.setMax(70);
                                seekBarY.setMax(70);
                                if (checkpoint != 0) {
                                    if (checkpoint==6){
                                        AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                                        alert1.setTitle("Please move the x and y seekbar and click the graph to add points!");
                                        alert1.setCancelable(false);
                                        alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.dismiss();
                                            }
                                        });
                                        AlertDialog alertDialog = alert1.create();
                                        alertDialog.show();
                                    }
                                    //drawPoints(downx, downy, eachBoxX, eachBoxY, 1);
                                    drawPointsScrollBar(downx, downy, eachBoxX, eachBoxY, 1);

                                }
                                else{
                                    seekBarY.setEnabled(false);
                                    seekBarX.setEnabled(false);
                                }
                            }
                        }
                    }
                    mImageView.invalidate();
                    //}
                    //touch = false;
                    break;
                /*case MotionEvent.ACTION_POINTER_UP:
                    mode = 0;
                    zoom = false;
                    disable = true;
                    //Toast.makeText(getBaseContext(), "The second finger up! ", Toast.LENGTH_SHORT).show();
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = MODE_ZOOM;
                    //calculate the initial distance between two fingers
                    startDis = distance(event);

                    if (startDis>10f){
                        midPoint = mid(event);
                        //other stupid stuff!!!!!!!!
                        //Toast.makeText(getBaseContext(), "The second Finger put down!Ready for zoom! ", Toast.LENGTH_SHORT).show();

                    }
                    break;*/
                case MotionEvent.ACTION_CANCEL:
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    //count the distance between two fingers
    private float distance(MotionEvent event){
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        //triangle theorem
        return Float.parseFloat(String.valueOf(Math.sqrt(dx * dx + dy * dy)));
    }
    //distance for scrooling
    private float distanceScrollX(MotionEvent event){
        float dx = event.getX(1) - event.getX(0);
        //triangle theorem
        return Float.parseFloat(String.valueOf(dx));
    }

    private float distanceScrollY(MotionEvent event){
        float dy = event.getY(1) - event.getY(0);
        return Float.parseFloat(String.valueOf(dy));
    }

    //count the midpoint of two fingers
    private PointF mid(MotionEvent event){
        float midx = (event.getX(1) - event.getX(1))/2;
        float midy = (event.getY(1) - event.getY(1))/2;

        return new PointF(midx,midy);
    }

    //draw x interval for scroll
    public void drawXIntervallScroll(){
        // get our spacing values
        int xSpacing = (int) Math.floor(width / lineDensity * orgscale);
        //int ySpacing = (int) Math.floor(height / lineDensity*orgscale);
        //scroll to right,move towards smaller number
        if (scrollx>0) {
            if (interXNO<=1){
                int count = 0;
                for (int i = eachBoxX; i < eachBoxX * 9; i++) {
                    if ((Math.abs(i) % xSpacing) == 0) {
                        canvas.drawCircle(i, (eachBoxY * 9), 4, paint);
                        canvas.drawText(String.valueOf(interXNO + count), i, (eachBoxY * 9 + eachBoxY / 4), paint);
                        count++;
                    }
                }
                interXNO = 1;
                //its  alreadt the limit,do nothing
            }else if (interXNO>1) {
                int count = 0;
                for (int i = eachBoxX; i < eachBoxX * 9; i++) {
                    if ((Math.abs(i + scrollx) % xSpacing) == 0) {
                        canvas.drawCircle(i, (eachBoxY * 9), 4, paint);
                        canvas.drawText(String.valueOf(interXNO - 1 + count), i, (eachBoxY * 9 + eachBoxY / 4), paint);
                        count++;
                    }
                }
                interXNO--;
            }
        }else if (scrollx<0){   //increase interval number
            if (interXMAXNO>=7){ int count = 0;
                for (int i = eachBoxX; i < eachBoxX * 9; i++) {
                    if ((Math.abs(i) % xSpacing) == 0) {
                        canvas.drawCircle(i, (eachBoxY * 9), 4, paint);
                        canvas.drawText(String.valueOf(interXNO + count), i, (eachBoxY * 9 + eachBoxY / 4), paint);
                        count++;
                    }
                }
                interXMAXNO = 7;
                //its  alreadt the limit,do nothing
            }else if (interXMAXNO<7) {
                int count = 0;
                for (int i = eachBoxX; i < eachBoxX * 9; i++) {
                    if ((Math.abs(i + scrollx) % xSpacing) == 0) {
                        canvas.drawCircle(i, (eachBoxY * 9), 4, paint);
                        canvas.drawText(String.valueOf(interXNO + 1 + count), i, (eachBoxY * 9 + eachBoxY / 4), paint);
                        count++;
                        interXMAXNO = interXNO+1+count;
                    }
                }
                interXNO++;
            }
        }else{
            int count = 0;
            for (int i = eachBoxX; i < eachBoxX * 9; i++) {
                if ((Math.abs(i) % xSpacing) == 0) {
                    canvas.drawCircle(i, (eachBoxY * 9), 4, paint);
                    canvas.drawText(String.valueOf(interXNO + count), i, (eachBoxY * 9 + eachBoxY / 4), paint);
                    count++;
                }
            }
        }
    }

    //draw Y interval for scroll
    public void  drawYIntervallScroll(){
        // get our spacing values
        int ySpacing = (int) Math.floor(height / lineDensity*orgscale);
        //move down, we need to increase interval number
        if (scrolly>0) {
            if (interYMAXNO >= 7){
                int count = 0;
                for (int i = eachBoxY * 9; i > eachBoxY; i--) {
                    if ((Math.abs(i) % ySpacing) == 0) {
                        canvas.drawCircle(eachBoxX, i, 4, paint);
                        canvas.drawText(String.valueOf(interXNO + count), eachBoxX * 3/4, i, paint);
                        count++;
                    }
                }
                interYMAXNO =7;
                //its the limit,do nothing
            }else if (interYMAXNO < 7) {
                int count = 0;
                for (int i = eachBoxY * 9; i > eachBoxY; i--) {
                    if ((Math.abs(i + scrolly) % ySpacing) == 0) {
                        canvas.drawCircle(eachBoxX, i, 4, paint);
                        canvas.drawText(String.valueOf(interYNO + 1 + count), eachBoxX * 3 / 4, i, paint);
                        count++;
                        interYMAXNO = interYNO+1+count;
                    }
                }
                interYNO++;
            }
        }else if (scrolly<0){   //decrease interval number
            if (interYNO <= 1){ int count = 0;
                for (int i = eachBoxY * 9; i > eachBoxY; i--) {
                    if ((Math.abs(i) % ySpacing) == 0) {
                        canvas.drawCircle(eachBoxX, i, 4, paint);
                        canvas.drawText(String.valueOf(interXNO + count), eachBoxX * 3/4, i, paint);
                        count++;
                    }
                }
                interYNO = 1;

                //its  alreadt the limit,do nothing
            }else if (interYNO > 1) {
                int count = 0;
                for (int i = eachBoxY * 9; i > eachBoxY; i--) {
                    if ((Math.abs(i + scrolly) % ySpacing) == 0) {
                        canvas.drawCircle(eachBoxX, i, 4, paint);
                        canvas.drawText(String.valueOf(interXNO - 1 + count), eachBoxX * 3/4, i, paint);
                        count++;
                    }
                }
                interYNO--;
            }
        }else{
            int count = 0;
            for (int i = eachBoxY * 9; i > eachBoxY; i--) {
                if ((Math.abs(i) % ySpacing) == 0) {
                    canvas.drawCircle(eachBoxX, i, 4, paint);
                    canvas.drawText(String.valueOf(interXNO + count), eachBoxX * 3/4, i, paint);
                    count++;
                }
            }
        }
    }

    //draw x interval for zoom
    public void drawXInterval(){
        int round =(int)Math.floor(9/orgscale);
        int intervalNo = 4-(round/2);
        interXNO = intervalNo;
        for (int i =0;i<round;i++) {
            canvas.drawCircle(eachBoxX * orgscale * (i + 1), (eachBoxY * 9), 4, paint);
            canvas.drawText(String.valueOf(intervalNo + i), eachBoxX * orgscale * (i + 1), (eachBoxY * 9 + eachBoxY / 4), paint);
            interXMAXNO = intervalNo+i;
        }

    }

    //draw y interval for zoom
    public void drawYInterval(){
        //draw the interval points on x axis
        int round =(int)Math.floor(9/orgscale);
        int intervalNo = 4-(round/2);
        interYNO = intervalNo;
        for (int i =0;i<round;i++) {
            canvas.drawCircle(eachBoxX,eachBoxY*orgscale*(i+1),4,paint);
        }
        //draw the text
        for (int i =round-1;i>=0;i--) {
            canvas.drawText(String.valueOf(intervalNo++), eachBoxX*3/4, eachBoxY*orgscale*(i+1), paint);
        }
        interYMAXNO = intervalNo;


    }
    public void drawXAxisPoints(final float x, final float y, float xInitial, float yValue) {
        if ((x > xInitial) && (y > yValue - (yValue) / 20) && (y < (yValue + (yValue) / 20))) {
            if (checkx != 0){
                //Debug purpose
                Log.i("Touch pts: ", x + "");
                Log.i("Touch pts: ", y + "");
                Log.i("Variation Y touch ", String.valueOf((yValue) / 20));

                final float usrYPosDisply = (yValue + (yValue) / 20);  //For displaying the entered point by user at pos lower than the x-axis
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Enter Point");
                // Set an EditText view to get user input
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                float xIntepretedVal = interpretXInterval(x, eachBoxX, eachBoxX, 1);
                alert.setView(input);

                if (xpointTwice[(int) xIntepretedVal - 1] == 0) {
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String inputVal = input.getText().toString();
                            String correctValue = String.valueOf(interpretXInterval(x, eachBoxX, eachBoxX, 1));
                            paint.setColor(Color.RED);
                            float xIntepretedPos = interpretTouchPosition(x, eachBoxX, eachBoxX);
                            float xIntepretedVal = interpretXInterval(x, eachBoxX, eachBoxX, 1);
                            Log.i("Intepreted X: ", xIntepretedPos + "");
                            Log.i("Intepr Val: ", xIntepretedVal + "");
                            canvas.drawCircle(xIntepretedPos, (eachBoxY * 9), 4, paint);
                            if (Integer.parseInt(inputVal) != xIntepretedVal) {
                                Toast.makeText(getBaseContext(), "Wrong input:Correct is: " + xIntepretedVal, Toast.LENGTH_SHORT).show();
                            } else {
                                canvas.drawText(correctValue, 0, correctValue.length(), xIntepretedPos, usrYPosDisply, paint);
                                Toast.makeText(getBaseContext(), "Correct! ", Toast.LENGTH_SHORT).show();
                                //we decrease the count number of x axis interval
                                checkx--;
                                //we set this interval unavailable,in ther words,if we touch this interval again ,a toast will appear and the text will not change
                                xpointTwice[(int) xIntepretedVal - 1] = 1;
                            }

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
                } else {
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                    alert1.setTitle("You've already set this point !Let's choose other points! ");
                    alert1.setCancelable(false);
                    alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alert1.create();
                    alertDialog.show();
                }
            }else{
                AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                alert1.setTitle("Please finish x axis interval first!");
                alert1.setCancelable(false);
                alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alert1.create();
                alertDialog.show();
            }
        } else {
            AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
            alert1.setTitle("Please touch on the x axis to type in the interval value!");
            alert1.setCancelable(false);
            alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alert1.create();
            alertDialog.show();
            //Toast.makeText(getBaseContext(),"First learn to plot X-Axis. Click near the X-axis",Toast.LENGTH_SHORT).show();
        }
    }

    public float interpretTouchPosition(float x, float initialVal, float eachBoxVal) {
        /* This function will try to interpret the x value touched on the x-axis.
        example: each box is 100unit, then if a user touched x value is 200-250 then it interpret it as 200,
        if its 250-300 interpret it as 300 and so on..
         */
        float difference = Math.abs(x - initialVal);
        int boxAwayFromAxis = Math.round(difference / eachBoxVal);
        float intepretedVal = (eachBoxVal * boxAwayFromAxis) + initialVal;
        return intepretedVal;
    }

    public int interpretXInterval(float x, float initial, float eachBoxVal, int multiple) {
        /* This function will try to interpret the x interval..
         */
        float difference = x - initial;
        int boxAwayFromAxis = (Math.round(difference / eachBoxVal)) * multiple;
        return boxAwayFromAxis;
    }

    public int interpretYInterval(float y, float initial, float eachBoxVal, int multiple) {
        /* This function will try to interpret the x interval..
         */
        float difference = (height - y) - initial;
        int boxAwayFromAxis = (Math.round(difference / eachBoxVal)) * multiple;
        return boxAwayFromAxis;
    }

    //if we have finished x axis and y axis has not dinished
    public void drawYAxisPoints(final float x, final float y, final float yInitial, float xValue) {
        if ((y > yInitial) && (x > xValue - (xValue) / 2) && (x < (xValue + (xValue) / 2))) {
            if (checky!=0) {
                //Debug purpose
                Log.i("Touch pts: ", x + "");
                Log.i("Touch pts: ", y + "");
                Log.i("Variation X touch ", String.valueOf((eachBoxX) / 2));
                final float xInitial = eachBoxX;
                final float usrXPosDisply = (xInitial - (xInitial) / 4);  //For displaying the entered point by user at pos lower than the x-axis
                float yIntepretedVal = interpretYInterval(y, eachBoxY, eachBoxY, 1);
                if (ypointTwice[(int) yIntepretedVal - 1] == 0) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Enter Point");
                    // Set an EditText view to get user input
                    final EditText input = new EditText(this);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    alert.setView(input);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String inputVal = input.getText().toString();
                            paint.setColor(Color.RED);
                            float yIntepretedPos = interpretTouchPosition(y, eachBoxY, eachBoxY);
                            float yIntepretedVal = interpretYInterval(y, eachBoxY, eachBoxY, 1);
                            String correctValue = String.valueOf(yIntepretedVal);
                            Log.i("Intepreted Y: ", yIntepretedPos + "");
                            Log.i("Intepr Val: ", yIntepretedVal + "");
                            canvas.drawCircle(xInitial, yIntepretedPos, 4, paint);
                            if (Integer.parseInt(inputVal) != yIntepretedVal) {
                                Toast.makeText(getBaseContext(), "Wrong input:Correct is: " + yIntepretedVal, Toast.LENGTH_SHORT).show();
                            } else {
                                canvas.drawText(correctValue, 0, correctValue.length(), usrXPosDisply, yIntepretedPos, paint);
                                Toast.makeText(getBaseContext(), "Correct interval value! ", Toast.LENGTH_SHORT).show();
                                checky--;
                                ypointTwice[(int) yIntepretedVal - 1] = 1;
                            }
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
                } else {
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                    alert1.setTitle("You've already set this point !Let's choose other points! ");
                    alert1.setCancelable(false);
                    alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alert1.create();
                    alertDialog.show();
                }
            }else {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                alert1.setTitle("Lets finish y axis first!");
                alert1.setCancelable(false);
                alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alert1.create();
                alertDialog.show();
            }
        } else {
            AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
            alert1.setTitle("Please touch on the y axis to type in the interval value!!");
            alert1.setCancelable(false);
            alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alert1.create();
            alertDialog.show();
            //Toast.makeText(getBaseContext(),"Learn to plot Y-Axis. Click near the Y-axis",Toast.LENGTH_SHORT).show();
        }
    }


    public void drawPointsScrollBar(float xvalue,float yvalue,float xInitial,float yInitial,int multiple){
        boolean flag = false;
        /* This function is used to drawPoints in the area in the axis*/
        if ((xvalue > xInitial && yvalue > yInitial) && (xvalue < XEnd) && yvalue < YEnd ) {
            //now we need to check if x value and y value are correct
            if (checkpoint!=0) {

                float valueX = (float) seekBarX.getProgress() / 10;
                float valueY = (float) seekBarY.getProgress() / 10;

                for (int i = 0; i < 6; i++) {
                    int coresXdata = tableValue[0][i];
                    if ((valueX - (float)coresXdata) == 0) {
                        int coresYdata = tableValue[1][i];
                        if ((valueY - (float)coresYdata) == 0) {
                            flag = true;
                            if (pointTwice[i] == 0) {
                                paint.setColor(Color.GREEN);
                                canvas.drawCircle((float) (eachBoxX*(valueX+1.0)), (float) (eachBoxY*(9.0-valueY)), 4, paint);
                                paint.setColor(Color.BLACK);
                                canvas.drawText("(" + coresXdata + "," + coresYdata + ")", (float) (eachBoxX*(valueX+1.0)), (float) (eachBoxY*(9.0-valueY)) + 10, paint);
                                checkpoint--;
                                pointTwice[i] = 1;
                            } else {
                                AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                                alert1.setTitle("this point has already been plotted!Lets choose another point!");
                                alert1.setCancelable(false);
                                alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alertDialog = alert1.create();
                                alertDialog.show();
                                break;
                            }
                        }

                    }
                }
                if (flag == false) {
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                    alert1.setTitle("Opps!the point location is not correct");
                    alert1.setCancelable(false);
                    alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alert1.create();
                    alertDialog.show();
                }
            }else{
                AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                alert1.setTitle("You haven't finish plotting all the points!");
                alert1.setCancelable(false);
                alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alert1.create();
                alertDialog.show();
            }

        }
    }
    //when both x axis and y axis are finished, students can put other points
    public void drawPoints(float x, float y, float xInitial, float yInitial, int multiple) {
        boolean flag = false;
        /* This function is used to drawPoints in the area in the axis*/
        if ((x > xInitial && y > yInitial) && (x < XEnd) && y < YEnd ) {
            //now we need to check if x value and y value are correct

            if (checkpoint!=0) {
                float yIntepretedPos = interpretTouchPosition(y, eachBoxY, eachBoxY);
                float xIntepretedPos = interpretTouchPosition(x, eachBoxX, eachBoxX);
                int intepretedXVal = interpretXInterval(xIntepretedPos, xInitial, eachBoxX, multiple);
                int intepretedYVal = interpretYInterval(yIntepretedPos, yInitial, eachBoxY, multiple);

                for (int i = 0; i < 6; i++) {
                    int coresXdata = tableValue[0][i];
                    if ((intepretedXVal - coresXdata) == 0) {
                        int coresYdata = tableValue[1][i];
                        if ((intepretedYVal - coresYdata) == 0) {
                            flag = true;
                            if (pointTwice[i] == 0) {
                                paint.setColor(Color.GREEN);
                                canvas.drawCircle(xIntepretedPos, yIntepretedPos, 4, paint);
                                paint.setColor(Color.BLACK);
                                canvas.drawText("(" + coresXdata + "," + coresYdata + ")", xIntepretedPos, yIntepretedPos + 10, paint);
                                checkpoint--;
                                pointTwice[i] = 1;
                            } else {
                                AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                                alert1.setTitle("this point has already been plotted!Lets choose another point!");
                                alert1.setCancelable(false);
                                alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alertDialog = alert1.create();
                                alertDialog.show();
                                break;
                            }
                        }

                    }
                }
                if (flag == false) {
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                    alert1.setTitle("Opps!the point location is not correct");
                    alert1.setCancelable(false);
                    alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alert1.create();
                    alertDialog.show();
                }
            }else{
                AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                alert1.setTitle("You haven't finish plotting all the points!");
                alert1.setCancelable(false);
                alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alert1.create();
                alertDialog.show();
            }

        }

    }

    private void setQuestionView() {
        txtQuestion.setText(currentQ.getQuestion());
        txtSubQues.setText(currentQ.getSubQuestionList().get(subQuesCount).getSubQuestion());
        //Need to dynamically generate the radio options
/////////////////////////////////generate the option buttons//////////////////////////////////////
        List<Options> optionsList = currentQ.getSubQuestionList().get(subQuesCount).getOptionsList();
        String optionType = currentQ.getSubQuestionList().get(subQuesCount).getOptionType();
        if (optionType.equals("Radio")) {
            for (Options options : optionsList) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setId(0 + i);
                if (optionCount == 0) {
                    radioButton.setChecked(true);
                }
                radioButton.setText(options.getOptionValue());
                ((ViewGroup) findViewById(R.id.radioSetupSel)).addView(radioButton);
                i++;
                optionCount++;
            }
            optionCount = 0;
        } else if (optionType.equals("TextBox")) {
            LinearLayout layoutDynamic = (LinearLayout) findViewById(R.id.layoutDynamic);
            editTextDyn = new EditText(getApplicationContext());
            editTextDyn.setId(R.id.edittext_dynamic);
            layoutDynamic.addView(editTextDyn);
        } else if (optionType.equals("LabelAxis")) {
            //call the function to add Label

        }

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
    public void drawXAxisLabel(final float x, final float y, final float xInitial,float yValue){
        if((x > (xInitial+eachBoxX))&&(y > yValue )&&( y < (yValue + eachBoxY))){
            //Debug purpose
            Log.i("Touch pts: ", x + "");
            Log.i("Touch pts: ", y + "");

            final float usrYPosDisply= (yValue + (yValue)/10 );  //For displaying the entered Label by user at pos lower than the x-axis
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Enter X-axis Label");
            // Set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);

            if (checkXLabel==false) {
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String inputVal = input.getText().toString();
                        String corectLabel = currentQ.getMainQuesHeadList().get(0).getHeading();
                        float positionX = (eachBoxX * (lineDensity / 2)) - eachBoxX;//to make it some what in middle
                        //check if the Label is correct
                        if (inputVal.equals(corectLabel)) {
                            paint.setColor(Color.RED);
                            canvas.drawText(inputVal, 0, inputVal.length(), positionX, usrYPosDisply, paint);
                            paint.setColor(Color.BLACK);
                            //set checkXLabel value so that it shows that we've already type in the correct x Label value
                            checkXLabel = true;
                            XLabel = corectLabel;
                        } else {
                            Toast.makeText(getBaseContext(), "Opps!You may miss something!Let's try again!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getBaseContext(), corectLabel, Toast.LENGTH_SHORT).show();

                        }

                        dialog.dismiss();

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }else{
                AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                alert1.setTitle("You've already type in the x Label!Let's plot y axis label!");
                alert1.setCancelable(false);
                alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alert1.create();
                alertDialog.show();
            }
        }else{
            AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
            alert1.setTitle("Plot Below X-Axis!");
            alert1.setCancelable(false);
            alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alert1.create();
            alertDialog.show();
            //Toast.makeText(getBaseContext(),"Plot Below X-Axis",Toast.LENGTH_SHORT).show();
        }
    }

    public void drawYAxisLabel(final float x, final float y, final float yInitial,float xValue){
        if((y > (yInitial+eachBoxY))&&(x > 0)&&( x < eachBoxX )){
            //Debug purpose
            Log.i("Touch pts: ", x + "");
            Log.i("Touch pts: ", y + "");
            final float xInitial = eachBoxX;
            final float usrXPosDisply= (xInitial - (xInitial)/2 );  //For displaying the entered point by user at pos lower than the x-axis
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Enter Y-axis Label");
            // Set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);

            if (checkYLabel==false) {
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String inputVal = input.getText().toString();
                        String corectLabel = currentQ.getMainQuesHeadList().get(1).getHeading();
                        float positionY = (eachBoxY * (lineDensity / 2)) - eachBoxY;//to make it some what in middle
                        //check the Y label
                        if (inputVal.equals(corectLabel)) {
                            paint.setColor(Color.RED);
                            canvas.save();
                            canvas.rotate(-90, 120, 90);
                            canvas.drawText(inputVal, -150, 0, paint);
                            canvas.restore();
                            paint.setColor(Color.BLACK);
                            checkYLabel=true;
                            YLabel = corectLabel;
                        }else{
                            Toast.makeText(getBaseContext(), "Opps!You may miss something!Let's try again!", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }else{
                AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                alert1.setTitle("You've already type in the Y Label!Let's go to the next step!");
                alert1.setCancelable(false);
                alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alert1.create();
                alertDialog.show();
            }
        }else{
            AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
            alert1.setTitle("Learn to plot Y-Axis Label. Click near the Y-axis");
            alert1.setCancelable(false);
            alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alert1.create();
            alertDialog.show();
            //Toast.makeText(getBaseContext(),"Learn to plot Y-Axis Label. Click near the Y-axis",Toast.LENGTH_SHORT).show();
        }
    }
}