package com.example.willm.study.Topics;

import android.text.InputType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by willm on 1/10/2018.
 */

public class MultiplicationFactory extends TopicFactory {
    public MultiplicationFactory(String name, boolean comp){
        title = name;
        cat = "Math";
        subCat = "Multiplication";
        complex = comp;
    }
    @Override
    public Map<String, String> generateQuestion(){
        Map<String, String> question = new HashMap<>();
        int num1, num2;
        if(complex){
            num1 = (int)(Math.random() * 100) + 1;
            num2 = (int)(Math.random() * 100) + 1;
        }
        else{
            num1 = (int)(Math.random() * 12) + 1;
            num2 = (int)(Math.random() * 12) + 1;
        }
        int ans = num1 * num2;
        question.put("question", num1 + " * " + num2);
        question.put("answer", Integer.toString(ans));
        question.put("answerType", Integer.toString(InputType.TYPE_CLASS_NUMBER));
        return question;
    }
}
