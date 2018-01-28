package com.example.willm.study.UI;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.willm.study.QuestionSettingsHandler;
import com.example.willm.study.R;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class QuestionSettingsActivity extends AppCompatActivity {
    private final int HIDDEN_ID = 0;
    private final int SETTINGS_ID = 1;
    private final int IMAGE_ID = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_settings);
        setUpDropdowns();
        setUpSettingsDisplay();
        //getSupportFragmentManager().beginTransaction()
        //        .replace(android.R.id.content, new QuestionSettingsFragment())
        //        .commit();
    }

    public int findPos(String[] arr, String val){
        int i = 0;
        for (String str : arr){
            if(str.equals(val)){
                return i;
            }
            i++;
        }
        return -1;
    }

    public void setUpDropdowns(){
        //----------------------Set up primary req type-------------------------
        //Find the spinner
        Spinner primaryReqType = findViewById(R.id.primary_req_type_val);
        //Set the on item selected listener to update preferences
        primaryReqType.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = prefs.edit();
                        String[] preferences = getResources().getStringArray(R.array.pref_req_types);
                        String selected = preferences[i];
                        Log.e("Selecting", selected);
                        edit.putString(QuestionSettingsHandler.KEY_PRIMARY_REQ_TYPE, selected);
                        edit.commit();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );
        //Get the current value
        String primaryReqTypeVal = QuestionSettingsHandler.getPreference(this, QuestionSettingsHandler.KEY_PRIMARY_REQ_TYPE);
        //Set spinner value to position of current value
        primaryReqType.setSelection(findPos(getResources().getStringArray(R.array.pref_req_types), primaryReqTypeVal));
        //---------------------Set up primary req-------------------------------
        //Find the spinner
        Spinner primaryReq = findViewById(R.id.primary_req_val);
        //Set the on item selected listener to update preferences
        primaryReq.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = prefs.edit();
                        String[] reqs = getResources().getStringArray(R.array.pref_reqs);
                        String selected = reqs[i];
                        Log.e("Selected", selected);
                        edit.putString(QuestionSettingsHandler.KEY_PRIMARY_REQ, selected);
                        edit.commit();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );
        //Get the current value
        String primaryReqVal = QuestionSettingsHandler.getPreference(this, QuestionSettingsHandler.KEY_PRIMARY_REQ);
        //Set spinner value to position of current value
        primaryReq.setSelection(findPos(getResources().getStringArray(R.array.pref_reqs), primaryReqVal));
        //---------------------Set up secondary req type------------------------
        //Find the spinner
        Spinner secondaryReqType = findViewById(R.id.secondary_req_type_val);
        //Set the on item selected listener to update preferences
        secondaryReqType.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = prefs.edit();
                        String[] reqTypes = getResources().getStringArray(R.array.pref_req_types_none);
                        String selected = reqTypes[i];
                        edit.putString(QuestionSettingsHandler.KEY_SECONDARY_REQ_TYPE, selected);
                        edit.commit();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );
        //Get the current value
        String secondaryReqTypeVal = QuestionSettingsHandler.getPreference(this, QuestionSettingsHandler.KEY_SECONDARY_REQ_TYPE);
        //Set the spinner value to position of current value
        secondaryReqType.setSelection(findPos(getResources().getStringArray(R.array.pref_req_types_none), secondaryReqTypeVal));
        //---------------------Set up secondary req-----------------------------
        //Find the spinner
        Spinner secondaryReq = findViewById(R.id.secondary_req_val);
        //Set the on item selected listener to update preferences
        secondaryReq.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = prefs.edit();
                        String[] reqs = getResources().getStringArray(R.array.pref_reqs);
                        String selected = reqs[i];
                        edit.putString(QuestionSettingsHandler.KEY_SECONDARY_REQ, selected);
                        edit.commit();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );
        //Get the current value
        String secondaryReqVal = QuestionSettingsHandler.getPreference(this, QuestionSettingsHandler.KEY_SECONDARY_REQ);
        //Set the spinner value to position of current value
        secondaryReq.setSelection(findPos(getResources().getStringArray(R.array.pref_reqs), secondaryReqVal));
        //---------------------Set up unlock duration---------------------------
        //Find the spinner
        Spinner unlockDur = findViewById(R.id.unlock_dur_val);
        //Set the on item selected listener to update preferences
        unlockDur.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = prefs.edit();
                        String[] durs = getResources().getStringArray(R.array.pref_unlock_dur_values);
                        String selected = durs[i];
                        edit.putString(QuestionSettingsHandler.KEY_PRIMARY_REQ, selected);
                        edit.commit();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );
        //Get the current value
        String unlockDurVal = QuestionSettingsHandler.getPreference(this, QuestionSettingsHandler.KEY_UNLOCK_DURATION);
        //Set the spinner value to position of current value
        unlockDur.setSelection(findPos(getResources().getStringArray(R.array.pref_unlock_dur_values), unlockDurVal));
    }

    public void setUpSettingsDisplay(){
        //-----------------Set up question requirements------------------
        FrameLayout questionRequirementsHeader = findViewById(R.id.question_requirements_group_header);
        LinearLayout questionRequirements = findViewById(R.id.question_requirements_group);
        ImageView questionRequirementsArrow = findViewById(R.id.question_requirements_arrow);
        LinearLayout questionRequirementsFull = findViewById(R.id.question_requirements_group_and_header);
        questionRequirementsHeader.setTag(R.id.hidden_id, Boolean.TRUE);//The object is hidden
        questionRequirementsHeader.setTag(R.id.settings_id, questionRequirements);//Store the group
        questionRequirementsHeader.setTag(R.id.image_id, questionRequirementsArrow);//Store the arrow
        questionRequirementsHeader.setTag(R.id.parent_id, questionRequirementsFull);//Store the parent layout
        //Hide the group
        questionRequirementsFull.removeView(questionRequirements);
        //----------------Set up when to ask group---------------------
        FrameLayout whenToAskHeader = findViewById(R.id.when_to_ask_group_header);
        LinearLayout whenToAsk = findViewById(R.id.when_to_ask_group);
        ImageView whenToAskArrow = findViewById(R.id.when_to_ask_arrow);
        LinearLayout whenToAskFull = findViewById(R.id.when_to_ask_group_and_header);
        whenToAskHeader.setTag(R.id.hidden_id, Boolean.TRUE);//The object is hidden
        whenToAskHeader.setTag(R.id.settings_id, whenToAsk);//Store the group
        whenToAskHeader.setTag(R.id.image_id, whenToAskArrow);//Store the arrow
        whenToAskHeader.setTag(R.id.parent_id, whenToAskFull);//Store the parent layout
        //Hide the group
        whenToAskFull.removeView(whenToAsk);

    }

    public void expandAndHideGroup(View v){
        Boolean hidden = (Boolean)v.getTag(R.id.hidden_id);//Find out if the settings are currently hidden
        LinearLayout settings = (LinearLayout)v.getTag(R.id.settings_id);//Find the list of settings
        LinearLayout settingsParent = (LinearLayout)v.getTag(R.id.parent_id);//Find the parent
        ImageView arrow = (ImageView)v.getTag(R.id.image_id);//Find the arrow
        if(hidden.equals(Boolean.TRUE)){
            v.setTag(R.id.hidden_id, Boolean.FALSE);//The object is no longer hidden
            //Make the settings visible
            settingsParent.addView(settings);
            //Change the arrow direction
            arrow.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
        }
        else{
            v.setTag(R.id.hidden_id, Boolean.TRUE);//The object is hidden
            //Hide the settings
            settingsParent.removeView(settings);
            //Change the arrow direction
            arrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
        }
    }

    public void goToTopics(View view){
        startActivity(new Intent(QuestionSettingsActivity.this, TopicActivity.class));
        finish();
    }

    public void goToProgress(View view){
        startActivity(new Intent(QuestionSettingsActivity.this, ProgressActivity.class));
        finish();
    }

    public void goToAppSettings(View view){
        startActivity(new Intent(QuestionSettingsActivity.this, AppSettingsActivity.class));
        finish();
    }

    public void goToQuestionSettings(View view){}

}
