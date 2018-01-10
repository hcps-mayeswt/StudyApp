package com.example.willm.study;

import java.util.Map;

/**
 * Created by willm on 12/29/2017.
 */

public abstract class Topic {
    public String title;
    public String cat;
    public String subCat;
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
}
