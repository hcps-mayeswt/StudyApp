package com.example.willm.study;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

public class QuestionSettingsHandler {
    //Keys for question settings preferences
    public static final String KEY_PRIMARY_REQ_TYPE = "primary_req_type";
    public static final String KEY_PRIMARY_REQ = "primary_req";
    public static final String KEY_SECONDARY_REQ_TYPE = "sec_req_type";
    public static final String KEY_SECONDARY_REQ = "sec_req";
    //public static final String KEY_ALWAYS_LOCK = "always_lockout";
    public static final String KEY_UNLOCK_DURATION = "unlock_duration";

    public static long getUnlockDuration(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("Preferences", prefs.getString(KEY_UNLOCK_DURATION, "0"));
        return Long.parseLong(prefs.getString(KEY_UNLOCK_DURATION, "0"));
    }

    public static String getPreference (Context context,String prefKey){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(prefKey, "");
    }

    public static boolean passedReq(Context context, int attempts, int correct, int inARow){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String primaryReqType = prefs.getString(KEY_PRIMARY_REQ_TYPE, "");
        int primaryReq = Integer.parseInt(prefs.getString(KEY_PRIMARY_REQ, "0"));
        Log.e("Attempted", primaryReq + "");
        Log.e("Attempted", primaryReqType);
        Log.e("Attempted", attempts + "");
        Log.e("Attempted", correct + "");
        switch(primaryReqType){
            case "# Attempted":
                if(attempts >= primaryReq) return true;
                break;
            case "# Correct":
                if(correct >= primaryReq) return true;
                break;
            case "# Correct In-A-Row":
                if(inARow >= primaryReq) return true;
                break;
            default:
                break;
        }
        String secondaryReqType = prefs.getString(KEY_SECONDARY_REQ_TYPE, "");
        int secondaryReq = Integer.parseInt(prefs.getString(KEY_SECONDARY_REQ, "0"));
        switch(secondaryReqType){
            case "Number Attempted":
                if(attempts >= secondaryReq) return true;
                break;
            case "Number Correct":
                if(correct >= secondaryReq) return true;
                break;
            case "Number Correct In-A-Row":
                if(inARow >= secondaryReq) return true;
                break;
            default:
                break;
        }
        return false;
    }
}