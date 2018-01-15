package com.example.willm.study.Topics;

import android.text.InputType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by willm on 1/12/2018.
 */

public class SubtractionFactory extends TopicFactory {
    public SubtractionFactory(String name, boolean comp){
        title = name;
        cat = "Math";
        subCat = "Subtraction";
        complex = comp;
    }
    @Override
    public Map<String, String> generateQuestion(){
        Map<String, String> question = new HashMap<>();
        int num1, num2;
        if(complex){
            num1 = (int)(Math.random() * 100);
            num2 = (int)(Math.random() * 100);
        }
        else{
            num1 = (int)(Math.random() * 10);
            num2 = (int)(Math.random() * 10);
        }
        int ans = num1 - num2;
        question.put("question", num1 + " - " + num2);
        question.put("answer", Integer.toString(ans));
        question.put("answerType", Integer.toString(InputType.TYPE_CLASS_NUMBER));
        return question;
    }
}
