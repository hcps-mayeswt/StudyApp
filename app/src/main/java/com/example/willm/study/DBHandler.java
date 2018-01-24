package com.example.willm.study;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.util.Log;

import com.example.willm.study.Topics.TopicFactory;
import com.example.willm.study.Topics.TopicsHandler;
import com.example.willm.study.UI.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willm on 1/3/2018.
 */

public class DBHandler extends SQLiteOpenHelper {
    private Context mContext;
    private Handler mHandler;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Topics";
    // Contacts table name
    private static final String TABLE_TOPICS= "Topics";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TOPIC_CAT = "topic_category";
    private static final String KEY_SUB_CAT = "sub_category";
    private static final String KEY_CURRENT = "current";
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mHandler = new Handler();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("Creating Database", "CREATING");
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TOPICS + "("
        + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
        + KEY_TOPIC_CAT + " TEXT," +  KEY_SUB_CAT + " TEXT," + KEY_CURRENT + " BIT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        addAllTopics(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPICS);
        onCreate(db);
    }

    public void addAllTopics(SQLiteDatabase db){
        addTopic("Single Digit Addition", "Math", "Addition", db);
        addTopic("Double Digit Addition", "Math", "Addition", db);
        addTopic("Multiplication Tables", "Math", "Multiplication", db);
        addTopic("Multiplication to 100", "Math", "Multiplication", db);
        addTopic("Single Digit Subtraction", "Math", "Subtraction", db);
        addTopic("Double Digit Subtraction", "Math", "Subtraction", db);
        addTopic("Simple Division", "Math", "Division", db);
        addTopic("Long Division", "Math", "Division", db);
    }

    public ArrayList<String> getSubCategories(String cat){
        ArrayList<String> cats = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TOPICS + " WHERE " + KEY_TOPIC_CAT + "='" + cat + "'", null);
        if(cursor.moveToFirst()){
            do{
                if(!cats.contains(cursor.getString(3)))
                    cats.add(cursor.getString(3));
            }while(cursor.moveToNext());
        }
        db.close();
        return cats;
    }

    public ArrayList<String> getAllCategories(){
        ArrayList<String> cats = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TOPICS, null);
        if(cursor.moveToFirst()){
            do{
                if(!cats.contains(cursor.getString(2)))
                    cats.add(cursor.getString(2));
            }while(cursor.moveToNext());
        }
        db.close();
        return cats;
    }

    // Adding new topic
    public void addTopic(String title, String cat, String subCat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, title); // Topic Name
        values.put(KEY_TOPIC_CAT, cat); // Topic category
        values.put(KEY_SUB_CAT, subCat);// Topic subcategory
        // Inserting Row
        db.insert(TABLE_TOPICS, null, values);
        db.close(); // Closing database connection
    }
    // Adding new topic
    public void addTopic(String title, String cat, String subCat, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, title); // Topic Name
        values.put(KEY_TOPIC_CAT, cat); // Topic category
        values.put(KEY_SUB_CAT, subCat);// Topic subcategory
        values.put(KEY_CURRENT, (byte)0);// Not current topic
        // Inserting Row
        db.insert(TABLE_TOPICS, null, values);
    }

    //Set a topic as current or not current
    public void updateCurrent(String t, byte val){
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("UPDATE " + TABLE_TOPICS + " SET " + KEY_CURRENT + "");
        ContentValues values = new ContentValues();
        values.put(KEY_CURRENT, val);
        db.update(TABLE_TOPICS, values, KEY_NAME + "=?", new String[]{t});
        db.close();
    }
    //Get current topics
    public ArrayList<String> getCurrentTopics(){
        ArrayList<String> topics = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TOPICS + " WHERE " + KEY_CURRENT + "=1", null);
        if(cursor.moveToFirst()){
            do{
                //Log.e("Getting Current TopicsHandler", cursor.getString(1));
                //Log.e("Getting Current TopicsHandler", TopicsHandler.allTopics.get(cursor.getString(1)).toString());
                topics.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }
        db.close();
        return topics;
    }
    //Get TopicsHandler By Category
    public List<String> getTopicsByCategory(String cat){
        List<String> topics = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TOPICS + " WHERE " + KEY_TOPIC_CAT + "='" + cat + "' OR "
                + KEY_SUB_CAT + "='" + cat + "'", null);
        if(cursor.moveToFirst()){
            do {
                topics.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }
        db.close();
        return topics;
    }
    //Get TopicsHandler By Category
    public ArrayList<String> getTopicsBySubCategory(String cat){
        ArrayList<String> topics = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TOPICS + " WHERE " + KEY_SUB_CAT + "='" + cat + "' AND "
                + KEY_CURRENT + "=0", null);
        Log.e("Getting Things", "SELECT * FROM " + TABLE_TOPICS + " WHERE " + KEY_SUB_CAT + "='" + cat + "' AND "
                + KEY_CURRENT + "=0");
        if(cursor.moveToFirst()){
            do {
                Log.e("Getting Things", cursor.getString(1));
                topics.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }
        db.close();
        return topics;
    }
}
