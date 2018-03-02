package com.example.willm.study.UI;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.willm.study.DBHandler;
import com.example.willm.study.R;

import java.util.ArrayList;
import java.util.Stack;

public class AddTopicsActivity extends AppCompatActivity {
    private ListView addTopicsDisplay;
    private ArrayList<String> displayList;
    private Stack<String> queryHistory;
    private String currentQuery;
    private TopicAdapter currentTopics;
    private TextView createCustomQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topics);

        queryHistory = new Stack<>();
        addTopicsDisplay = findViewById(R.id.add_topics_list);
        DBHandler db = new DBHandler(this);
        displayList = db.getAllCategories();
        currentTopics = new TopicAdapter(this, displayList, this, true);
        addTopicsDisplay.setAdapter(currentTopics);
        createCustomQuestions = new TextView(this);
        createCustomQuestions.setText(R.string.custom_questions);
        createCustomQuestions.setGravity(Gravity.CENTER);
        createCustomQuestions.setTextSize(22);
        createCustomQuestions.setTextColor(Color.BLACK);
        createCustomQuestions.setPadding(0, 50, 0, 50);
        createCustomQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createQuestions();
            }
        });
        addTopicsDisplay.addFooterView(createCustomQuestions);
    }

    public void createQuestions(){
        startActivity(new Intent(AddTopicsActivity.this, QuestionSetCreation.class));
    }

    @Override
    public void onBackPressed(){
        if(queryHistory.isEmpty()){
            super.onBackPressed();
        }
        else{
            DBHandler db = new DBHandler(this);
            currentQuery = queryHistory.pop();
            if(currentQuery.equals("Categories")){
                displayList = db.getAllCategories();
            }
            else{
                displayList = db.getSubCategories(currentQuery);
                if(displayList.size() == 0){
                    currentQuery = queryHistory.pop();
                    displayList = db.getAllCategories();
                }
            }
            Log.e("Going Back", displayList.toString());
            Log.e("Going Back", currentQuery);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    currentTopics.notifyNewData();
                }
            });
            if(currentQuery.equals("Categories")){
                addTopicsDisplay.addFooterView(createCustomQuestions);
            }
            else{
                addTopicsDisplay.removeFooterView(createCustomQuestions);
            }
            currentTopics = null;
            currentTopics = new TopicAdapter(this, displayList, this, true);
            addTopicsDisplay.setAdapter(currentTopics);
        }
    }

    public ArrayList<String> onListClicked(String listTag){
        Log.e("Adding Topics", queryHistory.toString());
        DBHandler db = new DBHandler(this);
        addTopicsDisplay.removeFooterView(createCustomQuestions);
        if(queryHistory.isEmpty()){
            Log.e("Adding Topics", listTag);
            //FIND SUBTOPICS
            queryHistory.push("Categories");
            currentQuery = listTag;
            displayList = db.getSubCategories(listTag);
            Log.e("Displaying Topics", displayList.toString());
            if(displayList.size() == 0){
                queryHistory.push(currentQuery);
                displayList = (ArrayList<String>)db.getTopicsByCategory(listTag);
            }
            Log.e("Adding Topics", displayList.toString());
        }
        else if(queryHistory.size() == 1){
            queryHistory.push(currentQuery);
            currentQuery = listTag;
            displayList = db.getTopicsBySubCategory(listTag);
            if(displayList.size() == 0){
                currentQuery = queryHistory.pop();
                displayList = db.getSubCategories(currentQuery);
            }
            Log.e(currentQuery, displayList.toString());
        }
        else{
            //ADD TOPICS
            db.updateCurrent(listTag, (byte)1);
            displayList = db.getTopicsBySubCategory(currentQuery);
            while(displayList.size() == 0){
                currentQuery = queryHistory.pop();
                if(currentQuery.equals("Categories")){
                    displayList = db.getAllCategories();
                    addTopicsDisplay.addFooterView(createCustomQuestions);
                }
                else{
                    displayList = db.getSubCategories(currentQuery);
                }
            }
        }
        return displayList;
    }
}
