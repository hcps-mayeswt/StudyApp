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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
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
    // Topics table name
    private static final String TABLE_TOPICS= "Topics";
    // Topics Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TOPIC_CAT = "topic_category";
    private static final String KEY_SUB_CAT = "sub_category";
    private static final String KEY_CURRENT = "current";
    private static final String KEY_MIN = "min_val";
    private static final String KEY_MAX = "max_val";
    //Vocab Table Name
    private static final String TABLE_VOCAB = "Vocab";
    private static final String KEY_DEFINITION = "definition";
    private static final String KEY_TERM = "term";
    private static final String KEY_SET = "set_id";
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mHandler = new Handler();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TOPICS_TABLE = "CREATE TABLE " + TABLE_TOPICS + "("
        + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
        + KEY_TOPIC_CAT + " TEXT," +  KEY_SUB_CAT + " TEXT," + KEY_CURRENT + " BIT," + KEY_MIN + " INT," + KEY_MAX + " INT" + ")";
        String CREATE_VOCAB_TABLE = "CREATE TABLE " + TABLE_VOCAB + "(" + KEY_ID + "INTEGER PRIMARY KEY," + KEY_SET + " TEXT," +
                KEY_DEFINITION + " TEXT," + KEY_TERM + " TEXT" + ")";
        db.execSQL(CREATE_TOPICS_TABLE);
        db.execSQL(CREATE_VOCAB_TABLE);
        addAllTopics(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPICS);
        onCreate(db);
    }

    public void addAllTopics(SQLiteDatabase db){
        addTopic("Single Digit Addition", "Math", "Addition", 0, 10, db);
        addTopic("Double Digit Addition", "Math", "Addition", 0, 100, db);
        addTopic("Multiplication Tables", "Math", "Multiplication", 1, 12, db);
        addTopic("Multiplication to 25", "Math", "Multiplication", 1, 25, db);
        addTopic("Single Digit Subtraction", "Math", "Subtraction", 1, 10, db);
        addTopic("Double Digit Subtraction", "Math", "Subtraction", 0, 100, db);
        addTopic("Simple Division", "Math", "Division", 1, 10, db);
        addTopic("Long Division", "Math", "Division", 1, 25, db);
        addTopic("Single Step Equations", "Math", "Algebra", 1, 1, db);
        addTopic("Two Step Equations", "Math", "Algebra", 2, 2, db);
        addTopic("Factoring Quadratic Equations", "Math", "Algebra", 3, 3, db);
    }

    public ArrayList<String> getSubCategories(String cat){
        ArrayList<String> cats = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TOPICS + " WHERE " + KEY_TOPIC_CAT + "='" + cat + "' AND " + KEY_CURRENT + "=0", null);
        if(cursor.moveToFirst()){
            do{
                if(!cats.contains(cursor.getString(3)) && cursor.getString(3) != null)
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
    public void addTopic(String title, String cat, String subCat, int minVal, int maxVal, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, title); // Topic Name
        values.put(KEY_TOPIC_CAT, cat); // Topic category
        values.put(KEY_SUB_CAT, subCat);// Topic subcategory
        values.put(KEY_CURRENT, (byte)0);// Not current topic
        values.put(KEY_MIN, minVal);//Lowest input value
        values.put(KEY_MAX, maxVal);//Greatest input value
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
    public ArrayList<HashMap<String, String> > getCurrentTopics(){
        ArrayList<HashMap<String, String>> topics = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TOPICS + " WHERE " + KEY_CURRENT + "=1", null);
        if(cursor.moveToFirst()){
            do{
                HashMap<String, String> topic = new HashMap<>();
                //Log.e("Getting Current TopicsHandler", cursor.getString(1));
                //Log.e("Getting Current TopicsHandler", TopicsHandler.allTopics.get(cursor.getString(1)).toString());
                topic.put("name", cursor.getString(1));
                topic.put("min", Integer.toString(cursor.getInt(5)));
                topic.put("max", Integer.toString(cursor.getInt(6)));
                topics.add(topic);
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return topics;
    }
    //Get current topics
    public ArrayList<String> getCurrentTopics(boolean asString){
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
        cursor.close();
        return topics;
    }
    //Get Topics By Category
    public List<String> getTopicsByCategory(String cat){
        List<String> topics = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TOPICS + " WHERE " + KEY_TOPIC_CAT + "='" + cat + "' AND "
                + KEY_CURRENT + "=0" + " OR " + KEY_SUB_CAT + "='" + cat + "'" + " AND " + KEY_CURRENT + "=0" , null);
        if(cursor.moveToFirst()){
            do {
                topics.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return topics;
    }
    //Get Topics By SubCategory
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
        cursor.close();
        return topics;
    }

    //Vocab table functionality
    public void addVocabTopic(String setTitle, HashMap<String, String> terms){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues topicVals = new ContentValues();
        topicVals.put(KEY_NAME, setTitle);
        topicVals.put(KEY_TOPIC_CAT, "Vocab");
        topicVals.put(KEY_CURRENT, (byte)1);
        db.insert(TABLE_TOPICS, null, topicVals);
        SQLiteDatabase reading = this.getReadableDatabase();
        Cursor cursor = reading.rawQuery("SELECT " + KEY_ID + " FROM " + TABLE_TOPICS + " WHERE " + KEY_NAME + "='" +
                setTitle + "'", null);
        String id = "";
        if(cursor.moveToFirst())
            id = cursor.getString(0);
        Log.e("Creating Vocab", terms.toString() + " " + id);
        for (String definition : terms.keySet()){
            Log.e("Creating Vocab", definition);
            String term = terms.get(definition);
            ContentValues termVals = new ContentValues();
            termVals.put(KEY_SET, id);
            termVals.put(KEY_TERM, term);
            termVals.put(KEY_DEFINITION, definition);
            db.insert(TABLE_VOCAB, null, termVals);
        }
        db.close();
        cursor.close();
    }
    public void editVocabTopic(String setTitle, HashMap<String, String> terms){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase reading = this.getReadableDatabase();
        Cursor cursor = reading.rawQuery("SELECT " + KEY_ID + " FROM " + TABLE_TOPICS + " WHERE " + KEY_NAME + "='" +
                setTitle + "'", null);
        String id = "";
        if(cursor.moveToFirst())
            id = cursor.getString(0);
        db.delete(TABLE_VOCAB,KEY_SET + "=?", new String[]{id});
        Log.e("Creating Vocab", terms.toString() + " " + id);
        for (String definition : terms.keySet()){
            Log.e("Creating Vocab", definition);
            String term = terms.get(definition);
            ContentValues termVals = new ContentValues();
            termVals.put(KEY_SET, id);
            termVals.put(KEY_TERM, term);
            termVals.put(KEY_DEFINITION, definition);
            db.insert(TABLE_VOCAB, null, termVals);
        }
        db.close();
        cursor.close();
    }
    public void deleteDBTopic(String setTitle){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase reading = this.getReadableDatabase();
        Cursor cursor = reading.rawQuery("SELECT " + KEY_ID + " FROM " + TABLE_TOPICS + " WHERE " + KEY_NAME + "='" +
                setTitle + "'", null);
        String id = "";
        if(cursor.moveToFirst())
            id = cursor.getString(0);
        db.delete(TABLE_VOCAB,KEY_SET + "=?", new String[]{id});
        db.delete(TABLE_TOPICS, KEY_ID + "=?", new String[]{id});
        db.close();
        cursor.close();
    }
    //Get vocab set from vocab table
    public ArrayList<HashMap<String, String>> getVocabTopic(String setTitle){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_ID + " FROM " + TABLE_TOPICS + " WHERE " + KEY_NAME + "='" + setTitle + "'", null);
        String id = "";
        if(cursor.moveToFirst()){
            id = cursor.getString(0);
        }
        Log.e("Getting Vocab", id);
        cursor = db.rawQuery("SELECT * FROM " + TABLE_VOCAB + " WHERE " + KEY_SET + "='" + id + "'", null);
        ArrayList<HashMap<String, String>> results = new ArrayList<>();
        if(cursor.moveToFirst()){
            Log.e("Getting Vocab","HERE");
            do{
                HashMap<String, String> term = new HashMap<>();
                term.put("def", cursor.getString(2));
                term.put("term", cursor.getString(3));
                results.add(term);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return results;
    }
}
