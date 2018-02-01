package com.example.willm.study.UI;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.willm.study.DBHandler;
import com.example.willm.study.R;

import java.util.ArrayList;


public class TopicAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mDataSource;
    private TopicActivity topicActivity;
    private AddTopicsActivity addTopicsActivity;
    private boolean addTopics;

    public TopicAdapter(Context context, ArrayList<String> items, TopicActivity topic, boolean adding){
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        topicActivity = topic;
        addTopics = adding;
    }
    public TopicAdapter(Context context, ArrayList<String> items, AddTopicsActivity topic, boolean adding){
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addTopicsActivity = topic;
        addTopics = adding;
    }


    public void notifyNewData(){
        final TopicAdapter adapter = this;
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
        final TopicAdapter adapter = this;
        // Get view for row item
        View rowView;
        if(addTopics){
            rowView = mInflater.inflate(R.layout.add_list_item_topic, parent, false);
        }else{
            rowView = mInflater.inflate(R.layout.list_item_topic, parent, false);
        }
        //Get the displays for the list
        TextView title = rowView.findViewById(R.id.topic_list_title);
        //Correctly fill the displays
        String topic = (String) getItem(position);
        title.setText(topic);
        rowView.setTag(topic);
        if(addTopics){
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("ADDING TOPICS", "VIEW ITEM CLICKED");
                    mDataSource = addTopicsActivity.onListClicked((String)view.getTag());
                    adapter.notifyDataSetChanged();
                }
            });
        }else {
            ImageButton removeButton = rowView.findViewById(R.id.topic_list_remove);
            TextView questionsCorrect = rowView.findViewById(R.id.topic_list_correct_questions);
            TextView questionsAttempted = rowView.findViewById(R.id.topic_list_attempted_questions);

            removeButton.setImageDrawable(mContext.getDrawable(R.drawable.remove_topics_50dp));
            removeButton.setTag(topic);
            questionsCorrect.setText("");
            questionsAttempted.setText("");
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("TopicFactory Removal", "Removing");
                    DBHandler db = new DBHandler(mContext);
                    db.updateCurrent((String) v.getTag(), (byte) 0);
                    mDataSource.remove(v.getTag());
                    if (db.getCurrentTopics().size() == 0) {
                        topicActivity.showEmptyState();
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
        return rowView;
    }
}
