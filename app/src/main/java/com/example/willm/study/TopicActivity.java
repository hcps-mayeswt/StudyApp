package com.example.willm.study;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class TopicActivity extends AppCompatActivity {
    LinearLayout emptyState;
    FrameLayout listSpot;
    ListView currentTopicsDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        TextView currentPage = findViewById(R.id.topic_nav_bar);
        currentPage.setBackground(getDrawable(R.drawable.bg_highlight));

        //Add TopicsHandler Button
        final ImageButton addTopics = new ImageButton(this);
        addTopics.setImageDrawable(getDrawable(R.drawable.add_topics_small));
        addTopics.setBackground(null);
        addTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddTopics(view);
            }
        });

        //Empty State
        emptyState = findViewById(R.id.topics_empty_state);
        listSpot = findViewById(R.id.topic_list_spot);
        currentTopicsDisplay = findViewById(R.id.current_topics_list);
        DBHandler db = new DBHandler(this);
        ArrayList<Topic> currentTopicsList = db.getCurrentTopics();
        Log.e("Reading from database", "Current " + currentTopicsList.toString());
        //Fill the topics list
        if(currentTopicsList != null && currentTopicsList.size() > 0) {
            final TopicAdapter currentTopics = new TopicAdapter(this, currentTopicsList, this);
            currentTopicsDisplay.setAdapter(currentTopics);
            currentTopicsDisplay.addFooterView(addTopics);
            listSpot.removeView(emptyState);
        }
        else{
            showEmptyState();
        }
    }

    public void showEmptyState(){
        listSpot.removeAllViews();
        listSpot.addView(emptyState);
    }

    public void goToAddTopics(View view){
        startActivity(new Intent(TopicActivity.this, AddTopicsActivity.class));
    }

    public void goToTopics(View view){}

    public void goToProgress(View view){
        startActivity(new Intent(TopicActivity.this, ProgressActivity.class));
        finish();
    }

    public void goToAppSettings(View view){
        startActivity(new Intent(TopicActivity.this, AppSettingsActivity.class));
        finish();
    }

    public void goToQuestionSettings(View view){
        startActivity(new Intent(TopicActivity.this, QuestionSettingsActivity.class));
        finish();
    }
}
