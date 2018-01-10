package com.example.willm.study;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willm on 1/3/2018.
 */

public class DBHandler extends SQLiteOpenHelper {
    private Context mContext;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "TopicsHandler";
    // Contacts table name
    private static final String TABLE_TOPICS= "TopicsHandler";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TOPIC_CAT = "topic_category";
    private static final String KEY_SUB_CAT = "sub_category";
    private static final String KEY_CURRENT = "current";
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TOPICS + "("
        + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
        + KEY_TOPIC_CAT + " TEXT," +  KEY_SUB_CAT + " TEXT," + KEY_CURRENT + " BIT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPICS);
        onCreate(db);
    }

    public boolean hasTopic(Topic t){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TOPICS + " WHERE " + KEY_NAME + "='" + t.getTitle() + "'", null);
        return cursor.moveToFirst();
    }

    // Adding new topic
    public void addTopic(Topic t) {
        if(!hasTopic(t)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, t.getTitle()); // Topic Name
            values.put(KEY_TOPIC_CAT, t.getCat()); // Topic category
            values.put(KEY_SUB_CAT, t.getSubCat());// Topic subcategory
            // Inserting Row
            db.insert(TABLE_TOPICS, null, values);
            db.close(); // Closing database connection
        }
        TopicsHandler.allTopics.put(t.getTitle(), t);
    }
    //Set a topic as current or not current
    public void updateCurrent(Topic t, byte val){
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("UPDATE " + TABLE_TOPICS + " SET " + KEY_CURRENT + "");
        ContentValues values = new ContentValues();
        values.put(KEY_CURRENT, val);
        db.update(TABLE_TOPICS, values, KEY_NAME + "=?", new String[]{t.getTitle()});
    }
    //Get current topics
    public ArrayList<Topic> getCurrentTopics(){
        ArrayList<Topic> topics = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        if(TopicsHandler.allTopics == null)
            MainActivity.addTopics(mContext);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TOPICS + " WHERE " + KEY_CURRENT + "=1", null);
        if(cursor.moveToFirst()){
            do{
                //Log.e("Getting Current TopicsHandler", cursor.getString(1));
                //Log.e("Getting Current TopicsHandler", TopicsHandler.allTopics.get(cursor.getString(1)).toString());
                topics.add(TopicsHandler.allTopics.get(cursor.getString(1)));
            }while(cursor.moveToNext());
        }
        return topics;
    }
    //Get TopicsHandler By Category
    public List<Topic> getTopicsByCategory(String cat){
        List<Topic> topics = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TOPICS + " WHERE " + KEY_TOPIC_CAT + "='" + cat + "' OR "
                + KEY_SUB_CAT + "='" + cat + "'", null);
        if(cursor.moveToFirst()){
            do {
                Topic t = TopicsHandler.allTopics.get(cursor.getString(1));
                topics.add(t);
            }while(cursor.moveToNext());
        }
        return topics;
    }
}
