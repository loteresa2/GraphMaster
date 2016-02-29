package com.applicationcourse.mobile.graphmaster.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.applicationcourse.mobile.graphmaster.R;

public class DrawGraphActivity  extends AppCompatActivity implements View.OnTouchListener  {


    private int lineDensity = 10;
    private int width = 1000;
    private int height = 1000;
    private Bitmap staticBitmap = null;
    Bitmap bitmap;
    private boolean usingImage = false;
    ImageView mImageView;
    Canvas canvas;
    Paint paint;
    float downx = 0, downy = 0, upx = 0, upy = 0;
    int currentColor = -3355444;
    static float X;
    static float Y;
    static float X1;
    int firstPoint = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_graph);
        bitmap = Bitmap.createBitmap((int) width, (int) height,
                Bitmap.Config.ARGB_8888);

        mImageView = (ImageView) findViewById(R.id.ImageView01);
        regenLines();
    }

    public void regenLines() {
        //updateHeightWidth();


        int rgbSize = width * height;
        int[] rgbValues = new int[rgbSize];
        for (int i = 0; i < rgbSize; i++) {
            rgbValues[i] = calculatePixelValue(i);
        }
        // set the imageview to the static
        staticBitmap = Bitmap.createBitmap(rgbValues, width, height,
                Bitmap.Config.RGB_565);
        Bitmap mutableBitmap = staticBitmap.copy(Bitmap.Config.ARGB_8888, true);

        mImageView.setImageBitmap(mutableBitmap);
        mImageView.setOnTouchListener(this);
        canvas = new Canvas(mutableBitmap);
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }

    public void paintPicture() {
        // get our spacing values
        int xSpacing = (int) Math.floor(width/lineDensity);
        int ySpacing = (int) Math.floor(height/lineDensity);

        for(int i = 0; i< width; i++) {
            for (int j = 0; j < height; j++) {
                // test for x line
                if((i % xSpacing) == 0)
                {
                    staticBitmap.setPixel(i,j,currentColor);
                }
                // test for y line
                if((j % ySpacing) == 0)
                {
                    staticBitmap.setPixel(i,j,currentColor);
                }
            }
        }
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
                if(firstPoint == 0){
                    if((downx % 100) > 50){
                        downx = downx + (100 - (downx%100));
                    }else{
                        downx = downx - (downx%100);
                    }
                    if((downy % 100) > 50){
                        downy = downy + (100 - (downy%100));
                    }else{
                        downy = downy - (downy%100);
                    }
                    X = downx;
                    Y = downy;
                    X1 = upx;
                    firstPoint = 1;
                }
                float deltaX = upx - downx;
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    Log.i("Initial Coor pts x: ", X + "");
                    Log.i("Initial Coor pts y: ",Y+"");
                    canvas.drawLine(downx, downy, upx, downy, paint);
                }
                float distanceFromInitial = downx - X;
                if((distanceFromInitial > 0) && (downy >= (Y-50) && downy <=(Y+50)) && downx < X1){
                    // Toast.makeText(this, "Value of x is "+distanceFromInitial, Toast.LENGTH_SHORT).show();
                    Log.i("Touch pts: ", upx + "");
                    final int nearestWholeNo= Math.round((downx - X) / 100);
                    final float diff = (downx - X) / 100;
                    paint.setColor(Color.RED);

                    Log.i("Diff is : ", diff + "");
                    Log.i("Interpreted pts: ", String.valueOf(nearestWholeNo) + "");
                    final int newXPosition;cd 
                    if(diff > nearestWholeNo){
                        newXPosition = Math.round(downx - (downx % 100));
                        canvas.drawCircle(newXPosition, Y,4, paint);
                    }else{
                        newXPosition = Math.round(downx - (downx%100));
                        canvas.drawCircle(newXPosition, Y,4, paint);
                    }

                    //canvas.drawText(strNearWhlNo, 0, strNearWhlNo.length(), downx, Y + 30, paint);
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);

                    alert.setTitle("Enter Point");

                    // Set an EditText view to get user input
                    final EditText input = new EditText(this);
                    alert.setView(input);

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String inputVal = input.getText().toString();
                            canvas.drawText(inputVal, 0, inputVal.length(), newXPosition, Y + 30, paint);
                            //canvas.drawText(diff+"", 0, String.valueOf(diff).length(), newXPosition, Y + 40, paint);
                            canvas.drawText(nearestWholeNo+"", 0, String.valueOf(nearestWholeNo).length(), newXPosition, Y + 60, paint);
                            dialog.dismiss();
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }

                mImageView.invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }
}
