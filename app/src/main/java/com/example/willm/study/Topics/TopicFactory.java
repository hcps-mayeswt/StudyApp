package com.example.willm.study.Topics;

import java.util.Map;

/**
 * Created by willm on 12/29/2017.
 */

public abstract class TopicFactory {
    protected String title;
    protected String cat;
    protected String subCat;
    protected int minVal, maxVal;
    public abstract Map<String, String> generateQuestion();
    @Override
    public String toString(){
        return "" + title;
    }
    public String getTitle(){
        return title + "";
    }
    public String getCat(){
        return cat + "";
    }
    public String getSubCat(){
        return subCat + "";
    }
    public int getMinVal(){return minVal;}
    public int getMaxVal(){return maxVal;}
}
