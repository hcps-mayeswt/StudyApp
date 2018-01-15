package com.example.willm.study.Topics;

import java.util.Map;

/**
 * Created by willm on 12/29/2017.
 */

public class TopicsHandler {
    public static Map<String, String> getQuestion(String topic){
        TopicFactory factory;
        switch(topic){
            case "Single Digit Addition":
                factory = new AdditionFactory(topic, false);
                break;
            case "Double Digit Addition":
                factory = new AdditionFactory(topic, true);
                break;
            case "Multiplication Tables":
                factory = new MultiplicationFactory(topic, false);
                break;
            case "Multiplication to 100":
                factory = new MultiplicationFactory(topic, true);
                break;
            case "Single Digit Subtraction":
                factory = new SubtractionFactory(topic, false);
                break;
            case "Double Digit Subtraction":
                factory = new SubtractionFactory(topic, true);
                break;
            case "Simple Division":
                factory = new DivisionFactory(topic, false);
                break;
            case "Long Division":
                factory = new DivisionFactory(topic, true);
                break;
            default:
                return null;
        }
        return factory.generateQuestion();
    }
}
