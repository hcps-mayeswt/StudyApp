package com.example.willm.study;

import android.text.InputType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by willm on 12/29/2017.
 */

public class Topics {
    public static Topic simpleAddition = new Topic(){
        public String topicName = "Single Digit Addition";
        @Override
        public Map<String, String> generateQuestion(){
            Map<String, String> question = new HashMap<>();
            int num1 = (int)(Math.random() * 10);
            int num2 = (int)(Math.random() * 10);
            int ans = num1 + num2;
            question.put("question", num1 + " + " + num2);
            question.put("answer", Integer.toString(ans));
            question.put("answerType", Integer.toString(InputType.TYPE_CLASS_NUMBER));
            return question;
        }
    };
}
