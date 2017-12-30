package com.example.willm.study;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class QuestionSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_settings);
        TextView currentPage = findViewById(R.id.question_settings_nav_bar);
        currentPage.setBackground(getDrawable(R.drawable.bg_highlight));
    }

    public void goToTopics(View view){
        startActivity(new Intent(QuestionSettingsActivity.this, TopicActivity.class));
        finish();
    }

    public void goToProgress(View view){
        startActivity(new Intent(QuestionSettingsActivity.this, ProgressActivity.class));
        finish();
    }

    public void goToAppSettings(View view){
        startActivity(new Intent(QuestionSettingsActivity.this, AppSettingsActivity.class));
        finish();
    }

    public void goToQuestionSettings(View view){}
}
