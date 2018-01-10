package com.example.willm.study;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by willm on 12/30/2017.
 */

public class TopicAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Topic> mDataSource;
    private TopicActivity topicActivity;

    public TopicAdapter(Context context, ArrayList<Topic> items, TopicActivity topic){
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        topicActivity = topic;
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
        View rowView = mInflater.inflate(R.layout.list_item_topic, parent, false);
        //Get the displays for the list
        TextView title = rowView.findViewById(R.id.topic_list_title);
        ImageButton removeButton = rowView.findViewById(R.id.topic_list_remove);
        TextView questionsCorrect = rowView.findViewById(R.id.topic_list_correct_questions);
        TextView questionsAttempted = rowView.findViewById(R.id.topic_list_attempted_questions);
        //Correctly fill the displays
        Topic topic = (Topic) getItem(position);
        title.setText(topic.toString());
        removeButton.setImageDrawable(mContext.getDrawable(R.drawable.remove_topics_50dp));
        removeButton.setTag(topic);
        questionsCorrect.setText("Questions Correct: 5");
        questionsAttempted.setText("Questions Attempted: 10");
        removeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.e("Topic Removal", "Removing");
                DBHandler db = new DBHandler(mContext);
                db.updateCurrent((Topic)v.getTag(), (byte)0);
                mDataSource.remove(v.getTag());
                if(db.getCurrentTopics().size() == 0){
                    topicActivity.showEmptyState();
                }
                else{
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return rowView;
    }
}
