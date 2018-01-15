package com.example.willm.study.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.willm.study.R;

public class AppSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        TextView currentPage = findViewById(R.id.app_settings_nav_bar);
        currentPage.setBackground(getDrawable(R.drawable.bg_highlight));
    }

    public void goToTopics(View view){
        startActivity(new Intent(AppSettingsActivity.this, TopicActivity.class));
        finish();
    }

    public void goToProgress(View view){
        startActivity(new Intent(AppSettingsActivity.this, ProgressActivity.class));
        finish();
    }

    public void goToAppSettings(View view){}

    public void goToQuestionSettings(View view){
        startActivity(new Intent(AppSettingsActivity.this, QuestionSettingsActivity.class));
        finish();
    }
}
