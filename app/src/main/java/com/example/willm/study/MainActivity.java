package com.example.willm.study;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextSwitcher;
import android.widget.TextView;

import java.util.List;
import java.util.TimerTask;

import static android.content.Context.ACTIVITY_SERVICE;

public class MainActivity extends AppCompatActivity {

    //Monitoring App Usage
    //final UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MonitorService ms = new MonitorService();
        //ms.start();
        PackageManager pm = getApplicationContext().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        //for(ApplicationInfo app : packages){
        //    if()
        //}
        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
        String current = taskInfo.get(0).topActivity.getPackageName();
        Log.d("Other App Monitoring",current);
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
        //startActivity(new Intent(MainActivity.this, StudyQuestionsActivity.class));
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
