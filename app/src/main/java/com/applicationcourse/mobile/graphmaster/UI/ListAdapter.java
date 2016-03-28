package com.applicationcourse.mobile.graphmaster.UI;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.applicationcourse.mobile.graphmaster.R;

/**
 * Created by Aiping Xiao on 2016-03-27.
 */
public class ListAdapter extends ArrayAdapter<String> {
    Context context;
    String[] videoTitle = null;
    int layoutResourceId;

    public ListAdapter(Context context,int layoutResourceId,String[] videoTitle){
        super(context,layoutResourceId, videoTitle);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.videoTitle = videoTitle;

    }
    static class TextHolder{
        TextView videotxt;
        ImageView videoicon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        String videotitle = videoTitle[position];
        TextHolder textHolder = null;


        if (view == null){
            LayoutInflater inflater = ((AppCompatActivity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.activity_list_row,parent,false);

            textHolder = new TextHolder();
            textHolder.videotxt = (TextView)view.findViewById(R.id.videoTitle);
            textHolder.videoicon = (ImageView)view.findViewById(R.id.videolist);

            view.setTag(textHolder);

        }else{
            textHolder = (TextHolder)view.getTag();
        }

        textHolder.videotxt.setText(videotitle);
        return view;

    }
}

