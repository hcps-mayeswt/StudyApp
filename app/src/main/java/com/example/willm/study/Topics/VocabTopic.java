package com.example.willm.study.Topics;

import android.content.Context;
import android.text.InputType;

import com.example.willm.study.DBHandler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by willm on 2/26/2018.
 */

public class VocabTopic extends TopicFactory {
    Context context;
    public VocabTopic(String name, Context c){
        title = name;
        context = c;
    }

    @Override
    public HashMap<String, String> generateQuestion(){
        //Get all the vocab words for this topic
        DBHandler db = new DBHandler(context);
        ArrayList<HashMap<String, String>> terms = db.getVocabTopic(title);
        //Pick a random one
        int index = (int)(Math.random() * terms.size());
        HashMap<String, String> term = terms.get(index);
        HashMap<String, String> question = new HashMap<>();
        question.put("question", term.get("def"));
        question.put("answer", term.get("term"));
        question.put("answerSigned", "");
        question.put("answerType", Integer.toString(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS));
        return question;
    }
}
