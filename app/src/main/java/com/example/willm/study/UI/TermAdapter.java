package com.example.willm.study.UI;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.willm.study.DBHandler;
import com.example.willm.study.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by willm on 2/26/2018.
 */

public class TermAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<HashMap<String, String>> mDataSource;
    private QuestionSetCreation qsActivity;
    private View.OnTouchListener mTouchListener;

    public TermAdapter(Context context, ArrayList<HashMap<String, String>> items, QuestionSetCreation activity, View.OnTouchListener tl){
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        qsActivity = activity;
        mTouchListener = tl;
    }


    public void notifyNewData(){
        final TermAdapter adapter = this;
        adapter.notifyDataSetChanged();
    }
    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TermAdapter adapter = this;
        // Get view for row item
        View rowView;
        rowView = mInflater.inflate(R.layout.list_item_vocab, parent, false);
        //Get the displays for the list
        TextView termView = rowView.findViewById(R.id.term);
        TextView definitionView = rowView.findViewById(R.id.definition);
        //Correctly fill the displays
        HashMap<String, String> term = (HashMap<String, String>) getItem(position);
        for (String definition : term.keySet()){
            definitionView.setText(definition);
            termView.setText(term.get(definition));
        }
        /*ImageButton removeButton = rowView.findViewById(R.id.topic_list_remove);
        removeButton.setTag(position);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qsActivity.remove((int)v.getTag());
            }
        });*/

        LinearLayout wrapper = rowView.findViewById(R.id.term_wrapper);
        rowView.setTag(position);
        rowView.setOnTouchListener(mTouchListener);

        return rowView;
    }
}
