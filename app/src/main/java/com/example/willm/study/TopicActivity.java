package com.example.willm.study;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TopicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        TextView currentPage = findViewById(R.id.topic_nav_bar);
        currentPage.setBackground(getDrawable(R.drawable.bg_highlight));
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
