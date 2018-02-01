package com.example.willm.study.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.willm.study.DBHandler;
import com.example.willm.study.QuestionSettingsHandler;
import com.example.willm.study.R;
import com.example.willm.study.Topics.TopicsHandler;

import java.util.ArrayList;
import java.util.Map;

public class StudyQuestionsActivity extends AppCompatActivity {

    public static long resetTime;

    String appPackage = "";//The app that was opened that triggered this to display

    String correctAnswer = "";//The correct answer

    int attempted, correct, inARow;

    DBHandler db;

    TextView numCorrect, numAttempted, numInARow, lastAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_questions);
        db = new DBHandler(this);
        getNextQuestion();
        resetTime = QuestionSettingsHandler.getUnlockDuration(this);
        //Record the app that the screen came from
        appPackage = getIntent().getStringExtra("App");
        attempted = 0;
        correct = 0;
        inARow = 0;
        //Find all the needed text views
        numAttempted = findViewById(R.id.numAttempted);
        numCorrect = findViewById(R.id.numCorrect);
        numInARow = findViewById(R.id.numInARow);
        lastAnswer = findViewById(R.id.lastAnswer);
        //Remove last answer text
        lastAnswer.setText("");
    }

    public void getNextQuestion(){
        //Randomly Choose TopicFactory
        ArrayList<String> currentTopics = db.getCurrentTopics();
        Log.e("Current topics", currentTopics.toString());
        int index = (int)(Math.random() * currentTopics.size());
        Log.e("Current topics", index + "");
        String t = currentTopics.get(index);
        Log.e("Current topics", t);
        //Generate the question
        Map<String, String> questionMap = TopicsHandler.getQuestion(t);
        //Display the question
        TextView questionText = findViewById(R.id.question);
        questionText.setText(questionMap.get("question"));
        //Store the correct answer
        correctAnswer = questionMap.get("answer");
        Log.e("Question Displaying", "Correct answer is " + correctAnswer);
        //Set the input type
        EditText userInput = findViewById(R.id.answer);
        if(questionMap.get("answerSigned").equals("")){
            userInput.setInputType(Integer.parseInt(questionMap.get("answerType")));
        }
        else{
            userInput.setInputType(Integer.parseInt(questionMap.get("answerSigned")) | Integer.parseInt(questionMap.get("answerType")));
        }
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
        if(!userAnswer.equals("")) attempted++;
        if(userAnswer.equals(correctAnswer)){
            correct++;
            inARow++;
        }
        else{
            inARow = 0;
        }
        updateDisplays();
        Log.e("Question Displaying", userAnswer);
        answerInput.setText("");
        if(QuestionSettingsHandler.passedReq(this, attempted, correct, inARow)) {
            //Write the next time that this needs to be displayed on
            SharedPreferences prefs = getSharedPreferences(getString(R.string.pref), MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(getString(R.string.display_time), System.currentTimeMillis() + resetTime);
            editor.apply();
            Log.e("Other App Monitoring", resetTime + "");
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

    //Update all the helper texts
    public void updateDisplays(){
        numCorrect.setText(getResources().getString(R.string.raw_number, correct));
        numInARow.setText(getResources().getString(R.string.raw_number, inARow));
        numAttempted.setText(getResources().getString(R.string.raw_number, attempted));
        lastAnswer.setText(getResources().getString(R.string.last_answer, correctAnswer));
    }
}
