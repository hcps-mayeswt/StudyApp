package com.example.willm.study;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by willm on 12/22/2017.
 */

public class NavBar extends View {
    private Paint navBar;
    private String pageTitle;
    private boolean currentPage;
    private int color;
    public NavBar(Context context, AttributeSet attrs){
        super(context, attrs);
        //Define the nav bar
        navBar = new Paint();
        //Define the page titles
        //Obtain the current page attribute
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NavBar, 0, 0);
        try{
            currentPage = a.getBoolean(R.styleable.NavBar_currentPage, false);
            pageTitle = a.getString(R.styleable.NavBar_pageTitle);
            color = a.getInteger(R.styleable.NavBar_color, 0);
        }finally{
            a.recycle();
        }
    }
    @Override
    protected void onDraw(Canvas canvas){
        //Draw the navbar
        int viewWidth = this.getMeasuredWidth();
        int viewHeight = this.getMeasuredHeight();
        if(currentPage) {
            //Set current page coloring
            navBar.setColor(getResources().getColor(R.color.colorPrimary));
        }
        else{
            //Set coloring for the nav items
            navBar.setColor(color);
        }
        navBar.setStyle(Paint.Style.FILL);
        navBar.setTextSize(40);
        navBar.setTextAlign(Paint.Align.CENTER);
        canvas.drawRect(0, 0, viewWidth, viewHeight, navBar);
        navBar.setColor(Color.WHITE);
        canvas.drawText(pageTitle.replace(" ", "\n"), viewWidth/2, viewHeight/2 - 5, navBar);
    }
}
