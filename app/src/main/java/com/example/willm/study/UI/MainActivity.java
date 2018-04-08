package com.example.willm.study.UI;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.willm.study.AppSettingsHandler;
import com.example.willm.study.AutoStart;
import com.example.willm.study.MonitorService;
import com.example.willm.study.R;

public class MainActivity extends Activity {
    private FrameLayout mainContentView;
    private FrameLayout tutorialView;
    private FrameLayout wrapper;
    private int tutorialWindow;
    final private int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Check for permission to track apps
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager) this
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), this.getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (this.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }
        //Request permission if not granted
        if (!granted) {
            Log.e("No Permission Granted", "No Permission");
            TextView text = new TextView(this);
            String message = "This app requires permission to track app usage. On the following page please scroll down to Study!" +
                    " and granted usage access.";
            text.setText(message);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Request Permission");
            builder.setView(text);
            builder.setPositiveButton("Grant Permission", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                }
            });
            builder.show();
        }
        //Record boot receiver
        final ComponentName onBootReceiver = new ComponentName(getApplication().getPackageName(), AutoStart.class.getName());
        if (getPackageManager().getComponentEnabledSetting(onBootReceiver) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
            getPackageManager().setComponentEnabledSetting(onBootReceiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        //Start the tracking service
        MonitorService.start(this);
        //Set default values for settings
        PreferenceManager.setDefaultValues(this, R.xml.questions_pref_general, false);
        //Ensure that all needed variables for the app blocking functionality are present
        SharedPreferences prefs = getSharedPreferences(getString(R.string.pref), MODE_PRIVATE);//Get preferences
        boolean tutorialShown = prefs.getBoolean(getString(R.string.tutorial_shown), false);
        long displayTime = prefs.getLong(getString(R.string.display_time), Long.MAX_VALUE);//Check if the display time is set
        //Check to make sure the display time isn't the default value, ie the d
        if (displayTime == Long.MAX_VALUE) {
            //Create an editor to set the next display time to 0.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(getString(R.string.display_time), 0);
            editor.apply();
        }
        mainContentView = findViewById(R.id.contentView);
        wrapper = findViewById(R.id.wrapperView);
        if (!tutorialShown){
            showTutorial();
        }
    }


    public void showTutorial(){
        //final Dialog dialog = new Dialog(this);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setContentView(tutorialView);
        //dialog.show();
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tutorialView = getLayoutInflater().inflate(R.layout.tutorial_full, wrapper).findViewById(R.id.tutorial);
        Button exitTutorial = tutorialView.findViewById(R.id.close_tutorial_button);
        exitTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTutorial();
                /*AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("Are you sure you want to exit the tutorial? It cannot be reopened.");
                builder.setPositiveButton("Exit Tutorial", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        endTutorial();
                            }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();*/
            }
        });
        tutorialView.setVisibility(View.VISIBLE);
        mainContentView.setClickable(false);
        tutorialWindow = 0;
        findViewById(R.id.topic_nav).setClickable(false);
        findViewById(R.id.progress_nav).setClickable(false);
        findViewById(R.id.app_settings_nav).setClickable(false);
        findViewById(R.id.question_settings_nav).setClickable(false);
        tutorialView.setOnTouchListener(new View.OnTouchListener() {
            private float startX = 0;
            private float difference = 0;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.e("Touched",
                        motionEvent.getAction() + " HERE");
                int event = motionEvent.getAction();
                view.performClick();
                switch(event){
                    case MotionEvent.ACTION_DOWN:
                        startX = motionEvent.getX();
                        Log.e("Touched", startX + "");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        difference = motionEvent.getX() - startX;
                        Log.e("Moving", difference +"");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("Moved", difference + "");
                        view.performClick();
                        animateSweep(view);
                        break;
                    default:
                        Log.e("Action", motionEvent.toString() + " " + motionEvent.getAction());
                }
                return true;
            }
            private void animateSweep(View v){
                final LinearLayout homeLayout = v.findViewById(R.id.home_tutorial);
                final LinearLayout topicLayout = v.findViewById(R.id.topics_tutorial);
                final LinearLayout appSettingsLayout = v.findViewById(R.id.app_settings_tutorial);
                final LinearLayout questionSettingsLayout = v.findViewById(R.id.question_settings_tutorial);
                float pixelLimit = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        25,
                        getResources().getDisplayMetrics()
                );
                final float animateAmount = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        300,
                        getResources().getDisplayMetrics()
                );
                if(Math.abs(difference) > pixelLimit){
                    if(difference < 0){
                        Log.e("Difference Less Than 0", "Difference < 0");
                        tutorialWindow++;
                    }
                    else{
                        tutorialWindow--;
                        tutorialWindow = Math.max(tutorialWindow, 0);
                    }
                    if(tutorialWindow == 0){
                        FrameLayout.LayoutParams currParams = (FrameLayout.LayoutParams)(tutorialView.findViewById(R.id.home_tutorial).getLayoutParams());
                        if(currParams.leftMargin == 0){
                            Log.e("No Animation", "Don't animate");
                        }
                        else {
                            Animation home = new Animation() {
                                @Override
                                public void applyTransformation(float v, Transformation t) {
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)homeLayout.getLayoutParams();
                                    params.leftMargin = (int) ((1 - v) * (-animateAmount));
                                    homeLayout.setLayoutParams(params);
                                }
                            };
                            home.setDuration(300);
                            homeLayout.startAnimation(home);
                            Animation topics = new Animation() {
                                @Override
                                public void applyTransformation(float v, Transformation t) {
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) topicLayout.getLayoutParams();
                                    params.leftMargin = (int) ((v) * (animateAmount));
                                    topicLayout.setLayoutParams(params);
                                }
                            };
                            topics.setDuration(300);
                            topicLayout.startAnimation(topics);
                            Animation appSettings = new Animation(){
                                @Override
                                public void applyTransformation(float v, Transformation t){
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) appSettingsLayout.getLayoutParams();
                                    params.leftMargin = (int)(animateAmount + v * animateAmount);
                                    appSettingsLayout.setLayoutParams(params);
                                }
                            };
                            appSettings.setDuration(300);
                            appSettingsLayout.startAnimation(appSettings);
                            Animation questionSettings = new Animation(){
                                @Override
                                public void applyTransformation(float v, Transformation t){
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) questionSettingsLayout.getLayoutParams();
                                    params.leftMargin = (int)(2 * animateAmount + v * animateAmount);
                                    questionSettingsLayout.setLayoutParams(params);
                                }
                            };
                            questionSettings.setDuration(300);
                            questionSettingsLayout.startAnimation(questionSettings);
                        }
                    }
                    else if(tutorialWindow == 1) {
                        if(difference < 0) {
                            Animation home = new Animation() {
                                @Override
                                public void applyTransformation(float v, Transformation t) {
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) homeLayout.getLayoutParams();
                                    params.leftMargin = (int) (v * (-animateAmount));
                                    homeLayout.setLayoutParams(params);
                                }
                            };
                            home.setDuration(300);
                            homeLayout.startAnimation(home);
                            Animation topics = new Animation() {
                                @Override
                                public void applyTransformation(float v, Transformation t) {
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) topicLayout.getLayoutParams();
                                    params.leftMargin = (int) ((1 - v) * (animateAmount));
                                    topicLayout.setLayoutParams(params);
                                }
                            };
                            topics.setDuration(300);
                            topicLayout.startAnimation(topics);
                            Animation appSettings = new Animation(){
                                @Override
                                public void applyTransformation(float v, Transformation t){
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) appSettingsLayout.getLayoutParams();
                                    params.leftMargin = (int)(animateAmount + (1 - v) * animateAmount);
                                    appSettingsLayout.setLayoutParams(params);
                                }
                            };
                            appSettings.setDuration(300);
                            appSettingsLayout.startAnimation(appSettings);
                            Animation questionSettings = new Animation(){
                                @Override
                                public void applyTransformation(float v, Transformation t){
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) questionSettingsLayout.getLayoutParams();
                                    params.leftMargin = (int)(2 * animateAmount + (1 - v) * animateAmount);
                                    questionSettingsLayout.setLayoutParams(params);
                                }
                            };
                            questionSettings.setDuration(300);
                            questionSettingsLayout.startAnimation(questionSettings);
                        }
                        else{
                            Animation home = new Animation() {
                                @Override
                                public void applyTransformation(float v, Transformation t){
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) homeLayout.getLayoutParams();
                                    params.leftMargin = (int)(-animateAmount + (1 - v) * -animateAmount);
                                    homeLayout.setLayoutParams(params);
                                }
                            };
                            home.setDuration(300);
                            homeLayout.startAnimation(home);
                            Animation topics = new Animation(){
                                @Override
                                public void applyTransformation(float v, Transformation t){
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) topicLayout.getLayoutParams();
                                    params.leftMargin = (int)((1 - v) * -animateAmount);
                                    topicLayout.setLayoutParams(params);
                                }
                            };
                            topics.setDuration(300);
                            topicLayout.startAnimation(topics);
                            Animation appSettings = new Animation(){
                                @Override
                                public void applyTransformation(float v, Transformation t){
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) appSettingsLayout.getLayoutParams();
                                    params.leftMargin = (int)(v * animateAmount);
                                    appSettingsLayout.setLayoutParams(params);
                                }
                            };
                            appSettings.setDuration(300);
                            appSettingsLayout.startAnimation(appSettings);
                            Animation questionSettings = new Animation(){
                                @Override
                                public void applyTransformation(float v, Transformation t){
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) questionSettingsLayout.getLayoutParams();
                                    params.leftMargin = (int)(animateAmount + v * animateAmount);
                                    questionSettingsLayout.setLayoutParams(params);
                                }
                            };
                            questionSettings.setDuration(300);
                            questionSettingsLayout.startAnimation(questionSettings);
                        }
                    }
                    else if(tutorialWindow == 2){
                        if(difference < 0) {
                            Animation home = new Animation() {
                                @Override
                                public void applyTransformation(float v, Transformation t) {
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) homeLayout.getLayoutParams();
                                    params.leftMargin = (int) (-animateAmount + v * (-animateAmount));
                                    homeLayout.setLayoutParams(params);
                                }
                            };
                            home.setDuration(300);
                            homeLayout.startAnimation(home);
                            Animation topics = new Animation() {
                                @Override
                                public void applyTransformation(float v, Transformation t) {
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) topicLayout.getLayoutParams();
                                    params.leftMargin = (int) (v * (-animateAmount));
                                    topicLayout.setLayoutParams(params);
                                }
                            };
                            topics.setDuration(300);
                            topicLayout.startAnimation(topics);
                            Animation appSettings = new Animation(){
                                @Override
                                public void applyTransformation(float v, Transformation t){
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) appSettingsLayout.getLayoutParams();
                                    params.leftMargin = (int)((1 - v) * animateAmount);
                                    appSettingsLayout.setLayoutParams(params);
                                }
                            };
                            appSettings.setDuration(300);
                            appSettingsLayout.startAnimation(appSettings);
                            Animation questionSettings = new Animation(){
                                @Override
                                public void applyTransformation(float v, Transformation t){
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) questionSettingsLayout.getLayoutParams();
                                    params.leftMargin = (int)(animateAmount + (1 - v) * animateAmount);
                                    questionSettingsLayout.setLayoutParams(params);
                                }
                            };
                            questionSettings.setDuration(300);
                            questionSettingsLayout.startAnimation(questionSettings);
                        }
                        else{
                            Animation home = new Animation() {
                                @Override
                                public void applyTransformation(float v, Transformation t){
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) homeLayout.getLayoutParams();
                                    params.leftMargin = (int)(2 * -animateAmount + (1 - v) * -animateAmount);
                                    homeLayout.setLayoutParams(params);
                                }
                            };
                            home.setDuration(300);
                            homeLayout.startAnimation(home);
                            Animation topics = new Animation(){
                                @Override
                                public void applyTransformation(float v, Transformation t){
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) topicLayout.getLayoutParams();
                                    params.leftMargin = (int)(-animateAmount + (1 - v) * -animateAmount);
                                    topicLayout.setLayoutParams(params);
                                }
                            };
                            topics.setDuration(300);
                            topicLayout.startAnimation(topics);
                            Animation appSettings = new Animation(){
                                @Override
                                public void applyTransformation(float v, Transformation t){
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) appSettingsLayout.getLayoutParams();
                                    params.leftMargin = (int)((1-v) * -animateAmount);
                                    appSettingsLayout.setLayoutParams(params);
                                }
                            };
                            appSettings.setDuration(300);
                            appSettingsLayout.startAnimation(appSettings);
                            Animation questionSettings = new Animation(){
                                @Override
                                public void applyTransformation(float v, Transformation t){
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) questionSettingsLayout.getLayoutParams();
                                    params.leftMargin = (int)(v * animateAmount);
                                    questionSettingsLayout.setLayoutParams(params);
                                }
                            };
                            questionSettings.setDuration(300);
                            questionSettingsLayout.startAnimation(questionSettings);
                        }
                    }
                    else if(tutorialWindow == 3){
                        if(difference < 0) {
                            Animation home = new Animation() {
                                @Override
                                public void applyTransformation(float v, Transformation t) {
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) homeLayout.getLayoutParams();
                                    params.leftMargin = (int) (2 * -animateAmount + v * (-animateAmount));
                                    homeLayout.setLayoutParams(params);
                                }
                            };
                            home.setDuration(300);
                            homeLayout.startAnimation(home);
                            Animation topics = new Animation() {
                                @Override
                                public void applyTransformation(float v, Transformation t) {
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) topicLayout.getLayoutParams();
                                    params.leftMargin = (int) (-animateAmount + v * (-animateAmount));
                                    topicLayout.setLayoutParams(params);
                                }
                            };
                            topics.setDuration(300);
                            topicLayout.startAnimation(topics);
                            Animation appSettings = new Animation(){
                                @Override
                                public void applyTransformation(float v, Transformation t){
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) appSettingsLayout.getLayoutParams();
                                    params.leftMargin = (int)(v * -animateAmount);
                                    appSettingsLayout.setLayoutParams(params);
                                }
                            };
                            appSettings.setDuration(300);
                            appSettingsLayout.startAnimation(appSettings);
                            Animation questionSettings = new Animation(){
                                @Override
                                public void applyTransformation(float v, Transformation t){
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) questionSettingsLayout.getLayoutParams();
                                    params.leftMargin = (int)((1 - v) * animateAmount);
                                    questionSettingsLayout.setLayoutParams(params);
                                }
                            };
                            questionSettings.setDuration(300);
                            questionSettingsLayout.startAnimation(questionSettings);
                        }
                        else{
                            Log.e("No animation", "No animation n eeded");
                        }
                    }
                    else{
                        endTutorial();
                    }
                }
            }
        });
    }

    public void endTutorial(){
        findViewById(R.id.topic_nav).setClickable(true);
        findViewById(R.id.progress_nav).setClickable(true);
        findViewById(R.id.app_settings_nav).setClickable(true);
        findViewById(R.id.question_settings_nav).setClickable(true);
        ObjectAnimator shrinkX = ObjectAnimator.ofFloat(tutorialView, "scaleX", 0.01f);
        shrinkX.setDuration(100);
        ObjectAnimator shrinkY = ObjectAnimator.ofFloat(tutorialView, "scaleY", 0.01f);
        shrinkY.setDuration(100);
        AnimatorSet animation = new AnimatorSet();
        animation.playTogether(shrinkX, shrinkY);
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                tutorialView.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
            }
        });
        animation.start();
        SharedPreferences prefs = getSharedPreferences(getString(R.string.pref), MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(getString(R.string.tutorial_shown), true);
        edit.apply();
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
    public void onTopicClick(final View r){
        AppSettingsHandler.promptForPassword(this, new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, TopicActivity.class));
            }
        }, new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Password was incorrect", Toast.LENGTH_SHORT).show();
                onTopicClick(r);
            }
        });
    }

    //Function to transition the user to the progress page
    public void onProgressClick(final View r){
        AppSettingsHandler.promptForPassword(this, new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, ProgressActivity.class));
            }
        }, new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Password was incorrect", Toast.LENGTH_SHORT).show();
                onProgressClick(r);
            }
        });
    }

    //Function to transition the user to the topic page
    public void onAppSettingsClick(final View r){
        AppSettingsHandler.promptForPassword(this, new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, AppSettingsActivity.class));
            }
        }, new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Password was incorrect", Toast.LENGTH_SHORT).show();
                onAppSettingsClick(r);
            }
        });
    }

    //Function to transition the user to the topic page
    public void onQuestionSettingsClick(final View r){
        AppSettingsHandler.promptForPassword(this, new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, QuestionSettingsActivity.class));
            }
        }, new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Password was incorrect", Toast.LENGTH_SHORT).show();
                onQuestionSettingsClick(r);
            }
        });
    }
}
