package com.example.willm.study;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        TextView current = (TextView)findViewById(R.id.progress_nav_bar);
        current.setBackground(getDrawable(R.drawable.bg_highlight));
    }

    public void goToTopics(View view){
        startActivity(new Intent(ProgressActivity.this, TopicActivity.class));
        finish();
    }

    public void goToProgress(View view){}

    public void goToAppSettings(View view){
        startActivity(new Intent(ProgressActivity.this, AppSettingsActivity.class));
        finish();
    }

    public void goToQuestionSettings(View view){
        startActivity(new Intent(ProgressActivity.this, QuestionSettingsActivity.class));
        finish();
    }

}
