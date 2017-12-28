package com.example.willm.study;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //Apps to exclude from being blocked

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Start the tracking service
        MonitorService.start(this);

        //Ensure that all needed variables for the app blocking functionality are present
        SharedPreferences prefs = getSharedPreferences(getString(R.string.pref), MODE_PRIVATE);//Get preferences
        long displayTime = prefs.getLong(getString(R.string.display_time), Long.MAX_VALUE);//Check if the display time is set
        //Check to make sure the display time isn't the default value, ie the d
        if(displayTime == Long.MAX_VALUE) {
            //Create an editor to set the next display time to 0.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(getString(R.string.display_time), 0);
            editor.commit();
        }
    }

    //Function to display hint text when a user clicks on the center
    public void onCenterClick(View r){
        final TextView center = (TextView)findViewById(R.id.center);
        //Define animations
        final AlphaAnimation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(200);
        final AlphaAnimation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(200);
        //Display help text
        center.startAnimation(out);
        out.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation animation){
                if(center.getText().equals(getString(R.string.app_name))) {
                    center.setTextSize(14);
                    center.setText(getString(R.string.center_click));
                }
                else{
                    center.setTextSize(36);
                    center.setText(getString(R.string.app_name));
                }
                center.startAnimation(in);
            }
            @Override
            public void onAnimationRepeat(Animation animation){ }
            @Override
            public void onAnimationStart(Animation animation){  }
        });
        //Revert text after delay
        center.postDelayed(new Runnable(){
            public void run() {
                out.setDuration(100);
                in.setDuration(100);
                center.startAnimation(out);
            }
        }, 2000);
    }

    //Function to transition the user to the topic page
    public void onTopicClick(View r){
        startActivity(new Intent(MainActivity.this, TopicActivity.class));
    }

    //Function to transition the user to the progress page
    public void onProgressClick(View r){
        startActivity(new Intent(MainActivity.this, ProgressActivity.class));
    }

    //Function to transition the user to the topic page
    public void onAppSettingsClick(View r){
        startActivity(new Intent(MainActivity.this, AppSettingsActivity.class));
    }

    //Function to transition the user to the topic page
    public void onQuestionSettingsClick(View r){

        startActivity(new Intent(MainActivity.this, QuestionSettingsActivity.class));
    }
}
