package com.applicationcourse.mobile.graphmaster.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.applicationcourse.mobile.graphmaster.Database.DatabaseHandler;
import com.applicationcourse.mobile.graphmaster.Database.MainQues;
import com.applicationcourse.mobile.graphmaster.Database.MainQuesHData;
import com.applicationcourse.mobile.graphmaster.Database.MainQuesHeading;
import com.applicationcourse.mobile.graphmaster.Database.Options;
import com.applicationcourse.mobile.graphmaster.Database.SubQuestion;
import com.applicationcourse.mobile.graphmaster.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.*;

import java.util.List;

public class LevelActivity extends Activity {

    private DatabaseHandler databaseHandler;
    private static Context mContext = null;
    private com.daimajia.androidviewhover.BlurLayout mSampleLayout, mSampleLayout2, mSampleLayout3, mSampleLayout4, mSampleLayout5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        mContext = this;
        //initialization
        databaseHandler = DatabaseHandler.getHandler(mContext);
        com.daimajia.androidviewhover.BlurLayout.setGlobalDefaultDuration(450);

        mSampleLayout = (com.daimajia.androidviewhover.BlurLayout) findViewById(R.id.level1);
        View hover = LayoutInflater.from(mContext).inflate(R.layout.hover_sample, null);
        hover.findViewById(R.id.avatar1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int grade=1;
                Toast.makeText(mContext, "Starting Level 1 questions", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LevelActivity.this, DrawGraphActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("grade", grade);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mSampleLayout.setHoverView(hover);
        mSampleLayout.setBlurDuration(550);

        mSampleLayout.addChildAppearAnimator(hover, R.id.description1, Techniques.FadeInUp);
        mSampleLayout.addChildDisappearAnimator(hover, R.id.description1, Techniques.FadeOutDown);
        mSampleLayout.addChildAppearAnimator(hover, R.id.avatar1, Techniques.DropOut, 1200);
        mSampleLayout.addChildDisappearAnimator(hover, R.id.avatar1, Techniques.FadeOutUp);
        mSampleLayout.enableZoomBackground(true);
        mSampleLayout.setBlurDuration(1000);

        //sample2
        mSampleLayout2 = (com.daimajia.androidviewhover.BlurLayout) findViewById(R.id.level2);
        View hover2 = LayoutInflater.from(mContext).inflate(R.layout.hover_sample2, null);
        hover2.findViewById(R.id.avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int grade=2;
                Toast.makeText(mContext, "Starting Level 2 questions", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LevelActivity.this, DrawGraphActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("grade", grade);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mSampleLayout2.setHoverView(hover2);

        mSampleLayout2.addChildAppearAnimator(hover2, R.id.description, Techniques.FadeInUp);
        mSampleLayout2.addChildDisappearAnimator(hover2, R.id.description, Techniques.FadeOutDown);
        mSampleLayout2.addChildAppearAnimator(hover2, R.id.avatar, Techniques.DropOut, 1200);
        mSampleLayout2.addChildDisappearAnimator(hover2, R.id.avatar, Techniques.FadeOutUp);
        mSampleLayout2.enableZoomBackground(true);
        mSampleLayout2.setBlurDuration(1000);

        //sample3
        mSampleLayout3 = (com.daimajia.androidviewhover.BlurLayout) findViewById(R.id.level3);
        View hover3 = LayoutInflater.from(mContext).inflate(R.layout.hover_sample3, null);
        hover3.findViewById(R.id.avatar3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int grade=3;
                Toast.makeText(mContext, "Starting Level 3 questions", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LevelActivity.this, DrawGraphActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("grade", grade);
                intent.putExtras(bundle);
                startActivity(intent);            }
        });
        mSampleLayout3.setHoverView(hover3);

        mSampleLayout3.addChildAppearAnimator(hover3, R.id.description3, Techniques.FadeInUp);
        mSampleLayout3.addChildDisappearAnimator(hover3, R.id.description3, Techniques.FadeOutDown);
        mSampleLayout3.addChildAppearAnimator(hover3, R.id.avatar3, Techniques.DropOut, 1200);
        mSampleLayout3.addChildDisappearAnimator(hover3, R.id.avatar3, Techniques.FadeOutUp);
        mSampleLayout3.enableZoomBackground(true);
        mSampleLayout3.setBlurDuration(1200);

        //sample 4

        mSampleLayout4 = (com.daimajia.androidviewhover.BlurLayout) findViewById(R.id.level4);
        View hover4 = LayoutInflater.from(mContext).inflate(R.layout.hover_sample4, null);
        hover4.findViewById(R.id.avatar4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int grade=4;
                Toast.makeText(mContext, "Starting Level 4 questions", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LevelActivity.this, DrawGraphActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("grade", grade);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mSampleLayout4.setHoverView(hover4);

        mSampleLayout4.addChildAppearAnimator(hover4, R.id.description4, Techniques.FadeInUp);
        mSampleLayout4.addChildDisappearAnimator(hover4, R.id.description4, Techniques.FadeOutDown);
        mSampleLayout4.addChildAppearAnimator(hover4, R.id.avatar4, Techniques.DropOut, 1200);
        mSampleLayout4.addChildDisappearAnimator(hover4, R.id.avatar4, Techniques.FadeOutUp);
        mSampleLayout4.enableZoomBackground(true);
        mSampleLayout4.setBlurDuration(1200);

        //Yolanda
        mSampleLayout5 = (com.daimajia.androidviewhover.BlurLayout) findViewById(R.id.level5);
        View hover5 = LayoutInflater.from(mContext).inflate(R.layout.hover_sample5, null);
        hover5.findViewById(R.id.avatar5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Watch some graph videos", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LevelActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
        mSampleLayout5.setHoverView(hover5);
        mSampleLayout5.setBlurDuration(550);
        mSampleLayout5.addChildAppearAnimator(hover5, R.id.description5, Techniques.FadeInUp);
        mSampleLayout5.addChildDisappearAnimator(hover5, R.id.description5, Techniques.FadeOutDown);
        mSampleLayout5.addChildAppearAnimator(hover5, R.id.avatar5, Techniques.DropOut, 1200);
        mSampleLayout5.addChildDisappearAnimator(hover5, R.id.avatar5, Techniques.FadeOutUp);
        mSampleLayout5.enableZoomBackground(true);
        mSampleLayout5.setBlurDuration(1000);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
