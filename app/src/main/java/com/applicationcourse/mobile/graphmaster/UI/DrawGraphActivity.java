package com.applicationcourse.mobile.graphmaster.UI;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.applicationcourse.mobile.graphmaster.Database.DatabaseHandler;
import com.applicationcourse.mobile.graphmaster.Database.HeadingData;
import com.applicationcourse.mobile.graphmaster.Database.Help;
import com.applicationcourse.mobile.graphmaster.Database.MainQues;
import com.applicationcourse.mobile.graphmaster.Database.MainQuesHData;
import com.applicationcourse.mobile.graphmaster.Database.MainQuesHeading;
import com.applicationcourse.mobile.graphmaster.Database.Options;
import com.applicationcourse.mobile.graphmaster.Database.Progress;
import com.applicationcourse.mobile.graphmaster.Database.SubQuestion;
import com.applicationcourse.mobile.graphmaster.R;
import com.applicationcourse.mobile.graphmaster.Util.Util;
import com.dd.CircularProgressButton;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import at.markushi.ui.CircleButton;

public class DrawGraphActivity  extends AppCompatActivity implements View.OnTouchListener,SeekBar.OnSeekBarChangeListener {
    private int lineDensity = 10;
    int eachBoxX,eachBoxY,qid = 0,i = 1,grade,subQuesCount = 0, optionCount = 0, width,height;
    long timeStart =0;
    private Bitmap staticBitmap = null;
    Bitmap bitmap;
    ImageView mImageView;
    Float[] marksList;
    float score = 0;
    Canvas canvas;
    Paint paint;
    TextView txtTitle;
    float xMultiple,yMultiple;
    int setMaxX, setMaxY;
    int noOfLevel = 4;
    //check if we finish ploting all the intervals for x axis and then we'll set y axis interval touch available,
    //first enable x axis, then y then areas between 2 axis
    float downx = 0, downy = 0, upx = 0, upy = 0, XEnd, YEnd, checkx,checky, checkpoint = 0;
    int currentColor = -3355444;
    List<MainQues> mainQuesList;
    MainQues currentQ;
    List<HeadingData> pointList;
    WebView txtSubQues,txtQuestion;
    TextView txtExplanation;
    EditText editTextDyn;
    List<MainQuesHeading> mainQuesHeadingList;
    boolean checkXLabel = false,checkYLabel = false;
    //check if the point has already been plotted
    int xpointTwice[],ypointTwice[],pointTwice[];
    RelativeLayout seekRelative;
    private SeekBar seekBarX,seekBarY;
    private TextView tvXMax,tvYMax;
    int xLowerValue,xHigherValue, yLowerValue, yHigherValue;
    CircleButton textButton,pictureButton;
    CircleButton menuButton;
    //Yolanda 1 line
    CircleButton calButton;
    float step;
    String XLabel,YLabel,xLabelValue, yLabelValue;
    final String[] intervalue = new String[1];
    final Boolean[] getValue = {false};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_graph);
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        width = (metrics.widthPixels);
        height = ((metrics.heightPixels) / 2);
        mImageView = (ImageView) findViewById(R.id.ImageView01);

        txtQuestion = (WebView) findViewById(R.id.webMainQues);
        txtSubQues = (WebView) findViewById(R.id.webSubQues);
        txtExplanation = (TextView) findViewById(R.id.txtExpl);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        tvXMax = (TextView)findViewById(R.id.tvXMax);
        tvYMax = (TextView)findViewById(R.id.tvYMax);

        seekBarX = (SeekBar)findViewById(R.id.seekBarX);
        seekBarY = (SeekBar)findViewById(R.id.seekBarY);
        menuButton = (CircleButton)findViewById(R.id.menuButton);
        textButton = (CircleButton)findViewById(R.id.textButton);
        pictureButton = (CircleButton)findViewById(R.id.ImageButton);
        //Yolanda 1 line
        calButton = (CircleButton)findViewById(R.id.calButton);
        seekRelative = (RelativeLayout) findViewById(R.id.seekLayout);
        //Yolanda
        final CircularProgressButton circularButton1 = (CircularProgressButton)findViewById(R.id.circularButton1);
        //first the seek bar can not be moved
        seekBarY.setEnabled(false);
        seekBarX.setEnabled(false);
        //initialize the seekbar
        seekBarX.setProgress(0);
        seekBarY.setProgress(0);
        //get grade from Level Activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            grade = extras.getInt("grade");
        }

        drawGraph();

        getQuestions(grade);
        marksList = DatabaseHandler.getAllMarksList();        //To generate view for each main question
        //qid will keep track of current main question, subQuesCount will keep track of current subquestion

        //get the number of points the students need to plot
        //checkpoint = currentQ.getMainQuesHeadList().get(0).getMainQuesHDataList().size();
        setQuestionView();
        paint.setTextSize(25);
        //Yolanda
        circularButton1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String inputTxt = "none";
                        if ((subQuesCount < (currentQ.getSubQuestionList().size())) && (qid < mainQuesList.size())) {
                            String optionType = currentQ.getSubQuestionList().get(subQuesCount).getOptionType();
                            final int subid = (int) currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId();
                            List<String> answerList = DatabaseHandler.getAnswerList(currentQ.getMqId(), subid);
                            if (optionType.equals("Radio")) {
                                RadioGroup grp = (RadioGroup) findViewById(R.id.radioSetupSel);
                                RadioButton answer = (RadioButton) findViewById(grp.getCheckedRadioButtonId());
                                inputTxt = answer.getText().toString();
                            } else if (optionType.equals("TextBox")) {
                                //Yolanda
                                if (getValue[0])
                                    inputTxt = intervalue[0];
                            }
                            inputTxt = inputTxt.trim();
                            final Dialog rankDialog = new Dialog(DrawGraphActivity.this);

                            if (subid == 3) {
                                if ((checkXLabel == true) && (checkYLabel == true)) {
                                    //Yolanda
                                    if (circularButton1.getProgress() == 0 ){
                                        simulateSuccessProgress(circularButton1);

                                        List<String> answerList1 = DatabaseHandler.getExemplar(currentQ.getMqId(), currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId());
                                        String corectLabelEx = answerList1.get(0);
                                        MaterialDialog.Builder mb = new MaterialDialog.Builder(DrawGraphActivity.this);
                                        mb.title("Check your work");
                                        mb.content(corectLabelEx);
                                        mb.positiveText("Next");
                                        mb.negativeText("Label Again");
                                        mb.onPositive(
                                                new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                                        dialog.dismiss();
                                                        rankDialog.setContentView(R.layout.rank_dialog);
                                                        rankDialog.setCancelable(false);//Yolanda
                                                        rankDialog.setTitle("How well did you do? \nRate your answer");
                                                        final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
                                                        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                                            public void onRatingChanged(RatingBar ratingBar, float rating,
                                                                                        boolean fromUser) {
                                                                //  Toast.makeText(getBaseContext(), String.valueOf(rating), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                        Button okayStarButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                                                        okayStarButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                float point = (ratingBar.getRating());
                                                                if(point != 3)
                                                                    score += (marksList[subid - 1]) / point;
                                                                //TODO: add next code
                                                                rankDialog.dismiss();
                                                                circularButton1.setProgress(0);
                                                                Next();//Yolanda
                                                            }
                                                        });
                                                        //now that the dialog is set up, it's time to show it
                                                        rankDialog.show();
                                                        Toast.makeText(getBaseContext(), "Correct!", Toast.LENGTH_SHORT).show();
                                                        //Toast.makeText(getBaseContext(), "Correct!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                        );
                                        mb.onNegative(
                                                new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                                        dialog.dismiss();
                                                        //redraw the whole graph
                                                        circularButton1.setProgress(0);
                                                        generateGridLines();
                                                        drawAxes();
                                                        paint.setTextSize(25);
                                                        paint.setColor(Color.BLACK);
                                                        checkXLabel = false;
                                                        checkYLabel = false;
                                                    }
                                                }
                                        );
                                        mb.cancelable(false);
                                        mb.canceledOnTouchOutside(false);
                                        mb.show();
                                    }else{
                                        circularButton1.setProgress(0);
                                    }
                                } else {
                                    //Yolanda
                                    if (circularButton1.getProgress() == 0 ){
                                        simulateErrorProgress(circularButton1);

                                        new MaterialDialog.Builder(DrawGraphActivity.this)
                                                .title("Oops")
                                                .content("Please finish all the labels before going to the next question!")
                                                .positiveText("OK")
                                                .onPositive(
                                                        new MaterialDialog.SingleButtonCallback() {
                                                            @Override
                                                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                                                dialog.dismiss();
                                                                circularButton1.setProgress(0);
                                                            }
                                                        }
                                                )
                                                .show();
                                    }else{
                                        circularButton1.setProgress(0);
                                    }

                                }
                            } else if (subid == 4 || subid == 5) {
                                //Checking for the input value for interval
                                if (answerList.contains(inputTxt)) {
                                    //Yolanda
                                    if (circularButton1.getProgress() == 0 ){
                                        simulateSuccessProgress(circularButton1);
                                    }else{
                                        circularButton1.setProgress(0);
                                    }
                                    if (subid == 4) {
                                        xMultiple = Float.parseFloat(inputTxt);
                                    } else if (subid == 5) {
                                        yMultiple = Float.parseFloat(inputTxt);
                                    }
                                    String optionExpl = DatabaseHandler.getOptionExpl(currentQ.getMqId(), subid, inputTxt);
                                    new MaterialDialog.Builder(DrawGraphActivity.this)
                                            .content(optionExpl)
                                            .positiveText("Next")
                                            .onPositive(
                                                    new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(MaterialDialog dialog, DialogAction which) {
                                                            //Yolanda
                                                            dialog.dismiss();
                                                            circularButton1.setProgress(0);
                                                            Next();

                                                        }
                                                    }
                                            )
                                            .show();
                                    //display the explanation
                                } else {
                                    //Yolanda
                                    if (circularButton1.getProgress()==0){
                                        simulateErrorProgress(circularButton1);
                                        String optionExpl = DatabaseHandler.getOptionExpl(currentQ.getMqId(), subid, "-1");
                                        new MaterialDialog.Builder(DrawGraphActivity.this)
                                                .content(optionExpl)
                                                .positiveText("OK")
                                                .onPositive(
                                                        new MaterialDialog.SingleButtonCallback() {
                                                            @Override
                                                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                                                //Yolanda
                                                                editTextDyn.setText("");
                                                                dialog.dismiss();
                                                                circularButton1.setProgress(0);

                                                            }
                                                        }
                                                )
                                                .show();
                                    }else {
                                        circularButton1.setProgress(0);
                                    }
                                }
                            } else if (subid == 6) {
                                txtTitle = (TextView) findViewById(R.id.txtTitle);
                                if (txtTitle.getText().length() == 0) {
                                    if (circularButton1.getProgress()==0){
                                        simulateErrorProgress(circularButton1);
                                        new MaterialDialog.Builder(DrawGraphActivity.this)
                                                .content("Please enter the title!")
                                                .positiveText("OK")
                                                .onPositive(
                                                        new MaterialDialog.SingleButtonCallback() {
                                                            @Override
                                                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                                                dialog.dismiss();
                                                                circularButton1.setProgress(0);
                                                            }
                                                        }
                                                )
                                                .show();

                                        circularButton1.setProgress(0);
                                    }else {
                                        circularButton1.setProgress(0);
                                    }
                                } else {
                                    //Yolanda
                                    if (circularButton1.getProgress()==0){
                                        simulateSuccessProgress(circularButton1);

                                        List<String> answerList2 = DatabaseHandler.getExemplar(currentQ.getMqId(), currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId());
                                        String titleExample = answerList2.get(0);
                                        MaterialDialog.Builder mb = new MaterialDialog.Builder(DrawGraphActivity.this);
                                        mb.title("Check your work");
                                        mb.content(titleExample);
                                        mb.positiveText("Next");
                                        mb.negativeText("Do it Again");
                                        mb.onPositive(
                                                new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                                        dialog.dismiss();
                                                        rankDialog.setContentView(R.layout.rank_dialog);
                                                        rankDialog.setCancelable(true);
                                                        rankDialog.setTitle("How well did you do?\nRate your answer");
                                                        final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
                                                        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                                            public void onRatingChanged(RatingBar ratingBar, float rating,
                                                                                        boolean fromUser) {
                                                                // Toast.makeText(getBaseContext(), String.valueOf(rating), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                                                        updateButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                float point = (ratingBar.getRating());
                                                                if(point != 3)
                                                                    score += (marksList[subid - 1]) / point;
                                                                rankDialog.dismiss();
                                                                circularButton1.setProgress(0);
                                                                //Yolanda
                                                                Next();
                                                            }
                                                        });
                                                        //now that the dialog is set up, it's time to show it
                                                        rankDialog.show();
                                                    }
                                                }
                                        );
                                        mb.onNegative(
                                                new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                                        dialog.dismiss();
                                                        //remove the title
                                                        txtTitle.setText("");
                                                        circularButton1.setProgress(0);
                                                    }
                                                }
                                        );
                                        mb.cancelable(false);
                                        mb.canceledOnTouchOutside(false);
                                        mb.show();
                                    }else {
                                        circularButton1.setProgress(0);
                                    }

                                }
                            } else if (subid == 7) {
                                if (circularButton1.getProgress()==0){
                                    if (checkpoint != 0) {
                                        simulateErrorProgress(circularButton1);
                                        Toast.makeText(getBaseContext(), "Not all points are plotted", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //Yolanda
                                        simulateSuccessProgress(circularButton1);
                                        new MaterialDialog.Builder(DrawGraphActivity.this)
                                                .content("Good job! You've finished all the points! Let's go to the next step!")
                                                .positiveText("Next")
                                                .onPositive(
                                                        new MaterialDialog.SingleButtonCallback() {
                                                            @Override
                                                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                                                dialog.dismiss();
                                                                circularButton1.setProgress(0);
                                                                Next();
                                                            }
                                                        }
                                                )
                                                .show();
                                    }
                                }else {
                                    circularButton1.setProgress(0);
                                }

                            } else {
                                if (circularButton1.getProgress()==0){
                                    String optionExpl;
                                    if(grade == 4 && subQuesCount == 0) {
                                        optionExpl = DatabaseHandler.getOptionExpl(currentQ.getMqId(), 9, "none");
                                        //Make star for Label
                                        simulateSuccessProgress(circularButton1);
                                        rankDialog.setTitle("How well did you do?\nRate your answer");
                                        rankDialog.setContentView(R.layout.rank_dialog);
                                        rankDialog.setCancelable(true);
                                        final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
                                        // text.setText(optionExpl);
                                        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                            public void onRatingChanged(RatingBar ratingBar, float rating,
                                                                        boolean fromUser) {
                                                Toast.makeText(getBaseContext(), String.valueOf(rating), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        Button okayStarButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                                        okayStarButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                float point = (ratingBar.getRating());
                                                if (point != 3) {
                                                    score += (marksList[0]) / point;
                                                }                                                //TODO: add next code
                                                rankDialog.dismiss();
                                                circularButton1.setProgress(0);
                                                Next();
                                            }
                                        });
                                        //now that the dialog is set up, it's time to show it
                                        rankDialog.show();
                                        //grade4 Toast.makeText(getBaseContext(), "Please type in the label value for both X and Y axis before coming to the next question! ", Toast.LENGTH_SHORT).show();
                                    }else{
                                        optionExpl = DatabaseHandler.getOptionExpl(currentQ.getMqId(), currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId(), inputTxt);
                                        if (answerList.contains(inputTxt)) {
                                            simulateSuccessProgress(circularButton1);
                                            new MaterialDialog.Builder(DrawGraphActivity.this)
                                                    .content(optionExpl)
                                                    .positiveText("Next")
                                                    .onPositive(
                                                            new MaterialDialog.SingleButtonCallback() {
                                                                @Override
                                                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                                                    dialog.dismiss();
                                                                    circularButton1.setProgress(0);
                                                                    Next();
                                                                }
                                                            }
                                                    )
                                                    .show();
                                            //Toast.makeText(getBaseContext(), "Please type in the label value for both X and Y axis before coming to the next question! ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            simulateErrorProgress(circularButton1);
                                            score += marksList[subid-1];
                                            new MaterialDialog.Builder(DrawGraphActivity.this)
                                                    .content(optionExpl)
                                                    .positiveText("OK")
                                                    .onPositive(
                                                            new MaterialDialog.SingleButtonCallback() {
                                                                @Override
                                                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                                                    dialog.dismiss();
                                                                    circularButton1.setProgress(0);
                                                                }
                                                            }
                                                    )
                                                    .show();
                                        }
                                    }
                                }else {
                                    circularButton1.setProgress(0);
                                }


                            }
                        }

                    }
                }
        );

        menuButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get video and text guide
                        // mQuickAction.show(v);
                        //mQuickAction.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
                        //Query the database
                        long mid = currentQ.getMqId();
                        long subid = currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId();
                        Help help = new Help();
                        help = DatabaseHandler.getVideoHelp(mid, subid);
                        Uri uri = Uri.parse(help.getValue());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        Toast.makeText(getBaseContext(), "Watch the video", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        textButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DrawGraphActivity.this,ShowTextActivity.class );
                        int mid = (int)currentQ.getMqId();
                        int subid = (int)currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId();
                        Bundle bundle = new Bundle();
                        bundle.putInt("mid", mid);
                        bundle.putInt("subid", subid);
                        intent.putExtras(bundle);
                        //intent.putExtra("subid",subid);
                        startActivity(intent);
                    }
                }
        );

        pictureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        long mid = currentQ.getMqId();
                        long subid = currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId();
                        Help help = new Help();
                        help = DatabaseHandler.getImageHelp(mid, subid);
                        Bitmap imagedata = null;
                        imagedata = BitmapFactory.decodeByteArray(help.getImage(), 0, (help.getImage()).length);

                        final AlertDialog.Builder alertadd = new AlertDialog.Builder(DrawGraphActivity.this);
                        LayoutInflater factory = LayoutInflater.from(DrawGraphActivity.this);
                        final View view = factory.inflate(R.layout.showpicture, null);
                        ImageView image = (ImageView)view.findViewById(R.id.image1);
                        image.setImageBitmap(imagedata);
                        //image.setImageResource(R.drawable.first);
                        alertadd.setView(view);
                        alertadd.setNeutralButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = alertadd.create();
                        alertDialog.show();
                    }
                }
        );

        //Yolanda listener:OPEN CALCULATOR
        calButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_APP_CALCULATOR);
                startActivity(i);
            }
        });

    }

    private void simulateSuccessProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
        widthAnimation.setDuration(500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
            }
        });
        widthAnimation.start();
    }

    private void simulateErrorProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 99);
        widthAnimation.setDuration(500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
                if (value == 99) {
                    button.setProgress(-1);
                }
            }
        });
        widthAnimation.start();
    }
    //Yolanda:wanna remove all the next buttons
    public void Next(){
        if (qid < mainQuesList.size()) {
            if (subQuesCount < (currentQ.getSubQuestionList().size())) {
                if (subQuesCount > 4 && grade >= 3) {
                    seekRelative.setVisibility(View.VISIBLE);
                }

            }
            txtExplanation.setText("");
            String optionType = currentQ.getSubQuestionList().get(subQuesCount).getOptionType();
            if (optionType.equals("Radio")) {
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioSetupSel);
                radioGroup.clearCheck();
                radioGroup.removeAllViews();
            } else if (optionType.equals("TextBox")) {
                //Yolanda

                View editTextRemove = (EditText) findViewById(R.id.edittext_dynamic);
                ViewGroup parent = (ViewGroup) editTextRemove.getParent();
                if (parent != null) {
                    parent.removeView(editTextRemove);
                }

            }
            ++subQuesCount;
            TableLayout tableLayout = (TableLayout) findViewById(R.id.table);
            tableLayout.removeAllViews();

            if ( subQuesCount < currentQ.getSubQuestionList().size() ) {
                setQuestionView();
            }else {
                new MaterialDialog.Builder(this)
                        .content("Next Question")
                        .positiveText("OK")
                        .onPositive(
                                new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .show();
                //Set the previous main question no of wrong
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                String dateVal = dateFormat.format(date);
                Long timeEnd = System.currentTimeMillis();
                Long timeTaken = timeEnd - timeStart;
                String time = Util.getElapsedTime(timeTaken);
                //TODO: Student id to change
                Progress prog = new Progress(dateVal, 1, "create", grade, time, score);
                DatabaseHandler.addProgressData(prog);
                canvas.drawColor(Color.WHITE);
                subQuesCount = 0;
                drawGraph();
                //Reset the subquestion count back to zero
                //Check the progress before proceeding
                int mainid = (int)currentQ.getMqId();
                String progress = DatabaseHandler.getProgressResult(1,mainid, "create", grade, time, "00:07:00");
                if (progress.equals("continueSmeLevel")) {
                    if (qid >= (mainQuesList.size()-1)) {
                        qid = 0;

                    } else {
                        qid++;
                    }
                    subQuesCount = 0;
                    currentQ = mainQuesList.get(qid);

                    setQuestionView();
                }else if (progress.equals("promoteLevel")) {
                    if(grade < noOfLevel-1) {
                        grade+=2;
                        qid = 0;
                        getQuestions(grade);
                        setQuestionView();
                    }else if(grade == noOfLevel - 1){
                        grade++;
                        qid = 0;
                        getQuestions(grade);
                        setQuestionView();

                    }
                }else if (progress.equals("nextLevel")) {
                    if(grade < noOfLevel-1) {
                        grade++;
                        qid = 0;
                        getQuestions(grade);
                        setQuestionView();
                    }else if(grade == noOfLevel - 1){
                        grade++;
                        qid = 0;
                        getQuestions(grade);
                        setQuestionView();

                    }
                } else if (progress.equals("lowerLevel")) {
                    //if its in level > 1 then only it can lower the grade else continue in same level
                    if(grade > 1) {
                        grade--;
                        qid = 0;
                        getQuestions(grade);
                        setQuestionView();

                    }else{
                        if (qid == mainQuesList.size()) {
                            qid = 0;
                        } else {
                            qid++;
                        }
                        setQuestionView();
                    }
                }
            }
        } else {
            Toast.makeText(getBaseContext(), "You finished learning how to create graph!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DrawGraphActivity.this, MainActivity.class);
            startActivity(intent);        }
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
        //Draw zero
        paint.setTextSize(25);
        final float usrYPosDisply = ((eachBoxY * 9) + ((eachBoxY * 9)) / 20);
        canvas.drawText("0", eachBoxX, usrYPosDisply, paint);
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
        if (subid >= 3) {
            int action = event.getAction();
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

                    if(subid ==3){
                        if (checkXLabel==false)
                            drawXAxisLabel(downx, downy, eachBoxX, (eachBoxY * 9));
                        else {
                            if(checkYLabel==false){
                                drawYAxisLabel(downx, downy, eachBoxY, eachBoxX);
                            }
                        }
                    }else if(subid == 6){
                        drawTitle(downx, downy, eachBoxX, (eachBoxY * 9));
                    }
                    else if(subid ==7){
                        if (checkx != 0) {
                            drawXAxisPoints(downx, downy, eachBoxX, (eachBoxY * 9));
                        }else{
                            if (checky != 0) {
                                drawYAxisPoints(downx, downy, eachBoxY, eachBoxX);
                            }else{
                                //teresa
                                if (checkpoint!=0 && grade < 3){
                                    drawPoints(downx, downy, eachBoxX, eachBoxY, 1);
                                }
                                else
                                if (checkpoint!=0 && grade >=3 ){
                                    seekBarX.setEnabled(true);
                                    seekBarY.setEnabled(true);
                                    seekBarX.setOnSeekBarChangeListener(this);
                                    seekBarY.setOnSeekBarChangeListener(this);
                                    seekBarX.setProgress(0);
                                    seekBarY.setProgress(0);
                                    //Set min and max of the seekBar
                                    float xIntepretedPos = interpretTouchPosition(downx, eachBoxX, eachBoxX);
                                    int xIntepretedVal = interpretXInterval(xIntepretedPos, eachBoxX, eachBoxX, 1);
                                    float yIntepretedPos = interpretTouchPosition(downy, eachBoxY, eachBoxY);
                                    int yIntepretedVal = interpretYInterval(yIntepretedPos, eachBoxY, eachBoxY, 1);

                                    if(xIntepretedVal >= 1){
                                        xLowerValue = (xIntepretedVal - 1);
                                    }else{
                                        xLowerValue = 0;
                                    }
                                    if(yIntepretedVal >= 1){
                                        yLowerValue = (yIntepretedVal -1);
                                    }else {
                                        yLowerValue = 0;
                                    }
                                    if(xIntepretedVal < 7){
                                        xHigherValue = (xIntepretedVal +1);
                                    }else{
                                        xHigherValue = 7;
                                    }
                                    if(yIntepretedVal < 7){
                                        yHigherValue = (yIntepretedVal +1);
                                    }else{
                                        yHigherValue = 7;
                                    }
                                    step = (float)0.1;
                                    setMaxX= (int)((xHigherValue - xLowerValue)/step);
                                    setMaxY = (int)((yHigherValue -yLowerValue)/step);
                                    seekBarX.setProgress(setMaxX/2);
                                    seekBarY.setProgress(setMaxY / 2);
                                    seekBarX.setMax(setMaxX);
                                    seekBarY.setMax(setMaxY);
                                   /* seekBarX.setProgress(xLowerValue);
                                    seekBarY.setProgress(yLowerValue);
                                    seekBarX.setMax(xHigherValue);
                                    seekBarY.setMax(yHigherValue);*/
                                    if (checkpoint != 0) {
                                        if (checkpoint== checkx){
                                            new MaterialDialog.Builder(this)
                                                    .content("Please move the x and y seekbar and click the graph to add points!")
                                                    .positiveText("OK")
                                                    .onPositive(
                                                            new MaterialDialog.SingleButtonCallback() {
                                                                @Override
                                                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                                                    dialog.dismiss();
                                                                }
                                                            }
                                                    )
                                                    .show();
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

                final int subid =(int) currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId();
                final float usrYPosDisply = (yValue + (yValue) / 20);  //For displaying the entered point by user at pos lower than the x-axis
                final String[] xlabel =new String[1];
                final MaterialDialog.Builder mb = new MaterialDialog.Builder(this);
                mb.title("X-axis interval");
                mb.content("Enter Intervals for the X-axis");
                mb.inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL  );
                mb.positiveText("Submit");
                mb.negativeText("Cancel");
                mb.alwaysCallInputCallback();
                mb.input(0, 0, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (input.toString().equals("")){
                            dialog.setContent("Interval can't be empty!");
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                        }else{
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                            xlabel[0] = input.toString();
                        }
                    }
                }).show();

                float xIntepretedPos = interpretTouchPosition(x, eachBoxX, eachBoxX);
                float xIntepretedVal = interpretXInterval(xIntepretedPos, eachBoxX, eachBoxX, 1);

                if (xpointTwice[(int) xIntepretedVal - 1] == 0) {
                    mb.onPositive(
                            new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    String inputTxt = xlabel[0];
                                    float inpVal=0;
                                    float xIntepretedPos = interpretTouchPosition(x, eachBoxX, eachBoxX);
                                    float xIntepretedVal = interpretXInterval(xIntepretedPos, eachBoxX, eachBoxX, 1);
                                    Log.i("Intepreted X: ", xIntepretedPos + "");
                                    Log.i("Intepr Val: ", xIntepretedVal + "");

                                    if(inputTxt.length()>0){
                                        inpVal = Float.parseFloat(inputTxt);
                                    }
                                    DecimalFormat df = new DecimalFormat();
                                    df.setMaximumFractionDigits(2);
                                    float interpretedX = Float.parseFloat(df.format(xIntepretedVal* xMultiple)); //only two decimal place
                                    if ( (inpVal - interpretedX) != 0.0) {
                                        score += (marksList[subid - 1]);

                                        Toast.makeText(getBaseContext(), "Wrong input! Try again", Toast.LENGTH_SHORT).show();
                                    } else {
                                        canvas.drawCircle(xIntepretedPos, (eachBoxY * 9), 4, paint);
                                        canvas.drawText(inputTxt, xIntepretedPos, usrYPosDisply, paint);
                                        Toast.makeText(getBaseContext(), "Correct! ", Toast.LENGTH_SHORT).show();
                                        //we decrease the count number of x axis interval
                                        checkx--;
                                        //we set this interval unavailable,in ther words,if we touch this interval again ,a toast will appear and the text will not change
                                        xpointTwice[(int) xIntepretedVal - 1] = 1;
                                    }

                                    dialog.dismiss();
                                    paint.setColor(Color.BLACK);

                                }
                            }
                    );
                    mb.onNegative(
                            new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    dialog.dismiss();
                                }
                            }
                    );


                } else {
                    new MaterialDialog.Builder(this)
                            .content("You've already set this point !Let's choose other points!")
                            .positiveText("OK")
                            .onPositive(
                                    new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(MaterialDialog dialog, DialogAction which) {
                                            dialog.dismiss();
                                        }
                                    }
                            )
                            .show();
                }
            }else{
                new MaterialDialog.Builder(this)
                        .content("Please finish x axis interval first!")
                        .positiveText("OK")
                        .onPositive(
                                new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .show();

            }
        } else {
            new MaterialDialog.Builder(this)
                    .content("Please touch on the x axis to type in the interval value!")
                    .positiveText("OK")
                    .onPositive(
                            new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    dialog.dismiss();
                                }
                            }
                    )
                    .show();
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

                final int subid = (int) currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId();

                final float usrXPosDisply = (xInitial - (xInitial) / 2.5f);  //For displaying the entered point by user at pos lower than the x-axis
                float yIntepretedVal = interpretYInterval(y, eachBoxY, eachBoxY, 1);
                if (ypointTwice[(int) yIntepretedVal - 1] == 0) {
                    final String[] xlabel =new String[1];
                    final MaterialDialog.Builder mb = new MaterialDialog.Builder(this);
                    mb.title("Y-axis interval");
                    mb.content("Enter Intervals for the Y-axis");
                    mb.inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    mb.positiveText("Submit");
                    mb.negativeText("Cancel");
                    mb.alwaysCallInputCallback();
                    mb.input(0, 0, false, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            if (input.toString().equals("")){
                                dialog.setContent("Interval can't be empty!");
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                            }else{
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                                xlabel[0] = input.toString();
                            }
                        }
                    }).show();
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Enter Intervals for Y-axis");
                    // Set an EditText view to get user input
                    final EditText input = new EditText(this);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    alert.setView(input);
                    mb.onPositive(
                            new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    String inputTxt = xlabel[0];
                                    float inpVal = 0;
                                    float yIntepretedPos = interpretTouchPosition(y, eachBoxY, eachBoxY);
                                    float yIntepretedVal = interpretYInterval(y, eachBoxY, eachBoxY, 1);
                                    Log.i("Intepreted Y: ", yIntepretedPos + "");
                                    Log.i("Intepr Val: ", yIntepretedVal + "");
                                    if (inputTxt.length() > 0) {
                                        inpVal = Float.parseFloat(inputTxt);
                                    }
                                    DecimalFormat df = new DecimalFormat();
                                    df.setMaximumFractionDigits(2);
                                    float interpretedY = Float.parseFloat(df.format(yIntepretedVal* yMultiple)); //
                                    if ((inpVal - interpretedY) != 0.0) {
                                        Toast.makeText(getBaseContext(), "Wrong input! Try again", Toast.LENGTH_SHORT).show();
                                        score += (marksList[subid - 1]);
                                    } else {
                                        canvas.drawCircle(xInitial, yIntepretedPos, 4, paint);
                                        canvas.drawText(inputTxt + "", usrXPosDisply, yIntepretedPos, paint);
                                        Toast.makeText(getBaseContext(), "Correct interval value! ", Toast.LENGTH_SHORT).show();
                                        checky--;
                                        ypointTwice[(int) yIntepretedVal - 1] = 1;
                                    }
                                    dialog.dismiss();
                                    paint.setColor(Color.BLACK);
                                }
                            }
                    );
                    mb.onNegative(
                            new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    dialog.dismiss();
                                }
                            }
                    );

                } else {
                    new MaterialDialog.Builder(this)
                            .content("You've already set this point !Let's choose other points!")
                            .positiveText("OK")
                            .onPositive(
                                    new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(MaterialDialog dialog, DialogAction which) {
                                            dialog.dismiss();
                                        }
                                    }
                            )
                            .show();
                }
            }else {
                new MaterialDialog.Builder(this)
                        .content("Lets finish y axis first!")
                        .positiveText("OK")
                        .onPositive(
                                new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .show();

            }
        } else {
            new MaterialDialog.Builder(this)
                    .content("Please touch on the y axis to type in the interval value!")
                    .positiveText("OK")
                    .onPositive(
                            new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    dialog.dismiss();
                                }
                            }
                    )
                    .show();
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
                    if(point.x == intepretedXVal*xMultiple && point.y == intepretedYVal*yMultiple){
                        flag = true;
                        if (pointTwice[i] == 0) {
                            paint.setColor(Color.GREEN);
                            canvas.drawCircle(xIntepretedPos, yIntepretedPos, 4, paint);
                            paint.setColor(Color.BLACK);
                            canvas.drawText("(" + point.x + "," + point.y + ")", xIntepretedPos, yIntepretedPos + 10, paint);
                            checkpoint--;
                            pointTwice[i] = 1;
                        } else {
                            new MaterialDialog.Builder(this)
                                    .content("This point has already been plotted!Lets choose another point!")
                                    .positiveText("OK")
                                    .onPositive(
                                            new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                                    dialog.dismiss();
                                                }
                                            }
                                    )
                                    .show();
                            break;
                        }
                    }i++;
                }
                if (flag == false) {
                    new MaterialDialog.Builder(this)
                            .content("Oops! The point location is not correct.")
                            .positiveText("OK")
                            .onPositive(
                                    new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(MaterialDialog dialog, DialogAction which) {
                                            dialog.dismiss();
                                        }
                                    }
                            )
                            .show();

                }
            }else{
                new MaterialDialog.Builder(this)
                        .content("You haven't finish plotting all the points!")
                        .positiveText("OK")
                        .onPositive(
                                new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .show();
            }

        }

    }


    private void setQuestionView() {
        //Yolanda 1 line
        calButton.setVisibility(View.INVISIBLE);

        if(subQuesCount == 0){
            timeStart = System.currentTimeMillis();
        }
        String text = "<div align=\"justify\">"
                + currentQ.getQuestion()
                + "</div> ";
        String text1 ="<div>"+(currentQ.getSubQuestionList().get(subQuesCount).getSubQuestion())+"</div>";
        txtQuestion.loadData(String.format("%s", text), "text/html", "utf-8");
        txtSubQues.loadData(String.format("%s", text1), "text/html", "utf-8");
        //Need to dynamically generate the radio options
/////////////////////////////////generate the option buttons//////////////////////////////////////
        // List<Options> optionsList = currentQ.getSubQuestionList().get(subQuesCount).getOptionsList();
        List<Options> optionsList = DatabaseHandler.getOptionList(currentQ.getMqId(),currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId());
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
            //Yolanda
            if(subQuesCount==3||subQuesCount==4){
                calButton.setVisibility(View.VISIBLE);
            }
            LinearLayout layoutDynamic = (LinearLayout) findViewById(R.id.layoutDynamic);
            editTextDyn = new EditText(getApplicationContext());
            editTextDyn.setId(R.id.edittext_dynamic);
            layoutDynamic.addView(editTextDyn);
            editTextDyn.setOnTouchListener(
                    new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                editTextDyn.setText("");
                                final MaterialDialog.Builder mb = new MaterialDialog.Builder(DrawGraphActivity.this);
                                if (subQuesCount == 0 && grade == 4) {
                                    mb.title("Enter the variable");
                                    mb.inputType(InputType.TYPE_CLASS_TEXT);
                                } else if (subQuesCount == 3) {
                                    mb.title("X Axis Interval");
                                    mb.content("Please type in the x axis value.");
                                    mb.inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);


                                } else if (subQuesCount == 4) {
                                    mb.title("Y Axis Interval");
                                    mb.content("Please type in the y axis value.");
                                    mb.inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);


                                } else if (grade == 4 && subQuesCount == 0) {
                                    mb.title("Independent value");
                                    mb.content("Please type in the value.");
                                    mb.inputType(InputType.TYPE_CLASS_TEXT |
                                            InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                                            InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                                }
                                mb.cancelable(false);
                                mb.canceledOnTouchOutside(false);
                                mb.positiveText("Submit");
                                mb.negativeText("Cancel");
                                mb.alwaysCallInputCallback();
                                mb.input(0, 0, false, new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(MaterialDialog dialog, CharSequence input) {
                                        if (input.toString().equals("")) {
                                            dialog.setContent("This value can't be empty!");
                                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                        } else {
                                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                                            intervalue[0] = input.toString();
                                        }
                                    }
                                }).show();
                                mb.onPositive(
                                        new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                                getValue[0] = true;
                                                editTextDyn.setText(intervalue[0]);
                                            }
                                        }
                                );
                                mb.onNegative(
                                        new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                                dialog.dismiss();
                                            }
                                        }
                                );
                            }
                            return true;
                        }
                    }
            );

        }

        ////////////////////////////////////////generate the table/////////////////////////////////////////


        int rowNum = currentQ.getMainQuesHeadList().get(0).getMainQuesHDataList().size();
        int colNum = currentQ.getMainQuesHeadList().size();
        TableLayout tableLayout = (TableLayout)findViewById(R.id.table);
        //set the header of the table
        TableRow rowHeader = new TableRow(this);
        rowHeader.setId(0);
        rowHeader.setBackgroundColor(Color.parseColor("#970031"));
        for (int x = 0; x< colNum;x++){
            TextView headerName = new TextView(this);
            headerName.setId(0 + i);
            headerName.setWidth(300);
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
                textView.setGravity(Gravity.CENTER);
                textView.setWidth(300);
                textView.setBackgroundColor(Color.WHITE);
                if(x%2!=0) {
                    textView.setBackgroundColor(Color.parseColor("#f5f5f0"));
                }
                textView.setId(0+i);
                textView.setText(currentQ.getMainQuesHeadList().get(y).getMainQuesHDataList().get(x).getData());
                tableRow.addView(textView);
                i++;
            }
            tableLayout.addView(tableRow);
        }


        txtExplanation.setText(null);

    }
    public void drawXAxisLabel(final float x, final float y, final float xInitial,float yValue){
        if((x > (xInitial+eachBoxX))&&(y > yValue )&&( y < (yValue + eachBoxY))){
            //Debug purpose
            Log.i("Touch pts: ", x + "");
            Log.i("Touch pts: ", y + "");

            final float usrYPosDisply= (yValue + (yValue)/10 );  //For displaying the entered Label by user at pos lower than the x-axis
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            MaterialDialog.Builder mb = new MaterialDialog.Builder(this);
            final String[] xlabel = new String[1];
            mb.title("Label");
            mb.content("Enter Y-axis Label");
            mb.inputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                    InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            mb.positiveText("Submit");
            mb.alwaysCallInputCallback(); // this forces the callback to be invoked with every input change
            mb.input(0, 0, false, new MaterialDialog.InputCallback() {
                @Override
                public void onInput(MaterialDialog dialog, CharSequence input) {
                    if (input.toString().equals("")){
                        dialog.setContent("Label can't be empty!");
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                    }else{
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                        xlabel[0] = input.toString();
                    }
                }
            }).show();
            alert.setTitle("Enter X-axis Label");
            // Set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);

            if (checkXLabel==false) {
                mb.onPositive(
                        new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                String inputVal = xlabel[0];
                                xLabelValue = inputVal;
                                inputVal.trim();
                                List<String> answerList = DatabaseHandler.getExemplar(currentQ.getMqId(), currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId());
                                String corectLabelEx = answerList.get(0);
                                //String corectLabelEx = DatabaseHandler.getHeadingName(currentQ.getMqId(), "x");
                                float positionX = (eachBoxX * (lineDensity / 2)) - eachBoxX;//to make it some what in middle
                                //check if the Label is correct
                                if (inputVal.length() > 0) {
                                    paint.setColor(Color.BLACK);
                                    canvas.drawText(inputVal, 0, inputVal.length(), positionX, usrYPosDisply, paint);
                                    paint.setColor(Color.BLACK);
                                    XLabel = inputVal;//Yolanda
                                    //set checkXLabel value so that it shows that we've already type in the correct x Label value
                                    checkXLabel = true;
                                    //txtExplanation.setText(corectLabelEx);
                                    //XLabel = corectLabelEx;
                                } else {
                                    Toast.makeText(getBaseContext(), "Oops!You may miss something!Let's try again!", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getBaseContext(), corectLabelEx, Toast.LENGTH_SHORT).show();

                                }

                                dialog.dismiss();
                            }
                        }
                );

            }else{
                new MaterialDialog.Builder(this)
                        .content("You've already type in the Y Label!Let's go to the next step!")
                        .positiveText("OK")
                        .onPositive(
                                new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .show();
            }
        }else{
            new MaterialDialog.Builder(this)
                    .content("Touch Below X-Axis!")
                    .positiveText("OK")
                    .onPositive(
                            new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    dialog.dismiss();
                                }
                            }
                    )
                    .show();
        }
    }

    public void drawYAxisLabel(final float x, final float y, final float yInitial,float xValue){
        if((y > (yInitial+eachBoxY))&&(x > 0)&&( x < eachBoxX )){
            //Debug purpose
            Log.i("Touch pts: ", x + "");
            Log.i("Touch pts: ", y + "");
            final float xInitial = eachBoxX;
            final String[] xlabel = new String[1];
            final float usrXPosDisply= (xInitial - (xInitial)/2 );  //For displaying the entered point by user at pos lower than the x-axis
            MaterialDialog.Builder mb = new MaterialDialog.Builder(this);
            mb.title("Label");
            mb.content("Enter Y-axis Label");
            mb.inputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                    InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            mb.positiveText("Submit");
            mb.alwaysCallInputCallback(); // this forces the callback to be invoked with every input change
            mb.input(0, 0, false, new MaterialDialog.InputCallback() {
                @Override
                public void onInput(MaterialDialog dialog, CharSequence input) {
                    if (input.toString().equals("")){
                        dialog.setContent("Label can't be empty!");
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                    }else{
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                        xlabel[0] = input.toString();
                    }
                }
            }).show();

            if (checkYLabel==false) {
                mb.onPositive(
                        new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                String inputVal = xlabel[0];
                                yLabelValue = inputVal;
                                inputVal.trim();
                                List<String> answerList = DatabaseHandler.getExemplar(currentQ.getMqId(), currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId());
                                String corectLabelEx = answerList.get(0);
                                float positionY = (eachBoxY * (lineDensity / 2)) - eachBoxY;//to make it some what in middle
                                //check the Y label
                                paint.setColor(Color.BLACK);
                                canvas.save();
                                canvas.rotate(-90, 120, 90);
                                canvas.drawText(inputVal, -150, 0, paint);
                                canvas.restore();
                                YLabel = inputVal;
                                paint.setColor(Color.BLACK);
                                checkYLabel = true;
                                //txtExplanation.setText(corectLabelEx);
                                //Toast.makeText(getBaseContext(),corectLabelEx, Toast.LENGTH_LONG).show();
                                //YLabel = corectLabelEx;
                                dialog.dismiss();
                            }
                        }
                );

            }else{
                new MaterialDialog.Builder(this)
                        .content("You've already type in the Y Label!Let's go to the next step!")
                        .positiveText("OK")
                        .onPositive(
                                new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .show();
            }
        } else {
            new MaterialDialog.Builder(this)
                    .content("Learn to plot Y-Axis Label. Click near the Y-axis!")
                    .positiveText("OK")
                    .onPositive(
                            new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    dialog.dismiss();
                                }
                            }
                    )
                    .show();
        }
    }
    public void getQuestions(int level){
        //The Question and Answer portion
        List<MainQuesHData> mainQuesHDataList;
        List<SubQuestion> subQuestionList;
        subQuesCount = 0;

        if(level <= noOfLevel) {
            mainQuesList = DatabaseHandler.getAllMainQVal("Create", "Line", level);
            if(grade < 4){
                subQuestionList = DatabaseHandler.getSubQValueList("Create", "Line");
            }else{
                subQuestionList = DatabaseHandler.getSubQValueListGrade4("Create", "Line");
            }
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
                //set the subquestion
                main.setSubQuestionList(subQuestionList);
            }

            //Get  x and y axis value as points
            currentQ = mainQuesList.get(qid);
            pointList = DatabaseHandler.getAllHeadingData((int)currentQ.getMqId());
            checkpoint = pointList.size();
            //set the check value
            checkx = 7;
            checky =7;

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
            }
        }else {
            Toast.makeText(getBaseContext(), "You finished learning this level", Toast.LENGTH_SHORT).show();
        }
    }

    public void drawGraph(){
        staticBitmap = null;
        timeStart =0;
        bitmap = Bitmap.createBitmap((int) width, (int) height,
                Bitmap.Config.ARGB_8888);
        downx = 0; downy = 0; upx = 0; upy = 0; checkx = 0;checky = 0; checkpoint = 0;
        checkXLabel = false; checkYLabel = false;
        txtTitle.setText("");
        seekRelative.setVisibility(View.GONE);

        generateGridLines();
        drawAxes();
    }
    public void drawTitle(final float x, final float y, final float xInitial,float yValue){
        int firstTwoBoxY =  2* eachBoxY;
        if((x > (xInitial+eachBoxX))&&(y >= yValue )&&( y < (yValue + eachBoxY))){
            //Debug purpose
            //Yolanda
            Log.i("Touch pts: ", x + "");
            Log.i("Touch pts: ", y + "");
            List<String> answerList = DatabaseHandler.getExemplar(currentQ.getMqId(), currentQ.getSubQuestionList().get(subQuesCount).getSubQuesId());
            String titleExample = answerList.get(0);
            final String[] xlabel = new String[1];
            final MaterialDialog.Builder mb = new MaterialDialog.Builder(this);
            mb.title("Enter Title");
            mb.inputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                    InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            mb.positiveText("Submit");
            mb.negativeText("Cancel");
            mb.alwaysCallInputCallback(); // this forces the callback to be invoked with every input change
            mb.input(0, 0, false, new MaterialDialog.InputCallback() {
                @Override
                public void onInput(MaterialDialog dialog, CharSequence input) {
                    if (input.toString().equals("")){
                        dialog.setContent("Title can't be empty!");
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                    }else{
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                        xlabel[0] = input.toString();
                    }
                }
            }).show();

            mb.onPositive(
                    new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            String inputVal = xlabel[0];
                            txtTitle.setGravity(Gravity.CENTER);
                            txtTitle.setText(inputVal);
                            dialog.dismiss();
                        }
                    }
            );
            mb.onNegative(
                    new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            dialog.dismiss();
                        }
                    }
            );
            //txtExplanation.setText(titleExample);
        }else{
            Toast.makeText(getBaseContext(),"Enter the title by clicking on bottom portion of the graph ",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        //generate the graph again
        generateGridLines();
        drawAxes();
        //draw y-label
        canvas.save();
        canvas.rotate(-90, 120, 90);
        canvas.drawText(yLabelValue, -150, 0, paint);
        canvas.restore();
        //draw x-label
        float positionX = (eachBoxX * (lineDensity / 2)) - eachBoxX;
        float usrYPosDisply= (eachBoxY*10 - (eachBoxY)/10 );
        canvas.drawText(xLabelValue, 0, xLabelValue.length(), positionX, usrYPosDisply, paint);
        //draw all the interval values
        for (int i = 1;i<8;i++){
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            String valueX = df.format(xMultiple * i);
            String valueY = df.format(yMultiple*i);
            float usrXPosDisply = (eachBoxX - (eachBoxX) / 2.5f);
            canvas.drawCircle(eachBoxX * (i + 1), (eachBoxY * 9), 4, paint);
            //teresa
            canvas.drawText(valueX, eachBoxX  * (i + 1), (eachBoxY * 9 + eachBoxY / 2), paint);
            canvas.drawCircle(eachBoxX , eachBoxY *(9-i) , 4, paint);
            canvas.drawText(valueY, usrXPosDisply, eachBoxY * (9 -i), paint);
        }

        //redraw the point we've putted on the graph
        for (int i = 0;i<pointTwice.length;i++){
            if (pointTwice[i]==1){
                int calculatedX = (int) (eachBoxX * ((pointList.get(i).x) / xMultiple) + eachBoxX);
                int calculatedY = (int) (height - (eachBoxY * ((pointList.get(i).y) / yMultiple)) - eachBoxY);
                paint.setColor(Color.GREEN);
                canvas.drawCircle(calculatedX, calculatedY, 4, paint);
                paint.setColor(Color.BLACK);
                canvas.drawText("(" + (pointList.get(i).x) + "," + (pointList.get(i).y) + ")", calculatedX, (calculatedY + 14), paint);
            }
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        float valueY = Float.parseFloat(df.format(yLowerValue + (seekBarY.getProgress() * step)));
        float valueX = Float.parseFloat(df.format(xLowerValue + (seekBarX.getProgress() * step)));


        //  tvXMax.setText("" + valueX);
        // tvYMax.setText("" + valueY);
        //DRAW THE RED LINE FOR BOTH X AND Y SCROLL BAR
        Paint dashLine = new Paint();
        dashLine.setARGB(255, 0, 0, 0);
        dashLine.setStyle(Paint.Style.STROKE);
        dashLine.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        dashLine.setColor(Color.RED);
        dashLine.setStrokeWidth(2);

        canvas.drawLine((float) (eachBoxX * (valueX + 1.0)), eachBoxY, (float) (eachBoxX * (valueX + 1.0)), eachBoxY * 9, dashLine);
        canvas.drawLine(eachBoxX * 1, (float) (eachBoxY * (9.0 - valueY)), eachBoxX * 9,  (float) (eachBoxY*(9.0-valueY)), dashLine);

        //redraw
        mImageView.invalidate();

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    public void drawPointsScrollBar(final float xvalue,final float yvalue, final float xInitial, final float yInitial, final int multiple) {

        /* This function is used to drawPoints in the area in the axis*/
        Button btnPoint = (Button) findViewById(R.id.btnPoint);
        btnPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                if ((xvalue > xInitial && yvalue > yInitial) && (xvalue < XEnd) && yvalue < YEnd) {
                    //now we need to check if x value and y value are correct
                    if (checkpoint != 0) {

                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(2);
                        float valueX = Float.parseFloat(df.format((xLowerValue + (seekBarX.getProgress() * step))*xMultiple));
                        float valueY = Float.parseFloat(df.format((yLowerValue + (seekBarY.getProgress() * step)) * yMultiple));


                        for (int i = 0; (i < pointList.size()) && flag == false; i++) {
                            float coresXdata = pointList.get(i).x;
                            float errorAllowed = (float) (.2 * coresXdata);
                            if (((coresXdata - errorAllowed) < valueX) && (valueX < (coresXdata + errorAllowed))) {
                                float coresYdata = pointList.get(i).y;
                                errorAllowed = (float) (.2 * coresYdata);
                                if (((coresYdata - errorAllowed) < valueY) && (valueY < (coresYdata + errorAllowed))) {
                                    flag = true;
                                    if (pointTwice[i] == 0) {
                                        paint.setColor(Color.GREEN);
                                        int calculatedX = (int) (eachBoxX * ((pointList.get(i).x) / xMultiple) + eachBoxX);
                                        int calculatedY = (int) (height - (eachBoxY * ((pointList.get(i).y) / yMultiple)) - eachBoxY);
                                        canvas.drawCircle(calculatedX, calculatedY, 4, paint);
                                        paint.setColor(Color.BLACK);
                                        canvas.drawText("(" + (pointList.get(i).x) + "," + (pointList.get(i).y) + ")", calculatedX, (calculatedY + 14), paint);
                                        checkpoint--;
                                        pointTwice[i] = 1;
                                    } else {
                                        new MaterialDialog.Builder(DrawGraphActivity.this)
                                                .content("This point has already been plotted!Lets choose another point!")
                                                .positiveText("OK")
                                                .onPositive(
                                                        new MaterialDialog.SingleButtonCallback() {
                                                            @Override
                                                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                                                dialog.dismiss();
                                                            }
                                                        }
                                                )
                                                .show();
                                        break;
                                    }
                                }
                            }
                            seekBarX.setProgress(0);
                            seekBarY.setProgress(0);
                            mImageView.invalidate();
                        }
                        if (flag == false) {
                            new MaterialDialog.Builder(DrawGraphActivity.this)
                                    .content("Oops!the point location is not correct!")
                                    .positiveText("OK")
                                    .onPositive(
                                            new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                                    dialog.dismiss();
                                                }
                                            }
                                    )
                                    .show();
                        }
                    } else {
                        new MaterialDialog.Builder(DrawGraphActivity.this)
                                .content("You haven't finish plotting all the points!")
                                .positiveText("OK")
                                .onPositive(
                                        new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                                dialog.dismiss();
                                            }
                                        }
                                )
                                .show();
                    }
                    //redraw
                    mImageView.invalidate();
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DrawGraphActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}