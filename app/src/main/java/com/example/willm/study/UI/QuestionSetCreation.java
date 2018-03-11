package com.example.willm.study.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.willm.study.DBHandler;
import com.example.willm.study.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class QuestionSetCreation extends AppCompatActivity {
    private HashMap<String, String> questions;
    private ArrayList<HashMap<String, String>> listOfQuestions;
    private ListView vocabDisplay;
    private LinearLayout header;
    private LinearLayout emptyState;
    private FloatingActionButton submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_set_creation);
        vocabDisplay = findViewById(R.id.termList);
        questions = new HashMap<>();
        listOfQuestions = new ArrayList<>();
        header = findViewById(R.id.list_header);
        emptyState = findViewById(R.id.term_empty_state);
        submitButton = findViewById(R.id.submit_button);
        updateList();
    }

    public void updateList(){
        vocabDisplay.setAdapter(null);
        final TermAdapter currentTopics = new TermAdapter(this, listOfQuestions, this);
        vocabDisplay.setAdapter(currentTopics);
        if(listOfQuestions.size() == 0){
            //Hide Header
            header.setVisibility(View.INVISIBLE);
            //Show empty state
            emptyState.setVisibility(View.VISIBLE);
        }
        else{
            //Show header
            header.setVisibility(View.VISIBLE);
            //Hide empty state
            emptyState.setVisibility(View.INVISIBLE);
        }
        //Change color of submit set when large enough
        if(listOfQuestions.size() >= 5){
            submitButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.topicsMain)));
        }
        else{
            submitButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        }
    }

    public void remove(int i){
        HashMap<String, String> question = listOfQuestions.get(i);
        String def = "";
        for (String definition : question.keySet()){
            def = definition;
        }
        questions.remove(def);
        listOfQuestions.remove(i);
        updateList();
    }

    public void addNew(View v, String termAnswer, String defAnswer){
        final EditText def = new EditText(this);
        final EditText term = new EditText(this);
        //Set up the layout
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Term and Definition");
        // Set up the input
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        def.setInputType(InputType.TYPE_CLASS_TEXT);
        term.setInputType(InputType.TYPE_CLASS_TEXT);
        //Give hints
        term.setHint(R.string.answer_hint);
        def.setHint(R.string.question_hint);
        //Set Inputs
        term.setText(termAnswer);
        def.setText(defAnswer);
        //Add inputs to view
        layout.addView(term);
        layout.addView(def);


        builder.setView(layout);
        final View view = v;

        //Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String definition = def.getText().toString();
                String answer = term.getText().toString();
                if(definition.equals("") || answer.equals("")){
                    Toast.makeText(getApplicationContext(), "You must give a definition and answer", Toast.LENGTH_SHORT).show();
                    addNew(view, answer, definition);
                }
                else if(questions.containsKey(definition)){
                    Toast.makeText(getApplicationContext(), "That question already exists in this set", Toast.LENGTH_SHORT).show();
                    addNew(view, answer, definition);
                }
                else{
                    questions.put(definition, answer);
                    HashMap<String, String> question= new HashMap<>();
                    question.put(definition, answer);
                    listOfQuestions.add(question);
                    def.setText("");
                    term.setText("");
                    updateList();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void addNew(View v){
        final EditText def = new EditText(this);
        final EditText term = new EditText(this);
        //Set up the layout
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Term and Definition");
        // Set up the input
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        def.setInputType(InputType.TYPE_CLASS_TEXT);
        term.setInputType(InputType.TYPE_CLASS_TEXT);
        //Give hints
        term.setHint(R.string.answer_hint);
        def.setHint(R.string.question_hint);
        //Add inputs to view
        layout.addView(term);
        layout.addView(def);


        builder.setView(layout);
        final View view = v;

        //Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String definition = def.getText().toString();
                String answer = term.getText().toString();
                if(definition.equals("") || answer.equals("")){
                    Toast.makeText(getApplicationContext(), "You must give a definition and answer", Toast.LENGTH_SHORT).show();
                    addNew(view, answer, definition);
                }
                else if(questions.containsKey(definition)){
                    Toast.makeText(getApplicationContext(), "That question already exists in this set", Toast.LENGTH_SHORT).show();
                    addNew(view, answer, definition);
                }
                else{
                    questions.put(definition, answer);
                    HashMap<String, String> question= new HashMap<>();
                    question.put(definition, answer);
                    listOfQuestions.add(question);
                    def.setText("");
                    term.setText("");
                    updateList();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void goHome(){
        startActivity(new Intent(QuestionSetCreation.this, TopicActivity.class));
        finish();
    }

    public void createSet(View v){
        if(questions.keySet().size() < 5){
            Toast.makeText(this, "You need at least 5 items", Toast.LENGTH_LONG).show();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Name Your Question Set");
            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            Log.e("Creating Vocab", questions.toString());
            //Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //try {
                        if(input.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "You must give a name", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            DBHandler db = new DBHandler(getApplicationContext());
                            if(db.getVocabTopic(input.getText().toString()).size() != 0){
                                Toast.makeText(getApplicationContext(), "That name is already taken", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                db.addVocabTopic(input.getText().toString(), questions);
                                goHome();
                            }
                        }
                    //}catch(Exception e){
                    //    Log.e("Creating Vocab", e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
                    //}
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                }
            });
            builder.show();
        }
    }
}
