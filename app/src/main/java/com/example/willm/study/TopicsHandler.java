package com.example.willm.study;

import android.text.InputType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by willm on 12/29/2017.
 */

public class TopicsHandler {
    public static ArrayList<Topic> currentTopics;
    public static Map<String, Topic> allTopics;
    public static Topic simpleAddition = new Topic(){
        public String title = "Single Digit Addition";
        public String cat = "Math";
        public String subCat = "Addition";
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
        @Override
        public String toString(){
            return "" + title;
        }
        @Override
        public String getTitle(){
            return "" + title;
        }
        @Override
        public String getCat(){
            return cat + "";
        }
        @Override
        public String getSubCat(){
            return subCat + "";
        }
    };
    public static Topic simpleMultiplication = new Topic(){
        public String title = "Single Digit Multiplication";
        public String cat = "Math";
        public String subCat = "Multiplication";
        @Override
        public Map<String, String> generateQuestion(){
            Map<String, String> question = new HashMap<>();
            int num1 = (int)(Math.random() * 10);
            int num2 = (int)(Math.random() * 10);
            int ans = num1 * num2;
            question.put("question", num1 + " * " + num2);
            question.put("answer", Integer.toString(ans));
            question.put("answerType", Integer.toString(InputType.TYPE_CLASS_NUMBER));
            return question;
        }
        @Override
        public String toString(){
            return "" + title;
        }
        @Override
        public String getTitle(){
            return "" + title;
        }
        @Override
        public String getCat(){
            return cat + "";
        }
        @Override
        public String getSubCat(){
            return subCat + "";
        }
    };
    public static boolean remove(Topic t){
        return currentTopics.remove(t);
    }
}
