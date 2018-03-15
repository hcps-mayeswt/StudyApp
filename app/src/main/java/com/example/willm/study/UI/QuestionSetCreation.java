package com.example.willm.study.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorSpace;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.willm.study.DBHandler;
import com.example.willm.study.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionSetCreation extends AppCompatActivity {
    private HashMap<String, String> questions;
    private ArrayList<HashMap<String, String>> listOfQuestions;
    private ListView vocabDisplay;
    private LinearLayout emptyState;
    private FloatingActionButton submitButton;
    private MyTouchListener onTouchListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_set_creation);
        vocabDisplay = findViewById(R.id.termList);
        questions = new HashMap<>();
        listOfQuestions = new ArrayList<>();
        emptyState = findViewById(R.id.term_empty_state);
        submitButton = findViewById(R.id.submit_button);
        onTouchListener = new MyTouchListener();
        onTouchListener.setActivity(this);
        onTouchListener.setContext(this);
        updateList();
    }

    public void updateList(){
        vocabDisplay.setAdapter(null);
        final TermAdapter currentTopics = new TermAdapter(this, listOfQuestions, this, onTouchListener);
        vocabDisplay.setAdapter(currentTopics);
        if(listOfQuestions.size() == 0){
            //Show empty state
            emptyState.setVisibility(View.VISIBLE);
        }
        else{
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

class MyTouchListener implements View.OnTouchListener
{
    private int startPoint = -85;
    private Context mContext;
    private QuestionSetCreation activity;
    private int action_down_x = 0;
    private int action_up_x = 0;
    private int difference = 0;

    public void setContext(Context c){
        mContext = c;
    }
    public void setActivity(QuestionSetCreation a){
        activity = a;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        Integer position = (Integer) v.getTag();
//        Log.e("Pressed", "Something has been pressed");
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                action_down_x = (int) event.getX();
                Log.d("action", "ACTION_DOWN - ");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("action", "ACTION_MOVE - ");
                action_up_x = (int) event.getX();
                difference = action_down_x - action_up_x;
                animate(v);
                Log.e("Moving", "Difference " + difference);
                break;
            case MotionEvent.ACTION_UP:
                Log.d("action", "ACTION_UP - ");
                calcuateDifference(v, position);
                action_down_x = 0;
                action_up_x = 0;
                difference = 0;
                break;
        }
        return true;
    }

    private void animate(View v){
        Resources r = mContext.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                Math.min(startPoint - difference/5, 0),
                r.getDisplayMetrics()
        );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(px, 0, 0, 0);
        ImageButton removeButton = v.findViewById(R.id.term_remove);
        removeButton.setLayoutParams(params);
    }

    private void calcuateDifference(final View v, final int position) {
        if (difference < -400) {
            ImageButton removeButton = v.findViewById(R.id.term_remove);
            removeButton.setTag(position);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.remove((int) v.getTag());
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            startPoint = 0;
            params.setMargins(0, 0, 0, 0);
            removeButton.setLayoutParams(params);
        }
        else if(difference > 15){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            startPoint = -85;
            Resources r = mContext.getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    startPoint,
                    r.getDisplayMetrics()
            );
            params.setMargins(px, 0, 0, 0);
            ImageButton removeButton = v.findViewById(R.id.term_remove);
            removeButton.setLayoutParams(params);
        }
        else{
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            Resources r = mContext.getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    startPoint,
                    r.getDisplayMetrics()
            );
            Log.e("Point to move to", px + " " + startPoint);
            params.setMargins(px, 0, 0, 0);
            ImageButton removeButton = v.findViewById(R.id.term_remove);
            removeButton.setLayoutParams(params);
            Toast.makeText(mContext, "Swipe Left to Delete", Toast.LENGTH_SHORT).show();
        }
    }
}