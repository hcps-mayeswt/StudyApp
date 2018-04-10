package com.example.willm.study.Topics;

import android.content.Context;

import java.util.Map;

/**
 * Created by willm on 12/29/2017.
 */

public class TopicsHandler {
    public static Map<String, String> getQuestion(String topic, int minVal, int maxVal, Context c){
        TopicFactory factory;
        if(topic.contains("Addition")){
            factory = new AdditionFactory(topic, minVal, maxVal);
        }
        else if(topic.contains("Subtraction")){
            factory = new SubtractionFactory(topic, minVal, maxVal);
        }
        else if(topic.contains("Multiplication")){
            factory = new MultiplicationFactory(topic, minVal, maxVal);
        }
        else if(topic.contains("Division")){
            factory = new DivisionFactory(topic, minVal, maxVal);
        }
        else if(topic.contains("Equation")){
            factory = new AlgebraFactory(topic, minVal, maxVal);
        }
        else{
            factory = new VocabTopic(topic, c);
        }
        return factory.generateQuestion();
    }
}
