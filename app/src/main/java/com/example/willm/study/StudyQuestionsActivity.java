package com.example.willm.study;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StudyQuestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_questions);
    }

    public void moveOn(View v){
        this.finish();
    }
}
