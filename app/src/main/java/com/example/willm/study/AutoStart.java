package com.example.willm.study;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by willm on 4/3/2018.
 */

public class AutoStart extends BroadcastReceiver {
    public void onReceive(Context context, Intent arg1)
    {
        try {
            //Toast.makeText(context, "Auto Started", Toast.LENGTH_LONG).show();
            MonitorService.start(context);
            Log.i("Autostart", "started");
        }catch(Exception e){
            Log.e("Error", e.getMessage());
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
