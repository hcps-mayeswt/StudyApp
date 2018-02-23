package com.example.willm.study.Topics;

import android.text.InputType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by willm on 1/12/2018.
 */

public class SubtractionFactory extends TopicFactory {
    public SubtractionFactory(String name, int min, int max){
        title = name;
        cat = "Math";
        subCat = "Subtraction";
        minVal = min;
        maxVal = max;
    }
    @Override
    public Map<String, String> generateQuestion(){
        Map<String, String> question = new HashMap<>();
        int num1, num2;
        num1 = (int)(Math.random() * (maxVal - minVal + 1) + minVal);
        num2 = (int)(Math.random() * (maxVal - minVal + 1) + minVal);
        int ans = num1 - num2;
        question.put("question", num1 + " - " + num2);
        question.put("answer", Integer.toString(ans));
        question.put("answerSigned", Integer.toString(InputType.TYPE_NUMBER_FLAG_SIGNED));
        question.put("answerType", Integer.toString(InputType.TYPE_CLASS_NUMBER));
        return question;
    }
}
