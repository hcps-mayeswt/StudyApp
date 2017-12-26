package com.example.willm.study;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by willm on 12/21/2017.
 */

public class MonitorService extends Service
{
    private boolean initialized = false;
    private final IBinder mBinder = new LocalBinder();
    private ServiceCallback callback = null;
    private Timer timer = null;
    //private final Handler mHandler = new Handler();
    private String foreground = null;
    //private ArrayList<HashMap<String,Object>> processList;
    //private ArrayList<String> packages;
    //private Date split = null;

    public static int SERVICE_PERIOD = 1000; // TODO: customize (this is for scan every 5 seconds)


    @Override
    public IBinder onBind(Intent i){
        return mBinder;
    }

    public interface ServiceCallback
    {
        void sendResults(int resultCode, Bundle b);
    }

    public class LocalBinder extends Binder
    {
        MonitorService getService()
        {
            // Return this instance of the service so clients can call public methods
            return MonitorService.this;
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        initialized = true;
        //processList = ((MonitorApp)getApplication()).getProcessList();
        //packages = ((MonitorApp)getApplication()).getPackages();
    }

    /*@Override
    public IBinder onBind(Intent intent)
    {
        if(initialized)
        {
            return mBinder;
        }
        return null;
    }

    public void setCallback(ServiceCallback callback)
    {
        this.callback = callback;
    }

    private boolean addToStatistics(String target)
    {
        boolean changed = false;
        Date now = new Date();
        if(!TextUtils.isEmpty(target))
        {
            if(!target.equals(foreground))
            {
                int i;
                if(foreground != null && split != null)
                {
                    // TODO: calculate time difference from current moment
                    // to the moment when previous foreground process was activated
                    i = packages.indexOf(foreground);
                    long delta = (now.getTime() - split.getTime()) / 1000;
                    Long time = (Long)processList.get(i).get(COLUMN_PROCESS_TIME);
                    if(time != null)
                    {
                        // TODO: add the delta to statistics of 'foreground'
                        time += delta;
                    }
                    else
                    {
                        time = new Long(delta);
                    }
                    processList.get(i).put(COLUMN_PROCESS_TIME, time);
                }

                // update count of process activation for new 'target'
                i = packages.indexOf(target);
                Integer count = (Integer)processList.get(i).get(COLUMN_PROCESS_COUNT);
                if(count != null) count++;
                else
                {
                    count = new Integer(1);
                }
                processList.get(i).put(COLUMN_PROCESS_COUNT, count);

                foreground = target;
                split = now;
                changed = true;
            }
        }
        return changed;
    }*/


    public void start()
    {
        if(timer == null)
        {
            timer = new Timer();
            timer.schedule(new MonitoringTimerTask(), 500, SERVICE_PERIOD);
        }

        // TODO: startForeground(srvcid, createNotification(null));
    }

    public void stop()
    {
        timer.cancel();
        timer.purge();
        timer = null;
    }

    private class MonitoringTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            //ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
            //List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
            //String current = taskInfo.get(0).topActivity.getPackageName();
            //Log.d("Other App Monitoiring",current);
            Log.d("Other App Monitoring", "Monitoring");
        }
    }
}
