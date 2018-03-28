package com.example.willm.study.UI;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.willm.study.DBHandler;
import com.example.willm.study.QuestionSettingsHandler;
import com.example.willm.study.R;
import com.example.willm.study.Topics.TopicsHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudyQuestionsActivity extends AppCompatActivity {

    public static long resetTime;

    String appPackage = "";//The app that was opened that triggered this to display

    String correctAnswer = "";//The correct answer

    private String lastQuestion;

    int attempted, correct, inARow;
    float defaultTextSize;

    private FrameLayout correctFrame, incorrectFrame;

    DBHandler db;

    private TextView numCorrect, numAttempted, numInARow, lastAnswer, questionText, explanation, answerGuide;

    private TextView neededCorrect, neededAttempted, neededStreak;

    private EditText userInput;

    private LinearLayout answerLayout;

    private FrameLayout userInputLayout;

    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_questions);
        lastQuestion = "";
        db = new DBHandler(this);
        explanation = findViewById(R.id.answerTheFollowing);
        explanation.setHeight((int)(3.0/16.0 * getResources().getDisplayMetrics().widthPixels));
        Log.e("Setting Height", "Height " + getResources().getDisplayMetrics().widthPixels);
        //Find all views I need
        correctFrame = findViewById(R.id.correctFeedback);
        incorrectFrame = findViewById(R.id.incorrectFeedback);
        continueButton = findViewById(R.id.continueButton);
        questionText = findViewById(R.id.question);
        answerLayout = findViewById(R.id.answerLayout);
        answerGuide = findViewById(R.id.answerGuide);
        userInput = findViewById(R.id.answer);
        userInputLayout = findViewById(R.id.user_input_layout);
        neededCorrect = findViewById(R.id.numCorrectGoal);
        neededAttempted = findViewById(R.id.numAttemptedGoal);
        neededStreak = findViewById(R.id.numInARowGoal);
        //Generate the first question
        getNextQuestion();
        //Set listener to change button color
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable == null || editable.toString().equals("")){
                    continueButton.setBackground(getResources().getDrawable(R.drawable.submit_button_background_grey));
                }
                else{
                    continueButton.setBackground(getResources().getDrawable(R.drawable.submit_button_background));
                }
            }
        });
        //Display the goals
        updateGoals();
        resetTime = QuestionSettingsHandler.getUnlockDuration(this);
        //Record the app that the screen came from
        appPackage = getIntent().getStringExtra("App");
        attempted = 0;
        correct = 0;
        inARow = 0;
        //Record the starting question text size
        defaultTextSize = questionText.getTextSize();
        //Find all the needed text views
        numAttempted = findViewById(R.id.numAttempted);
        numCorrect = findViewById(R.id.numCorrect);
        numInARow = findViewById(R.id.numInARow);
        lastAnswer = findViewById(R.id.lastAnswer);
        String fillerText = "The answer to the previous question will be shown here";
        //Remove last answer text
        lastAnswer.setText(fillerText);
    }

    public void updateGoals(){
        String primaryReq = QuestionSettingsHandler.getPreference(this, QuestionSettingsHandler.KEY_PRIMARY_REQ_TYPE);
        String secondaryReq = QuestionSettingsHandler.getPreference(this, QuestionSettingsHandler.KEY_SECONDARY_REQ_TYPE);
        String toDisplayPrimary = "Goal: " + primaryReq.replace("#", QuestionSettingsHandler.getPreference(this, QuestionSettingsHandler.KEY_PRIMARY_REQ) );
        String toDisplaySecondary =  "Goal: " + secondaryReq.replace("#", QuestionSettingsHandler.getPreference(this, QuestionSettingsHandler.KEY_SECONDARY_REQ));

        neededAttempted.setText("");
        neededCorrect.setText("");
        neededStreak.setText("");
        if(primaryReq.equals(getString(R.string.req_attempted))){
            neededAttempted.setText(toDisplayPrimary);
        }
        else if(secondaryReq.equals(getString(R.string.req_attempted))){
            neededAttempted.setText(toDisplaySecondary);
        }

        if(primaryReq.equals(getString(R.string.req_correct))){
            neededCorrect.setText(toDisplayPrimary);
        }
        else if(secondaryReq.equals(getString(R.string.req_attempted))){
            neededCorrect.setText(toDisplaySecondary);
        }

        if(primaryReq.equals(getString(R.string.req_streak))){
            neededStreak.setText(toDisplayPrimary);
        }
        else if(secondaryReq.equals(getString(R.string.req_streak))){
            neededStreak.setText(toDisplaySecondary);
        }
    }

    public void getNextQuestion(){
        //Randomly Choose TopicFactory
        ArrayList<HashMap<String, String>> currentTopics = db.getCurrentTopics();
        Log.e("Current topics", currentTopics.toString());
        int index = (int)(Math.random() * currentTopics.size());
        Log.e("Current topics", index + "");
        String t = currentTopics.get(index).get("name");
        int min = Integer.parseInt(currentTopics.get(index).get("min"));
        int max = Integer.parseInt(currentTopics.get(index).get("max"));
        //Generate the question
        Map<String, String> questionMap = TopicsHandler.getQuestion(t, min, max, this);
        int j = 0;
        while(questionMap.get("question").equals(lastQuestion) && j < 20){
            j++;
            questionMap = TopicsHandler.getQuestion(t, min, max, this);
        }
        //Display the question
        displayQuestion(questionMap.get("question"));
        //Store the correct answer
        correctAnswer = questionMap.get("answer");
        Log.e("Question Displaying", "Correct answer is " + correctAnswer);
        //Set the input type
        if(questionMap.get("answerSigned").equals("")){
            userInput.setInputType(Integer.parseInt(questionMap.get("answerType")));
        }
        else{
            userInput.setInputType(Integer.parseInt(questionMap.get("answerSigned")) | Integer.parseInt(questionMap.get("answerType")));
        }
    }

    public void displayQuestion(String question){
        questionText.setText(question);
        lastQuestion = question;
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
        if(!userAnswer.equals("")) {
            attempted++;
            if (userAnswer.equals(correctAnswer)) {
                correct++;
                inARow++;
                animateFeedback(correctFrame);
            } else {
                inARow = 0;
                animateFeedback(incorrectFrame);
            }
            updateDisplays();
            Log.e("Question Displaying", userAnswer);
            answerInput.setText("");
            continueButton.setBackground(getResources().getDrawable(R.drawable.submit_button_background_grey));
            if (QuestionSettingsHandler.passedReq(this, attempted, correct, inARow)) {
                //Write the next time that this needs to be displayed on
                SharedPreferences prefs = getSharedPreferences(getString(R.string.pref), MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(getString(R.string.display_time), System.currentTimeMillis() + resetTime);
                editor.apply();
                Log.e("Other App Monitoring", resetTime + "");
                //Return to the app that triggered the creation of this screen
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(appPackage);
                try {
                    startActivity(launchIntent);
                } catch (Exception e) {

                }
                //Remove this activity from the task tray
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    Log.e("Other App Monitoring", "Removing Task");
                    finishAndRemoveTask();
                } else {
                    finish();
                }
            } else {
                getNextQuestion();
            }
        }
        else{
            Toast.makeText(this, "Please give an answer", Toast.LENGTH_SHORT).show();
        }
    }

    public void animateFeedback(FrameLayout frame){
        //correctFrame.setVisibility(View.VISIBLE);
        ObjectAnimator animation = ObjectAnimator.ofInt(frame, "visibility", View.VISIBLE );
        animation.setDuration(150);
        animation.setRepeatCount(1);
        animation.setRepeatMode(ValueAnimator.REVERSE);
        animation.start();
    }

    //Update all the helper texts
    public void updateDisplays(){
        numCorrect.setText(getResources().getString(R.string.raw_number, correct));
        numInARow.setText(getResources().getString(R.string.raw_number, inARow));
        numAttempted.setText(getResources().getString(R.string.raw_number, attempted));
        lastAnswer.setText(getResources().getString(R.string.last_answer, correctAnswer));
    }
}
