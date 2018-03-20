package com.example.willm.study.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    public void refreshData(){
        addTopicsDisplay.setAdapter(null);
        currentTopics = new TopicAdapter(this, displayList, this, true);
        addTopicsDisplay.setAdapter(currentTopics);
    }

    public void createQuestions(){
        Intent startActivity = new Intent(AddTopicsActivity.this, QuestionSetCreation.class);
        //startActivity.putExtra("vocabList", "WWII");
        startActivity(startActivity);
    }

    public void editQuestionSet(String setTitle){
        Intent startActivity = new Intent(AddTopicsActivity.this, QuestionSetCreation.class);
        startActivity.putExtra("vocabList", setTitle);
        startActivity(startActivity);
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

    public ArrayList<String> onListClicked(final String listTag){
        Log.e("Adding Topics", queryHistory.toString());
        final DBHandler db = new DBHandler(this);
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
                if(displayList.size() == 0){
                    while(!queryHistory.isEmpty()){
                        queryHistory.pop();
                    }
                    currentQuery = "Categories";
                    displayList = db.getAllCategories();
                    Toast.makeText(this, "No available topics", Toast.LENGTH_SHORT).show();
                    addTopicsDisplay.addFooterView(createCustomQuestions);
                }
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
            if(currentQuery.equals("Vocab")){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Would you like to edit or add this topic?");
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ADD TOPICS
                        db.updateCurrent(listTag, (byte) 1);
                        displayList = db.getTopicsBySubCategory(currentQuery);
                        Log.e("List ", displayList.toString());
                        while (displayList.size() == 0) {
                            Log.e("List", "In Here, updating data");
                            currentQuery = queryHistory.pop();
                            if (currentQuery.equals("Categories")) {
                                displayList = db.getAllCategories();
                                addTopicsDisplay.addFooterView(createCustomQuestions);
                                refreshData();
                            } else {
                                displayList = db.getSubCategories(currentQuery);
                            }
                            Log.e("List ", displayList.toString());
                        }
                    }
                });
                builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e("List Tag", listTag);
                        editQuestionSet(listTag);
                    }
                });
                builder.show();
            }
            else {
                //ADD TOPICS
                db.updateCurrent(listTag, (byte) 1);
                displayList = db.getTopicsBySubCategory(currentQuery);
                while (displayList.size() == 0) {
                    currentQuery = queryHistory.pop();
                    if (currentQuery.equals("Categories")) {
                        displayList = db.getAllCategories();
                        addTopicsDisplay.addFooterView(createCustomQuestions);
                    } else {
                        displayList = db.getSubCategories(currentQuery);
                    }
                }
            }
        }
        return displayList;
    }
}
