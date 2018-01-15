package com.example.willm.study.Topics;

import android.text.InputType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by willm on 1/14/2018.
 */

public class DivisionFactory extends TopicFactory {
    public DivisionFactory(String name, boolean comp){
        title = name;
        cat = "Math";
        subCat = "Division";
        complex = comp;
    }
    @Override
    public Map<String, String> generateQuestion(){
        Map<String, String> question = new HashMap<>();
        int num1, num2, ans;
        if(complex){
            num2 = (int)(Math.random() * 25) + 1;
            ans = (int)(Math.random() * 25) + 1;
        }
        else{
            num2 = (int)(Math.random() * 10) + 1;
            ans = (int)(Math.random() * 10) + 1;
        }
        num1 = ans * num2;
        question.put("question", num1 + " / " + num2);
        question.put("answer", Integer.toString(ans));
        question.put("answerType", Integer.toString(InputType.TYPE_CLASS_NUMBER));
        return question;
    }
}
