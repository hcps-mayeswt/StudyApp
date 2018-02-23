package com.example.willm.study.Topics;

import java.util.Map;

/**
 * Created by willm on 12/29/2017.
 */

public class TopicsHandler {
    public static Map<String, String> getQuestion(String topic, int minVal, int maxVal){
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
        else{
            return null;
        }
        return factory.generateQuestion();
    }
}
