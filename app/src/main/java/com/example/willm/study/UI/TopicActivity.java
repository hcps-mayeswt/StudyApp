package com.example.willm.study.UI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Path;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.willm.study.DBHandler;
import com.example.willm.study.R;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class TopicActivity extends AppCompatActivity {
    private LinearLayout emptyState;
    private FrameLayout listSpot;
    private ListView currentTopicsDisplay;
    private TextView emptyStateHeader;
    private TextView emptyStateBody;
    private ArrayList<String> currentTopicsList;
    private int largeFabSize, smallFabSize;
    private long animTime;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        TextView currentPage = findViewById(R.id.topic_nav_bar);
        currentPage.setBackground(getDrawable(R.drawable.bg_highlight));

        animTime = 500;
        //Empty State
        emptyState = findViewById(R.id.topics_empty_state);
        listSpot = findViewById(R.id.topic_list_spot);
        emptyStateHeader = findViewById(R.id.topics_empty_state_header);
        emptyStateBody = findViewById(R.id.topics_empty_state_text);

        currentTopicsDisplay = findViewById(R.id.current_topics_list);
        //Floating action button set up
        fab = findViewById(R.id.add_topics);
        Log.e("Positioning", "Start x " + fab.getX() + " Start Y " + fab.getY());
        largeFabSize = (int)TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                100,
                getResources().getDisplayMetrics()
        );
        smallFabSize = (int)TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                24,
                getResources().getDisplayMetrics()
        );
        updateList();
    }

    public void updateList(){
        currentTopicsDisplay.setVisibility(View.VISIBLE);
        emptyState.setVisibility(View.INVISIBLE);
        //Add TopicsHandler Button
        final ImageButton addTopics = new ImageButton(this);
        addTopics.setImageDrawable(getDrawable(R.drawable.add_topics_small));
        addTopics.setBackground(null);
        addTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddTopics(view);
            }
        });
        DBHandler db = new DBHandler(this);
        currentTopicsList = db.getCurrentTopics(true);
        Log.e("Reading from database", "Current " + currentTopicsList.toString());
        //Fill the topics list
        if(currentTopicsList != null && currentTopicsList.size() > 0) {
            currentTopicsDisplay.setAdapter(null);
            final TopicAdapter currentTopics = new TopicAdapter(this, currentTopicsList, this, false);
            currentTopicsDisplay.setAdapter(currentTopics);
            if(currentTopicsDisplay.getFooterViewsCount() == 0) {
                //currentTopicsDisplay.addFooterView(addTopics);
            }
            moveFabToCorner();
            //listSpot.removeView(emptyState);
            currentTopicsDisplay.deferNotifyDataSetChanged();
        }
        else{
            showEmptyStateNoAnimation();
            moveFabToCenter();
        }
    }

    public void moveFabToCorner(){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        int padding = (int)TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                getResources().getDisplayMetrics()
        );
        params.gravity = Gravity.BOTTOM|Gravity.RIGHT;
        params.setMargins(padding, padding, padding, padding);
        fab.setLayoutParams(params);
        //fab.setPadding(padding, padding, padding, padding);
    }

    public void moveFabToCenter(){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                largeFabSize,
                largeFabSize
        );
        params.gravity = Gravity.CENTER;
        Log.e("Padding To Center ", largeFabSize + "");
        fab.setLayoutParams(params);
    }

    public void animateFABToCenter(){
        final FloatingActionButton temp = new FloatingActionButton(this);
        temp.setLayoutParams(fab.getLayoutParams());
        temp.setImageResource(R.drawable.ic_add_black_24dp);
        temp.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.topicsMain)));
        temp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg)));
        temp.setX(fab.getX());
        temp.setY(fab.getY());
        listSpot.addView(temp);
        final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                largeFabSize,
                largeFabSize
        );
        params.gravity = Gravity.CENTER;
        float scaleAmount = (float)fab.getWidth()/(float)largeFabSize;
        final float[] locations = new float[2];
        locations[0] = listSpot.getWidth()/2 - largeFabSize/2;
        locations[1] = listSpot.getHeight()/2 - largeFabSize/2;
        fab.setScaleX(scaleAmount);
        fab.setScaleY(scaleAmount);
        fab.setVisibility(View.INVISIBLE);
        fab.setLayoutParams(params);
        ObjectAnimator visib = ObjectAnimator.ofInt(fab, "visibility", View.VISIBLE);
        visib.setDuration(25);
        ObjectAnimator animX = ObjectAnimator.ofFloat(fab, "x", locations[0]);
        animX.setDuration(animTime);
        ObjectAnimator animY = ObjectAnimator.ofFloat(fab, "y", locations[1]);
        animY.setDuration(animTime);
        ObjectAnimator animScaleX = ObjectAnimator.ofFloat(fab, "scaleX", 1f);
        animScaleX.setDuration(animTime);
        ObjectAnimator animScaleY = ObjectAnimator.ofFloat(fab, "scaleY", 1f);
        animScaleY.setDuration(animTime);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY, animScaleX, animScaleY, visib);
        animSetXY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation){
                listSpot.removeView(temp);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                fab.setScaleX(1f);
                fab.setScaleY(1f);
                fab.setLayoutParams(params);
                Log.e("Animation Finished", "X " + fab.getX() + " Y " + fab.getY());
            }
            @Override
            public void onAnimationCancel(Animator animation){
                Log.e("Animation Canceled ", animation.toString());
            }
        });
        animSetXY.start();
        Log.e("Padding To Center ", "X " + locations[0] + " Y " + locations[1]);
    }

    public void showEmptyStateNoAnimation(){
        emptyState.setVisibility(View.VISIBLE);
        currentTopicsDisplay.setVisibility(View.INVISIBLE);
    }

    public void showEmptyState(){
        //listSpot.removeAllViews();
        //listSpot.addView(emptyState);
        float elevation = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                3,
                getResources().getDisplayMetrics()
        );
        float startElev = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                -1,
                getResources().getDisplayMetrics()
        );
        emptyState.setScaleX(.001f);
        emptyState.setScaleY(.001f);
        emptyState.setElevation(startElev);
        emptyState.setVisibility(View.VISIBLE);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(emptyState, "scaleX", 1f);
        scaleX.setDuration(animTime);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(emptyState, "scaleY", 1f);
        scaleY.setDuration(animTime);
        ObjectAnimator raise = ObjectAnimator.ofFloat(emptyState, "elevation", elevation);
        raise.setDuration(animTime);
        AnimatorSet animation = new AnimatorSet();
        animation.playTogether(scaleX, scaleY, raise);
        animation.start();
        currentTopicsDisplay.setVisibility(View.INVISIBLE);
        //emptyStateBody.setVisibility(View.VISIBLE);
        //emptyStateHeader.setVisibility(View.VISIBLE);
    }

    //Ensure that the topics page is always properly loaded
    @Override
    public void onRestart() {
        Log.e("Restarted", "Restarted");
        super.onRestart();
        updateList();
    }

    public void remove(View v){
        final DBHandler db = new DBHandler(getApplicationContext());
        db.updateCurrent((String) v.getTag(), (byte) 0);

        currentTopicsList.remove(v.getTag());
        ObjectAnimator shrinkX = ObjectAnimator.ofFloat(v, "scaleX", 0.01f);
        shrinkX.setDuration(250);
        ObjectAnimator shrinkY = ObjectAnimator.ofFloat(v, "scaleY", 0.01f);
        shrinkY.setDuration(250);
        AnimatorSet animation = new AnimatorSet();
        animation.playTogether(shrinkX, shrinkY);
        final TopicActivity activity = this;
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (db.getCurrentTopics().size() == 0) {
                    showEmptyState();
                    animateFABToCenter();
                }
                currentTopicsDisplay.setAdapter(null);
                final TopicAdapter currentTopics = new TopicAdapter(getApplicationContext(), currentTopicsList, activity, false);
                currentTopicsDisplay.setAdapter(currentTopics);
                super.onAnimationEnd(animation);
            }
        });
        animation.start();
    }

    //NAVBAR
    public void goToAddTopics(View view){
        startActivity(new Intent(TopicActivity.this, AddTopicsActivity.class));
    }

    public void goToTopics(View view){}

    public void goToProgress(View view){
        startActivity(new Intent(TopicActivity.this, ProgressActivity.class));
        finish();
    }

    public void goToAppSettings(View view){
        startActivity(new Intent(TopicActivity.this, AppSettingsActivity.class));
        finish();
    }

    public void goToQuestionSettings(View view){
        startActivity(new Intent(TopicActivity.this, QuestionSettingsActivity.class));
        finish();
    }
}
