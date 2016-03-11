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
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.applicationcourse.mobile.graphmaster.Database.DatabaseHandler;
import com.applicationcourse.mobile.graphmaster.Database.HeadingData;
import com.applicationcourse.mobile.graphmaster.Database.MainQues;
import com.applicationcourse.mobile.graphmaster.Database.MainQuesHData;
import com.applicationcourse.mobile.graphmaster.Database.MainQuesHeading;
import com.applicationcourse.mobile.graphmaster.Database.Options;
import com.applicationcourse.mobile.graphmaster.Database.Progress;
import com.applicationcourse.mobile.graphmaster.Database.SubQuestion;
import com.applicationcourse.mobile.graphmaster.R;
import com.applicationcourse.mobile.graphmaster.Util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DrawGraphActivity  extends AppCompatActivity implements View.OnTouchListener {
    private int lineDensity = 10;
    int eachBoxX,eachBoxY,qid = 0,i = 1,grade = 0,subQuesCount = 0, optionCount = 0, width,height, noOfWrong = 0;
    long timeStart =0;
    private Bitmap staticBitmap = null;
    Bitmap bitmap;
    ImageView mImageView;
    Canvas canvas;
    Paint paint;
    TextView txtTitle;
    //check if we finish ploting all the intervals for x axis and then we'll set y axis interval touch available,
    //first enable x axis, then y then areas between 2 axis
    float downx = 0, downy = 0, upx = 0, upy = 0, XEnd, YEnd, checkx = 0,checky = 0, checkpoint = 0;
    int currentColor = -3355444;
    List<MainQues> mainQuesList;
    MainQues currentQ;
    List<HeadingData> pointList;
    TextView txtQuestion, txtSubQues, txtExplanation;
    Button btnNext,btnSubmit;
    EditText editTextDyn;
    List<MainQuesHeading> mainQuesHeadingList;
    boolean checkXLabel = false,checkYLabel = false;
    String XLabel ="none",YLabel ="none";
    //check if the point has already been plotted
    int xpointTwice[],ypointTwice[],pointTwice[];
    //since there are some bugs that I can not get table value from database,so I just set a temporary array to store the data
    int[][] tableValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_graph);
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        width = (metrics.widthPixels);
        height = ((metrics.heightPixels) / 2);
        int level = 6;
        //set the temporary table value
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
        mImageView = (ImageView) findViewById(R.id.ImageView01);

        txtQuestion = (TextView) findViewById(R.id.txtMainQues);
        txtSubQues = (TextView) findViewById(R.id.txtSubQues);
        txtExplanation = (TextView) findViewById(R.id.txtExpl);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        drawGraph();

        getQuestions(level);
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
                if ((subQuesCount < (currentQ.getSubQuestionList().size())) && (qid < mainQuesList.size())) {
                    String optionType = currentQ.getSubQuestionList().get(subQuesCount).getOptionType();
                    if (optionType.equals("Radio")) {
                        RadioGroup grp = (RadioGroup) findViewById(R.id.radioSetupSel);
                        RadioButton answer = (RadioButton) findViewById(grp.getCheckedRadioButtonId());
                        selected = answer.getText().toString();
                    } else if (optionType.equals("TextBox")) {
                        selected = editTextDyn.getText().toString();
                    }
                    selected = selected.trim();
                    int subid = (int) currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId();
                    if (subid == 4) {
                        if ((checkXLabel == true) && (checkYLabel == true)) {
                            btnSubmit.setEnabled(false);
                            btnNext.setEnabled(true);
                            Toast.makeText(getBaseContext(), "Correct!", Toast.LENGTH_SHORT).show();
                        } else {
                            btnNext.setEnabled(false);
                            noOfWrong++;
                        }
                    } else if (subid == 5 || subid == 6) {
                        //Checking for the input value for interval
                        List<String> answerList = DatabaseHandler.getAnswerList(currentQ.getMqId(), subid);
                        if (answerList.contains(selected)) {
                            //display the explanation
                            String optionExpl = DatabaseHandler.getOptionExpl(currentQ.getMqId(), subid, selected);
                            txtExplanation.setText(optionExpl);
                            btnSubmit.setEnabled(false);
                            btnNext.setEnabled(true);
                        } else {
                            Toast.makeText(getBaseContext(), "Enter the interval", Toast.LENGTH_SHORT).show();
                            noOfWrong++;
                        }
                    } else if (subid == 7) {
                        txtTitle = (TextView) findViewById(R.id.txtTitle);
                        if (txtTitle.getText().length() == 0) {
                            Toast.makeText(getBaseContext(), "Enter the title", Toast.LENGTH_SHORT).show();
                        } else {
                            btnSubmit.setEnabled(false);
                            btnNext.setEnabled(true);
                        }
                    } else if (subid == 8) {
                      /*  if (checkpoint != 0) {
                            noOfWrong++;
                            Toast.makeText(getBaseContext(), "Not all points are plotted", Toast.LENGTH_SHORT).show();
                        } else {*/
                        btnSubmit.setEnabled(false);
                        btnNext.setEnabled(true);
                        //}
                    } else {
                        String optionExpl = DatabaseHandler.getOptionExpl(currentQ.getMqId(), currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId(), selected);
                        if (selected.equals(currentQ.getSubQuestionList().get(subQuesCount).getAnswerList().get(0))) {
                            txtExplanation.setText(optionExpl);
                            btnNext.setEnabled(true);
                            //Toast.makeText(getBaseContext(), "Please type in the label value for both X and Y axis before coming to the next question! ", Toast.LENGTH_SHORT).show();
                            btnSubmit.setEnabled(false);

                        } else {
                            noOfWrong++;
                            txtExplanation.setText(optionExpl);
                        }
                    }
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qid < mainQuesList.size()) {
                    if (subQuesCount < (currentQ.getSubQuestionList().size())) {
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
                    }
                    if (subQuesCount < (currentQ.getSubQuestionList().size())) {
                        setQuestionView();
                        btnSubmit.setEnabled(true);
                    } else {
                        Toast.makeText(getBaseContext(), "Starting next question", Toast.LENGTH_SHORT).show();
                        //Set the previous main question no of wrong
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = new Date();
                        String dateVal = dateFormat.format(date);
                        Long timeEnd = System.currentTimeMillis();
                        Long timeTaken = timeEnd - timeStart;
                        String time = Util.getElapsedTime(timeTaken);
                        //TODO: Student id to change and grade
                        Progress prog = new Progress(dateVal, 1, "create", 1, time, noOfWrong);
                        DatabaseHandler.addProgressData(prog);
                        canvas.drawColor(Color.WHITE);
                        subQuesCount = 0;
                        drawGraph();
                        //Reset the subquestion count back to zero
                        //Check the progress before proceeding
                        String progress = DatabaseHandler.getProgressResult(1, "create", 1,time, "00:02:00");
                        if (progress.equals("repeatLevel") || progress.equals("nextLevel")) {
                            qid++;
                            if (progress.equals("repeatLevel")) {
                                if (qid == mainQuesList.size()) {
                                    qid = 0;
                                }
                            } else {
                                //Proceed to next level
                                getQuestions(grade++);
                                qid = 0;
                            }
                            currentQ = mainQuesList.get(qid);
                            setQuestionView();
                            btnSubmit.setEnabled(true);
                        } else if (progress.equals("promoteLevel")) {
                            grade++;
                            qid = 0;
                            currentQ = mainQuesList.get(qid);
                            getQuestions(grade++);
                        }
                    }
                } else {
                    Toast.makeText(getBaseContext(), "You finished learning how to create graph!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void drawAxes() {
        eachBoxX = width / 10;
        eachBoxY = height / 10;
        XEnd = eachBoxX * 9;
        YEnd = eachBoxY * 9;
        //set the check value
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
            pointTwice[j] = 0;
        }

        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);
        canvas.drawLine(eachBoxX * 1, YEnd, XEnd, YEnd, paint);
        canvas.drawLine(eachBoxX * 1, YEnd, eachBoxX * 1, eachBoxY * 1, paint);
        //Arrow
        canvas.drawLine(eachBoxX / 4 + eachBoxX, eachBoxY / 4 + eachBoxY, eachBoxX * 1, eachBoxY * 1, paint);
        canvas.drawLine(eachBoxX - eachBoxX / 4, eachBoxY / 4 + eachBoxY, eachBoxX * 1, eachBoxY * 1, paint);
        canvas.drawLine(eachBoxX * 9 -eachBoxX/4 , YEnd-(YEnd/30), XEnd, YEnd, paint);
        canvas.drawLine(eachBoxX * 9 - eachBoxX / 4, YEnd + (YEnd / 30), XEnd, YEnd, paint);
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

    public Integer calculatePixelValue(int location) {
        // get our x and y location
        int xLocation = (int) location % width;
        int yLocation = (int) Math.floor(location / width);

        // set our white and black values
        int White = Integer.MAX_VALUE;

        // get our spacing values
        int xSpacing = (int) Math.floor(width / lineDensity);
        int ySpacing = (int) Math.floor(height / lineDensity);

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
    public boolean onTouch(View v, MotionEvent event) {
        int subid = (int) currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId();
        if (subid > 3) {
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
                    Log.i("width :", width + "");
                    Log.i("height: ", height + "");
                    Log.i("downx: ", downx + "");
                    Log.i("downy: ", downy + "");
                    Log.i("upx: ", upx + "");
                    Log.i("upy: ", upy + "");
                    Log.i("EachBoxX: ", eachBoxX + "");
                    Log.i("EachBoxY: ", eachBoxY + "");

                    if(subid ==4){
                        if (checkXLabel==false)
                            drawXAxisLabel(downx, downy, eachBoxX, (eachBoxY * 9));
                        else {
                            if(checkYLabel==false){
                                drawYAxisLabel(downx, downy, eachBoxY, eachBoxX);
                            }
                        }
                    }else if(subid == 7){
                        drawTitle(downx, downy, eachBoxX, (eachBoxY * 9));
                    }
                    else if(subid ==8){
                        if (checkx != 0) {
                            drawXAxisPoints(downx, downy, eachBoxX, (eachBoxY * 9));
                        }else{
                            if (checky != 0) {
                                drawYAxisPoints(downx, downy, eachBoxY, eachBoxX);
                            }else{
                                if (checkpoint!=0){
                                    drawPoints(downx, downy, eachBoxX, eachBoxY, 1);
                                }
                            }
                        }
                    }

                    mImageView.invalidate();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    public void drawXAxisPoints(final float x, final float y, float xInitial, float yValue) {
        if ((x > xInitial) && (y > (yValue - (yValue) / 20)) && (y < (yValue + (yValue) / 20))) {
            if (checkx != 0){
                //Debug purpose
                Log.i("Touch pts: ", x + "");
                Log.i("Touch pts: ", y + "");
                Log.i("Variation Y touch ", String.valueOf((yValue) / 20));

                final float usrYPosDisply = (yValue + (yValue) / 20);  //For displaying the entered point by user at pos lower than the x-axis
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Enter Intervals for the X-axis");
                // Set an EditText view to get user input
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                float xIntepretedPos = interpretTouchPosition(x, eachBoxX, eachBoxX);
                float xIntepretedVal = interpretXInterval(xIntepretedPos, eachBoxX, eachBoxX, 1);
                alert.setView(input);

                if (xpointTwice[(int) xIntepretedVal - 1] == 0) {
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String inputVal = input.getText().toString();
                            int inpVal=0;
                            paint.setColor(Color.RED);
                            float xIntepretedPos = interpretTouchPosition(x, eachBoxX, eachBoxX);
                            float xIntepretedVal = interpretXInterval(xIntepretedPos, eachBoxX, eachBoxX, 1);
                            Log.i("Intepreted X: ", xIntepretedPos + "");
                            Log.i("Intepr Val: ", xIntepretedVal + "");

                            if(inputVal.length()>0){
                                inpVal = Integer.parseInt(inputVal);
                            }
                            if ( inpVal != xIntepretedVal) {
                                noOfWrong++;
                                Toast.makeText(getBaseContext(), "Wrong input:Correct is: " + xIntepretedVal, Toast.LENGTH_SHORT).show();
                            } else {
                                canvas.drawCircle(xIntepretedPos, (eachBoxY * 9), 4, paint);
                                canvas.drawText(inpVal+"", xIntepretedPos, usrYPosDisply, paint);
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
                    noOfWrong++;
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
                    alert.setTitle("Enter Intervals for Y-axis");
                    // Set an EditText view to get user input
                    final EditText input = new EditText(this);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    alert.setView(input);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String inputVal = input.getText().toString();
                            int inpVal=0;
                            paint.setColor(Color.RED);
                            float yIntepretedPos = interpretTouchPosition(y, eachBoxY, eachBoxY);
                            float yIntepretedVal = interpretYInterval(y, eachBoxY, eachBoxY, 1);
                            Log.i("Intepreted Y: ", yIntepretedPos + "");
                            Log.i("Intepr Val: ", yIntepretedVal + "");
                            if(inputVal.length() > 0) {
                                inpVal = Integer.parseInt(inputVal);
                            }
                            if (inpVal != yIntepretedVal) {
                                Toast.makeText(getBaseContext(), "Wrong input:Correct is: " + yIntepretedVal, Toast.LENGTH_SHORT).show();
                                noOfWrong++;
                            } else {
                                canvas.drawCircle(xInitial, yIntepretedPos, 4, paint);
                                canvas.drawText(inpVal+"", usrXPosDisply, yIntepretedPos, paint);
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
                    noOfWrong++;
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
                Log.i("x: ",intepretedXVal+"");
                Log.i("y: ",intepretedYVal+"");
                int i=0;
                for(HeadingData point: pointList){
                    if(point.x == intepretedXVal && point.y == intepretedYVal){
                        flag = true;
                        if (pointTwice[i] == 0) {
                            paint.setColor(Color.GREEN);
                            canvas.drawCircle(xIntepretedPos, yIntepretedPos, 4, paint);
                            paint.setColor(Color.BLACK);
                            canvas.drawText("(" + intepretedXVal + "," + intepretedYVal + ")", xIntepretedPos, yIntepretedPos + 10, paint);
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
                    }i++;
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
        if(subQuesCount == 0){
             timeStart = System.currentTimeMillis();
        }
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
        for (int x = 0; x< colNum;x++){
            TextView headerName = new TextView(this);
            headerName.setId(0 + i);
            headerName.setText(currentQ.getMainQuesHeadList().get(x).getHeading());
            headerName.setTextColor(Color.WHITE);
            headerName.setPadding(5, 5, 5, 5);
            rowHeader.addView(headerName);
            i++;
        }
        tableLayout.addView(rowHeader);
        for(int x = 0; x< rowNum;x++){
            TableRow tableRow = new TableRow(this);
            for (int y = 0;y<colNum;y++){
                TextView textView = new TextView(this);
                textView.setId(0+i);
                textView.setText(currentQ.getMainQuesHeadList().get(y).getMainQuesHDataList().get(x).getData());
                tableRow.addView(textView);
                i++;
            }
            tableLayout.addView(tableRow);
        }


        txtExplanation.setText(null);
        btnNext.setEnabled(false);

    }
    public void drawXAxisLabel(final float x, final float y, final float xInitial,float yValue){
        if((x > (xInitial+eachBoxX))&&(y >= yValue )&&( y < (yValue + eachBoxY))){
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
                        String corectLabel = DatabaseHandler.getHeadingName(currentQ.getMqId(), "x");
                        float positionX = (eachBoxX * (lineDensity / 2)) - eachBoxX;//to make it some what in middle
                        //check if the Label is correct
                        //if (inputVal.equals(corectLabel)) {
                            paint.setColor(Color.RED);
                            canvas.drawText(inputVal, 0, inputVal.length(), positionX, usrYPosDisply, paint);
                            paint.setColor(Color.BLACK);
                            //set checkXLabel value so that it shows that we've already type in the correct x Label value
                            checkXLabel = true;
                            XLabel = corectLabel;
                       // } else {
                        //    Toast.makeText(getBaseContext(), "Opps!You may miss something!Let's try again!", Toast.LENGTH_SHORT).show();
                        //    Toast.makeText(getBaseContext(), corectLabel, Toast.LENGTH_SHORT).show();

                       // }

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
                        String corectLabel = DatabaseHandler.getHeadingName(currentQ.getMqId(),"y");
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
    public void getQuestions(int level){
        //The Question and Answer portion
        List<MainQuesHData> mainQuesHDataList;
        List<SubQuestion> subQuestionList;
        List<Options> optionsList;
        List<String> answerList;
        int noOfLevel = 6;
        if(level <= noOfLevel) {
            mainQuesList = DatabaseHandler.getAllMainQVal("Create", "Line", level);
            subQuestionList = DatabaseHandler.getSubQValueList("Create", "Line");
            for (MainQues main : mainQuesList) {
                //get the list of heading for a main question
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
            //Get  x and y axis value as points
            pointList = DatabaseHandler.getAllHeadingData(1);
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
        }else{
            Toast.makeText(getBaseContext(),"You finished learning this level",Toast.LENGTH_SHORT).show();
        }
    }
    public void drawGraph(){
        staticBitmap = null;
        noOfWrong = 0;
        timeStart =0;
        bitmap = Bitmap.createBitmap((int) width, (int) height,
                Bitmap.Config.ARGB_8888);
        downx = 0; downy = 0; upx = 0; upy = 0; checkx = 0;checky = 0; checkpoint = 0;
        checkXLabel = false; checkYLabel = false;
        XLabel ="none"; YLabel ="none";
        txtTitle.setText("");
        generateGridLines();
        drawAxes();
    }
    public void drawTitle(final float x, final float y, final float xInitial,float yValue){
        int firstTwoBoxY =  2* eachBoxY;
        if((x > (xInitial+eachBoxX))&&(y >= yValue )&&( y < (yValue + eachBoxY))){
            //Debug purpose
            Log.i("Touch pts: ", x + "");
            Log.i("Touch pts: ", y + "");
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText input = new EditText(this);
            if(txtTitle.getText().length() == 0){
                alert.setTitle("Enter Title");
            }else{
                alert.setTitle("Re-enter Title:");
            }
            // Set an EditText view to get user input

            alert.setView(input);

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String inputVal = input.getText().toString();
                    txtTitle.setText(inputVal);
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
            Toast.makeText(getBaseContext(),"Enter the title by clicking on top portion of the graph ",Toast.LENGTH_SHORT).show();
        }
    }
}