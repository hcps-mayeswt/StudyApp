package com.example.willm.study.UI;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.willm.study.DBHandler;
import com.example.willm.study.R;

import java.util.ArrayList;
import java.util.Stack;

public class AddTopicsActivity extends AppCompatActivity {
    ListView addTopicsDisplay;
    ArrayList<String> displayList;
    Stack<String> queryHistory;
    String currentQuery;
    TopicAdapter currentTopics;
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
            }
            Log.e("Going Back", displayList.toString());
            Log.e("Going Back", currentQuery);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    currentTopics.notifyNewData();
                }
            });
            currentTopics = null;
            currentTopics = new TopicAdapter(this, displayList, this, true);
            addTopicsDisplay.setAdapter(currentTopics);
        }
    }

    public ArrayList<String> onListClicked(String listTag){
        Log.e("Adding Topics", queryHistory.toString());
        DBHandler db = new DBHandler(this);
        if(queryHistory.isEmpty()){
            Log.e("Adding Topics", listTag);
            //FIND SUBTOPICS
            queryHistory.push("Categories");
            currentQuery = listTag;
            displayList = db.getSubCategories(listTag);
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
            if(displayList.size() == 0){
                currentQuery = queryHistory.pop();
                displayList = db.getSubCategories(currentQuery);
            }
        }
        return displayList;
    }
}
