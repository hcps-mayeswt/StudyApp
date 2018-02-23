package com.example.willm.study.Topics;

import android.text.InputType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by willm on 1/14/2018.
 */

public class DivisionFactory extends TopicFactory {
    public DivisionFactory(String name, int min, int max){
        title = name;
        cat = "Math";
        subCat = "Division";
        minVal = min;
        maxVal = max;
    }
    @Override
    public Map<String, String> generateQuestion(){
        Map<String, String> question = new HashMap<>();
        int num1, num2, ans;
        num2 = (int)(Math.random() * (maxVal - minVal + 1) + minVal);
        ans = (int)(Math.random() * (maxVal - minVal + 1) + minVal);
        num1 = ans * num2;
        question.put("question", num1 + " / " + num2);
        question.put("answer", Integer.toString(ans));
        question.put("answerSigned", "");
        question.put("answerType", Integer.toString(InputType.TYPE_CLASS_NUMBER));
        return question;
    }
}
