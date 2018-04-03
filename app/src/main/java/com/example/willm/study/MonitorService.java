package com.example.willm.study;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.willm.study.UI.StudyQuestionsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MonitorService extends Service {

    private static final int REPEAT_INTERVAL = 1000; // 1 sec

    private Handler mHandler = new Handler();//Handler to repeat the app usage tracking every second

    private DBHandler db;

    private final Runnable appTracker = new Runnable(){
        @Override
        public void run(){
            ArrayList<String> currentTopics = db.getCurrentTopics(true);
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
                    if(usm != null) {
                        //Current time
                        long time = System.currentTimeMillis();

                        //Get the apps that have been used over the past 10 seconds
                        //This list includes the apps homescreen
                        List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 10 * 1000, time);
                        //Make sure we have a list of apps
                        if (appList != null && appList.size() > 0) {
                            //Sort all of the apps based on when they were last used
                            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
                            for (UsageStats usageStats : appList) {
                                if(usageStats.getLastTimeStamp() - usageStats.getFirstTimeStamp() > 5000) {
                                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                                }
                            }
                            //Check to make sure the map isn't empty
                            if (!mySortedMap.isEmpty()) {
                                //The current app is the most recently used app
                                currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                                Log.e("Other App Monitoring", "Foreground " + (mySortedMap.get(mySortedMap.lastKey()).getLastTimeStamp() - mySortedMap.get(mySortedMap.lastKey()).getFirstTimeStamp()));
                            }
                        }
                    }
                } else {
                    //On pre-lollipop phones get a list of all currently running activities
                    ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    if(am != null) {
                        List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
                        //The first app is the currently running app
                        currentApp = tasks.get(0).processName;
                    }
                }
                //Ensure that the current app isn't one of a list of excluded apps
                //TODO: Make this into an actual list, so it is extensible.
                if (!currentApp.equals("NULL") && !currentApp.contains("com.android") && !currentApp.contains("com.sec.android") && !currentApp.contains("willm.study") && !currentApp.equals("com.android.systemui")) {
                    //Present the lockout page to the user
                    Intent presentQuestions = new Intent(MonitorService.this, StudyQuestionsActivity.class);
                    //Record the current app so the page can go back to the correct place
                    presentQuestions.putExtra("App", currentApp);
                    startActivity(presentQuestions);
                }
                Log.e("Other App Monitoring", "Current App in foreground is: " + currentApp);
                mHandler.postDelayed(appTracker, REPEAT_INTERVAL);
            }
            else {
                //Repeat the check after a delay
                mHandler.postDelayed(appTracker, REPEAT_INTERVAL);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    //Creates an intent to open this service from the created context
    private static Intent makeSelfIntent(Context context) {
        return new Intent(context, MonitorService.class);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        startForeground(1,new Notification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        db = new DBHandler(this);
        //Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        mHandler.post(appTracker);//Start tracking app usage
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;//Return START_STICKY to ensure that this service will continue running even if the app is removed from the app tray
    }

    // Call this method from onCreate of monitoring app
    public static void start(Context context) {
        //Start the service
        Intent intent = makeSelfIntent(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }
}