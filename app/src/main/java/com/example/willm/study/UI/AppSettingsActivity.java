package com.example.willm.study.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.willm.study.AppSettingsHandler;
import com.example.willm.study.R;

public class AppSettingsActivity extends AppCompatActivity {
    private CheckBox usePin;
    private boolean usePinVal;
    private EditText pin;
    private String pinVal;
    private boolean editingPin = false;
    private LinearLayout pinLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        TextView currentPage = findViewById(R.id.app_settings_nav_bar);
        currentPage.setBackground(getDrawable(R.drawable.bg_highlight));
        setUpSettings();
        setUpSettingsDisplay();
        pin.setEnabled(false);
    }

    public void setUpSettings(){
        pin = findViewById(R.id.pin_val);
        pinVal = AppSettingsHandler.getPreference(this, AppSettingsHandler.KEY_PIN);
        pinLayout = findViewById(R.id.pin);
        usePin = findViewById(R.id.require_pin_val);
        usePinVal = AppSettingsHandler.getBoolPreference(this, AppSettingsHandler.KEY_REQUIRE_PIN);
        usePin.setChecked(usePinVal);
        usePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor edit = prefs.edit();
                boolean isChecked = ((CheckBox) view).isChecked();
                edit.putBoolean(AppSettingsHandler.KEY_REQUIRE_PIN, isChecked);
                if(!isChecked){
                    LinearLayout securityGroup = findViewById(R.id.security_group);
                    securityGroup.removeView(pinLayout);
                }
                else{
                    LinearLayout securityGroup = findViewById(R.id.security_group);
                    securityGroup.addView(pinLayout);
                }
                edit.apply();
            }
        });
        pin.setText(pinVal);
        Log.e("App Settings", usePinVal + "");
    }

    public void setUpSettingsDisplay(){
        //-----------------Set up question requirements------------------
        FrameLayout securityHeader = findViewById(R.id.security_group_header);
        LinearLayout security = findViewById(R.id.security_group);
        ImageView securityArrow = findViewById(R.id.security_arrow);
        LinearLayout securityFull = findViewById(R.id.security_group_and_header);
        securityHeader.setTag(R.id.hidden_id, Boolean.TRUE);//The object is hidden
        securityHeader.setTag(R.id.settings_id, security);//Store the group
        securityHeader.setTag(R.id.image_id, securityArrow);//Store the arrow
        securityHeader.setTag(R.id.parent_id, securityFull);//Store the parent layout
        //Hide the group
        securityFull.removeView(security);
    }

    public void passcodeHandling(final View v){
        if(!editingPin) {
            AppSettingsHandler.promptForPassword(this, new Runnable() {
                @Override
                public void run() {
                    editingPin = true;
                    ((Button) v).setText(R.string.save_passcode);
                    pin.setEnabled(true);
                    pin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }, new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AppSettingsActivity.this, "Password was incorrect", Toast.LENGTH_SHORT).show();
                    passcodeHandling(v);
                }
            });
        }
        else{
            ((Button)v).setText(R.string.edit_passcode);
            editingPin = false;
            pinVal = pin.getText().toString();
            //Store the pin
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(AppSettingsHandler.KEY_PIN, pinVal);
            edit.apply();
            pin.setTransformationMethod(PasswordTransformationMethod.getInstance());
            pin.setEnabled(false);
        }
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
        startActivity(new Intent(AppSettingsActivity.this, TopicActivity.class));
        finish();
    }

    public void goToProgress(View view){
        startActivity(new Intent(AppSettingsActivity.this, ProgressActivity.class));
        finish();
    }

    public void goToAppSettings(View view){}

    public void goToQuestionSettings(View view){
        startActivity(new Intent(AppSettingsActivity.this, QuestionSettingsActivity.class));
        finish();
    }
}
