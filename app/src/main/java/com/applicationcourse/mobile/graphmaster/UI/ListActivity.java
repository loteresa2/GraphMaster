package com.applicationcourse.mobile.graphmaster.UI;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.applicationcourse.mobile.graphmaster.R;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ListView listView;
    private ListAdapter listAdapter;
    private String[] videoTitle = {"Finding the Independent and Dependent Variable","Which Axis Takes Which Variable","Labelling Your Axes","Finding the Intervals for Your Axes","Creating a Title for Your Graph","Numbering Your Axes","Plotting Points On the Graph","Reading Your Graph for Information"};
    private String[] url = {"https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU",
            "https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU",
            "https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU",
            "https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU","https://youtu.be/liFLl3Eh_HU"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list2);

        listView = (ListView)findViewById(R.id.videolistView);
        listAdapter = new ListAdapter(this,R.layout.activity_list_row,videoTitle);
        try {
            listView.setAdapter(listAdapter);
        }catch (Exception e){
            Log.e("Adapter",e.getMessage());
        }

        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String urltext = url[position];

        if (urltext == null){
            Log.e("Get url","no url");
        }else{
            //open  youtube
            Uri uri = Uri.parse(urltext);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            Toast.makeText(getBaseContext(), "watch the video", Toast.LENGTH_SHORT).show();
        }
    }
}
