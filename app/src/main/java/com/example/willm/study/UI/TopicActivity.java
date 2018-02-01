package com.example.willm.study.UI;

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

import com.example.willm.study.DBHandler;
import com.example.willm.study.R;
import com.example.willm.study.Topics.TopicFactory;

import java.util.ArrayList;


public class TopicActivity extends AppCompatActivity {
    LinearLayout emptyState;
    FrameLayout listSpot;
    ListView currentTopicsDisplay;
    ArrayList<String> currentTopicsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        TextView currentPage = findViewById(R.id.topic_nav_bar);
        currentPage.setBackground(getDrawable(R.drawable.bg_highlight));

        //Empty State
        emptyState = findViewById(R.id.topics_empty_state);
        listSpot = findViewById(R.id.topic_list_spot);
        currentTopicsDisplay = findViewById(R.id.current_topics_list);
        Log.e("Created", "Created");
        updateList();
    }

    public void updateList(){
        listSpot.removeAllViews();
        listSpot.addView(currentTopicsDisplay);
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
        DBHandler db = new DBHandler(this);
        currentTopicsList = db.getCurrentTopics();
        Log.e("Reading from database", "Current " + currentTopicsList.toString());
        //Fill the topics list
        if(currentTopicsList != null && currentTopicsList.size() > 0) {
            currentTopicsDisplay.setAdapter(null);
            final TopicAdapter currentTopics = new TopicAdapter(this, currentTopicsList, this, false);
            currentTopicsDisplay.setAdapter(currentTopics);
            if(currentTopicsDisplay.getFooterViewsCount() == 0) {
                currentTopicsDisplay.addFooterView(addTopics);
            }
            listSpot.removeView(emptyState);
            currentTopicsDisplay.deferNotifyDataSetChanged();
        }
        else{
            showEmptyState();
        }
    }

    public void showEmptyState(){
        listSpot.removeAllViews();
        listSpot.addView(emptyState);
    }

    //Ensure that the topics page is always properly loaded
    @Override
    public void onRestart() {
        Log.e("Restarted", "Restarted");
        super.onRestart();
        updateList();
    }

    //NAVBAR
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
