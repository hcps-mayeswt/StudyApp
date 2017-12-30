package com.example.willm.study;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

public class StudyQuestionsActivity extends AppCompatActivity {

    public static final int RESET_TIME = 10 * 60 * 1000;//10 minute

    String appPackage = "";//The app that was opened that triggered this to display

    String correctAnswer = "";//The correct answer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_questions);
        getNextQuestion();
        //Record the app that the screen came from
        appPackage = getIntent().getStringExtra("App");
    }

    public void getNextQuestion(){
        //Generate the question
        Map<String, String> questionMap = Topics.simpleAddition.generateQuestion();
        //Display the question
        TextView questionText = findViewById(R.id.question);
        questionText.setText(questionMap.get("question"));
        //Store the correct answer
        correctAnswer = questionMap.get("answer");
        Log.e("Question Displaying", "Correct answer is " + correctAnswer);
        //Set the input type
        EditText userInput = findViewById(R.id.answer);
        userInput.setInputType(Integer.parseInt(questionMap.get("answerType")));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        if(!hasFocus) {
            Log.e("Other App Monitoring", "REMOVING TASK");
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                Log.e("Other App Monitoring", "REMOVING TASK");
                finishAndRemoveTask();
            } else {
                finish();
            }
        }
    }

    public void moveOn(View v){
        //Get the users answer
        EditText answerInput = findViewById(R.id.answer);
        String userAnswer = answerInput.getText().toString();
        Log.e("Question Displaying", userAnswer);
        if(userAnswer.equals(correctAnswer)) {
            //Write the next time that this needs to be displayed on
            SharedPreferences prefs = getSharedPreferences(getString(R.string.pref), MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(getString(R.string.display_time), System.currentTimeMillis() + RESET_TIME);
            editor.commit();
            //Return to the app that triggered the creation of this screen
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(appPackage);
            startActivity(launchIntent);
            //Remove this activity from the task tray
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                Log.e("Other App Monitoring", "Removing Task");
                finishAndRemoveTask();
            } else {
                finish();
            }
        }
        else{
            getNextQuestion();
        }
    }
}
