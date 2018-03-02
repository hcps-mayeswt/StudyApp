package com.example.willm.study.UI;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_set_creation);
        vocabDisplay = findViewById(R.id.termList);
        questions = new HashMap<>();
        listOfQuestions = new ArrayList<>();
        updateList();
    }

    public void updateList(){
        vocabDisplay.setAdapter(null);
        final TermAdapter currentTopics = new TermAdapter(this, listOfQuestions, this);
        vocabDisplay.setAdapter(currentTopics);
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

    public void addNew(View v){
        EditText def = findViewById(R.id.def);
        EditText term = findViewById(R.id.addTerm);
        String definition = def.getText().toString();
        String answer = term.getText().toString();
        if(definition.equals("") || answer.equals("")){
            Toast.makeText(this, "You must give a definition and answer", Toast.LENGTH_SHORT).show();
        }
        else if(questions.containsKey(definition)){
            Toast.makeText(this, "That question already exists in this set", Toast.LENGTH_SHORT).show();
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
                            Log.e("Creating Vocab", "HERE" );
                            DBHandler db = new DBHandler(getApplicationContext());
                            db.addVocabTopic(input.getText().toString(), questions);
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
