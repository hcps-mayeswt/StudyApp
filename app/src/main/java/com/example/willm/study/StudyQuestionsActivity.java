package com.example.willm.study;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StudyQuestionsActivity extends AppCompatActivity {

    public static final int RESET_TIME = 10 * 60 * 1000;//10 minute

    String appPackage = "";//The app that was opened that triggered this to display

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_questions);
        //Record the app that the screen came from
        appPackage = getIntent().getStringExtra("App");
    }

    public void moveOn(View v){
        //Write the next time that this needs to be displayed on
        SharedPreferences prefs = getSharedPreferences(getString(R.string.pref), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(getString(R.string.display_time), System.currentTimeMillis() + RESET_TIME);
        editor.commit();
        //Return to the app that triggered the creation of this screen
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(appPackage);
        startActivity(launchIntent);
        //TODO: Remove this activity from the activity tray
    }
}
