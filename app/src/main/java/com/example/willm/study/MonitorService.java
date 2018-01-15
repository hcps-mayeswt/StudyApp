package com.example.willm.study;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;

import com.example.willm.study.Topics.TopicFactory;
import com.example.willm.study.UI.StudyQuestionsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by willm on 12/21/2017.
 */

public class MonitorService extends Service {

    private static final int REPEAT_INTERVAL = 1000; // 1 sec

    private Handler mHandler = new Handler();//Handler to repeat the app usage tracking every second

    private DBHandler db;

    private final Runnable appTracker = new Runnable(){
        @Override
        public void run(){
            ArrayList<String> currentTopics = db.getCurrentTopics();
            //Get the time when the lockout screen should next be displayed
            SharedPreferences prefs = getSharedPreferences(getString(R.string.pref), MODE_PRIVATE);
            long displayTime = prefs.getLong(getString(R.string.display_time), 0);
            //If we should display the lockout app, start looking for apps that are running
            if(System.currentTimeMillis() >= displayTime && currentTopics != null && currentTopics.size() > 0) {
                String currentApp = "NULL";//The app that is currently being used
                //The preferred method only works in Lollipop or later
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    //Get the apps usage stats tracker
                    UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
                    //Current time
                    long time = System.currentTimeMillis();

                    //Get the apps that have been used over the past 1000 seconds
                    //This list includes the apps homescreen
                    List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
                    //Make sure we have a list of apps
                    if (appList != null && appList.size() > 0) {
                        //Sort all of the apps based on when they were last used
                        SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
                        for (UsageStats usageStats : appList) {
                            mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                        }
                        //Check to make sure the map isn't empty
                        if (mySortedMap != null && !mySortedMap.isEmpty()) {
                            //The current app is the most recently used app
                            currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                        }
                    }
                } else {
                    //On pre-lollipop phones get a list of all currently running activities
                    ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
                    //The first app is the currently running app
                    currentApp = tasks.get(0).processName;
                }
                //Ensure that the current app isn't one of a list of excluded apps
                //TODO: Make this into an actual list, so it is extensible.
                if (!currentApp.equals("NULL") && !currentApp.equals("com.sec.android.app.launcher") && !currentApp.contains("com.example.willm.study") && !currentApp.equals("com.android.systemui")) {
                    //Present the lockout page to the user
                    Intent presentQuestions = new Intent(MonitorService.this, StudyQuestionsActivity.class);
                    //Record the current app so the page can go back to the correct place
                    presentQuestions.putExtra("App", currentApp);
                    startActivity(presentQuestions);
                }
                //Log.e("Other App Monitoring", "Current App in foreground is: " + currentApp);

            }
            //Repeat the check after a delay
            mHandler.postDelayed(appTracker, REPEAT_INTERVAL);
        }
    };

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    //Creates an intent to open this service from the created context
    private static Intent makeSelfIntent(Context context) {
        Intent intent = new Intent(context, MonitorService.class);
        return intent;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        db = new DBHandler(this);
        mHandler.post(appTracker);//Start tracking app usage
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;//Return START_STICKY to ensure that this service will continue running even if the app is removed from the app tray
    }

    // Call this method from onCreate of monitoring app
    public static void start(Context context) {
        //Start the service
        Intent intent = makeSelfIntent(context);
        context.startService(intent);
    }
}