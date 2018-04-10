package com.example.willm.study.Topics;

import android.text.InputType;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by willm on 4/9/2018.
 */

public class AlgebraFactory extends TopicFactory {
    public AlgebraFactory(String name, int min, int max){
        title = name;
        cat = "Math";
        subCat = "Addition";
        minVal = min;
        maxVal = max;
    }
    @Override
    public Map<String, String> generateQuestion(){
        Map<String, String> question = new HashMap<>();
        int ans = 0;
        //Single step equations
        if(minVal == 1){
            int rand = (int)(Math.random() * 4);
            //Multiplication
            if(rand == 0){
                ans = (int)(Math.random() * 21) - 10;
                int mult = (int)(Math.random() * 9) + 2;//Number to multiply by
                if((int)(Math.random() * 2) == 1) mult *= -1;
                int prod = mult * ans;
                question.put("question", "Solve for X: " + mult + "X=" + prod);
            }
            //Division
            else if(rand == 1){
                //Numbers to multiply by
                int divisor = (int)(Math.random() * 9) + 2;
                if((int)(Math.random() * 2) == 1) divisor *= -1;
                int quotient = (int)(Math.random() * 21) - 10;
                ans = divisor * quotient;
                question.put("question", "Solve for X: " + "X/" + divisor + "=" + quotient);
            }
            //Addition
            else if(rand == 2){
                ans = (int)(Math.random() * 31) - 15;
                int add = (int)(Math.random() * 15) + 1;//Number to add
                int sum = ans + add;
                question.put("question", "Solve for X: " + "X + " + add + "=" + sum);
            }
            //Subtraction
            else{
                int difference = (int)(Math.random() * 31) - 15;
                int sub = (int)(Math.random() * 15) + 1;
                ans = difference + sub;
                question.put("question", "Solve for X: " + "X - " + sub + "=" + difference);
            }
            question.put("answer", Integer.toString(ans));
            question.put("answerSigned", Integer.toString(InputType.TYPE_NUMBER_FLAG_SIGNED));
            question.put("answerType", Integer.toString(InputType.TYPE_CLASS_NUMBER));
        }
        //2 Step Equations
        else if(minVal == 2){
            int multOrDivide = (int)(Math.random() * 2);
            String equation = "";
            int equationAns;
            //Multiply
            if(multOrDivide == 0){
                ans = (int)(Math.random() * 31)-15;
                int mult = (int)(Math.random() * 9) + 2;
                if((int)(Math.random() * 2) == 1) mult *= -1;
                equationAns = mult * ans;
                equation += mult + "X";
            }
            //Divide
            else{
                equationAns = (int)(Math.random() * 21)-10;
                int divisor = (int)(Math.random() * 14) + 2;
                if((int)(Math.random() * 2) == 1) divisor *= -1;
                ans = equationAns * divisor;
                equation += "X/" + divisor;
            }
            int addOrSubtract = (int)(Math.random() * 2);
            //Add
            if(addOrSubtract == 0){
                int toAdd = (int)(Math.random() * 30) + 1;
                equationAns += toAdd;
                equation += " + " + toAdd;
            }
            //Subtract
            else{
                int toSub = (int)(Math.random() * 30) + 1;
                equationAns -= toSub;
                equation += " - " + toSub;
            }
            //Build question
            equation += "=" + equationAns;
            question.put("question", "Solve for X: " + equation);
            question.put("answer", Integer.toString(ans));
            question.put("answerSigned", Integer.toString(InputType.TYPE_NUMBER_FLAG_SIGNED));
            question.put("answerType", Integer.toString(InputType.TYPE_CLASS_NUMBER));
        }
        //Quadratics
        else{
            int root1 = (int)(Math.random() * 31)-15;
            String factor1;
            if(root1 < 0){
                factor1 = "(X - " + -root1 + ")";
            }
            else if(root1 == 0){
                factor1 = "X";
            }
            else{
                factor1 = "(X + " + root1 + ")";
            }
            int root2 = (int)(Math.random() * 31)-15;
            String factor2;
            if(root2 < 0){
                factor2 = "(X - " + -root2 + ")";
            }
            else if(root2 == 0){
                factor2 = "X";
            }
            else{
                factor2 = "(X + " + root2 + ")";
            }
            String answer = factor1 + factor2;
            String answer2 = factor2 + factor1;
            int b = root1 + root2;
            int c = root1 * root2;
            String equation = "X^2";
            if(b < 0){
                Log.e("HERE", "B is less than 0");
                equation += " - " + -b + "X";
            }
            else if(b == 1){
                equation += " + X";
            }
            else if(b != 0){
                equation += " + " + b + "X";
            }
            if(c < 0){
                equation += " - " + -c;
            }
            else if(c != 0){
                equation += " + " + c;
            }
            question.put("question", "Factor: " + equation);
            question.put("answer", answer);
            question.put("answer2", answer2);
            question.put("answerSigned", "");
            question.put("answerType", Integer.toString(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS));
        }
        return question;
    }
}
