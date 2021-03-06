package com.example.willm.study.UI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
    private FrameLayout content;
    private FloatingActionButton removeButton;
    private TextView header;
    String list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_set_creation);
        vocabDisplay = findViewById(R.id.termList);
        questions = new HashMap<>();
        listOfQuestions = new ArrayList<>();
        emptyState = findViewById(R.id.term_empty_state);
        submitButton = findViewById(R.id.submit_button);
        removeButton = findViewById(R.id.remove_button);
        header = findViewById(R.id.custom_questions_header);
        onTouchListener = new MyTouchListener();
        onTouchListener.setActivity(this);
        onTouchListener.setContext(this);
        content = findViewById(R.id.custom_question_set_content);
        list = getIntent().getStringExtra("vocabList");
        getData();
        updateList();
    }

    public void getData(){
        if(list != null){
            DBHandler db = new DBHandler(this);
            ArrayList<HashMap<String, String>> temp = db.getVocabTopic(list);
            for(HashMap<String, String> term : temp){
                questions.put(term.get("def"), term.get("term"));
                HashMap<String, String> question = new HashMap<>();
                question.put(term.get("def"), term.get("term"));
                listOfQuestions.add(question);
                header.setText(list);
            }
        }
        else{
            removeButton.setVisibility(View.INVISIBLE);
        }
    }

    public void updateList(){
        //Show empty state
        emptyState.setVisibility(View.VISIBLE);
        vocabDisplay.setAdapter(null);
        final TermAdapter currentTopics = new TermAdapter(this, listOfQuestions, this, onTouchListener);
        vocabDisplay.setAdapter(currentTopics);
        if(listOfQuestions.size() == 0){
            //Raise content
            float elevInPix = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    4.0f,
                    getResources().getDisplayMetrics()
            );
            ObjectAnimator growX = ObjectAnimator.ofFloat(emptyState, "scaleX", 1f);
            growX.setDuration(500);
            ObjectAnimator growY = ObjectAnimator.ofFloat(emptyState, "scaleY", 1f);
            growY.setDuration(500);
            ObjectAnimator lower = ObjectAnimator.ofFloat(content, "elevation", elevInPix);
            lower.setDuration(500);
            AnimatorSet animation = new AnimatorSet();
            animation.playTogether(growX, growY, lower);
            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //Show empty state
                    emptyState.setVisibility(View.VISIBLE);
                }
            });
            animation.start();
        }
        else{
            ObjectAnimator shrinkX = ObjectAnimator.ofFloat(emptyState, "scaleX", 0.01f);
            shrinkX.setDuration(500);
            ObjectAnimator shrinkY = ObjectAnimator.ofFloat(emptyState, "scaleY", 0.01f);
            shrinkY.setDuration(500);
            ObjectAnimator lower = ObjectAnimator.ofFloat(content, "elevation", 0);
            lower.setDuration(500);
            AnimatorSet animation = new AnimatorSet();
            animation.playTogether(shrinkX, shrinkY, lower);
            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //Hide empty state
                    emptyState.setVisibility(View.INVISIBLE);
                }
            });
            animation.start();
        }
        //Change color of submit set when large enough
        if(listOfQuestions.size() >= 5){
            submitButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.topicsMain)));
        }
        else{
            submitButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        }
    }

    public void remove(View v){
        int i = (int)v.getTag();
        HashMap<String, String> question = listOfQuestions.get(i);
        String def = "";
        for (String definition : question.keySet()){
            def = definition;
        }
        questions.remove(def);
        listOfQuestions.remove(i);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 0.01f);
        scaleX.setDuration(250);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 0.01f);
        scaleY.setDuration(250);
        AnimatorSet animation = new AnimatorSet();
        animation.playTogether(scaleY, scaleX);
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                updateList();
                super.onAnimationEnd(animation);
            }
        });
        animation.start();
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
        def.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        term.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
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

    public void deleteSet(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to permanently delete this set?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBHandler db = new DBHandler(getApplicationContext());
                db.deleteDBTopic(list);
                goHome();
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
            Toast.makeText(this, "You need at least 5 terms", Toast.LENGTH_LONG).show();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Name Your Question Set");
            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            input.setText(list);
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
                            if(db.getVocabTopic(input.getText().toString()).size() != 0 && !list.equals(input.getText().toString())){
                                Toast.makeText(getApplicationContext(), "That name is already taken", Toast.LENGTH_SHORT).show();
                            }
                            else if(db.getVocabTopic(input.getText().toString()).size() == 0) {
                                db.addVocabTopic(input.getText().toString(), questions);
                                list = input.getText().toString();
                                header.setText(list);
                                Toast.makeText(getApplicationContext(), "Set saved as " + list, Toast.LENGTH_SHORT).show();
                                removeButton.setVisibility(View.VISIBLE);
                            }
                            else{
                                db.editVocabTopic(input.getText().toString(), questions);
                                list = input.getText().toString();
                                header.setText(list);
                                Toast.makeText(getApplicationContext(), "Set saved as " + list, Toast.LENGTH_SHORT).show();
                                removeButton.setVisibility(View.VISIBLE);
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
            case MotionEvent.ACTION_CANCEL:
                calcuateDifference(v, position);
                action_down_x = 0;
                action_up_x = 0;
                difference = 0;
                break;
            default:
                Log.e("action", action + "");
        }
        return true;
    }

    private void animate(View v){
        Resources r = mContext.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                Math.max(Math.min(startPoint - difference/5, 0), -85),
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
        if (difference < -300) {
            ImageButton removeButton = v.findViewById(R.id.term_remove);
            removeButton.setTag(position);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.remove(v);
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
        else if(difference > 300){
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
            removeButton.setOnClickListener(null);
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
            params.setMargins(px, 0, 0, 0);
            ImageButton removeButton = v.findViewById(R.id.term_remove);
            removeButton.setLayoutParams(params);
            if(startPoint == 0) {
                Toast.makeText(mContext, "Swipe Right", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(mContext, "Swipe Left to Delete", Toast.LENGTH_SHORT).show();
            }
        }
    }
}