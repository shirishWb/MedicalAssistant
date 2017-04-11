package com.whitebirdtechnology.medicalassistant.SqlDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.whitebirdtechnology.medicalassistant.ChatScreen.FeedItemChat;
import com.whitebirdtechnology.medicalassistant.FeedItemUserInfo;
import com.whitebirdtechnology.medicalassistant.R;
import com.whitebirdtechnology.medicalassistant.Sharepreference.ClsSharePreference;

import java.util.ArrayList;

/**
 * Created by dell on 9/4/17.
 */

public class SqlDatabaseChat extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MEDICAL_ASSISTANCE_CHAT.db";
    private static final String TABLE_NAME_USER_INFO = "USER_INFO_TAB";
    private static final String COL_Id = "col_id";
    private static final String COL_ISFAV = "is_favourite";
    private static final String COL_NAME = "name";
    private static final String COL_OCCUPATION = "occupation";
    private static final String COL_PROF_PATH = "img_prof_path";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_MSG = "message";
    private static final String COL_KEY_VALUE = "key_unique";
    private static final String COL_MOBILE_NO = "mobile_no";
    private static final String COL_READ_VALUE = "read_value";
    private static final String COL_TIME = "time";
    private static final String COL_TYPE = "type";
    private static final String CREATE_TABLE_USER_INFO = "CREATE TABLE "
            + TABLE_NAME_USER_INFO + "(" + COL_Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"+COL_ISFAV + " TEXT,"+COL_MOBILE_NO + " TEXT,"+COL_OCCUPATION + " TEXT,"+COL_PROF_PATH + " TEXT,"+COL_USER_ID + " TEXT,"+COL_NAME+" TEXT" + ")";

    ClsSharePreference clsSharePreference;
    Context context;
    public SqlDatabaseChat(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
        clsSharePreference = new ClsSharePreference(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_USER_INFO);
        onCreate(db);
    }
    public void CreateTable(String tableName, FeedItemChat feedItemChat){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + COL_KEY_VALUE + " TEXT PRIMARY KEY,"+COL_TIME + " TEXT,"+COL_READ_VALUE + " TEXT,"+COL_TYPE + " TEXT,"+COL_PROF_PATH + " TEXT," +COL_MOBILE_NO + " TEXT," + COL_MSG + " TEXT"+")";
      /*  database.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " ("
                + COL_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_KEY_VALUE
                + " TEXT NOT NULL, " + COL_TIME + " TEXT NOT NULL, "+ COL_TYPE + " TEXT NOT NULL, "+ COL_PROF_PATH + " TEXT NOT NULL, "+ COL_MOBILE_NO + " TEXT NOT NULL, "+ COL_MSG + " TEXT NOT NULL, "
                + COL_READ_VALUE + " TEXT NOT NULL);");*/
      database.execSQL(query);
    //    database.execSQL("DROP TABLE IF EXISTS " + tableName);
      /*  database.close();
        SQLiteDatabase database1 = this.getWritableDatabase();
*/
        ContentValues cv = new ContentValues();
        cv.put(COL_READ_VALUE, feedItemChat.getStringReadValue());
        cv.put(COL_TYPE, feedItemChat.getStringType());
        cv.put(COL_MOBILE_NO, feedItemChat.getStringMobileNo());
        cv.put(COL_MSG, feedItemChat.getStringMsg());
        cv.put(COL_TIME, feedItemChat.getStringTime());
        cv.put(COL_PROF_PATH,feedItemChat.getStringImg());
        try {
            int row  = database.update(tableName,cv,COL_KEY_VALUE+ "="+feedItemChat.getStringKeyValue(),null);
            if(row==0) {
                cv.put(COL_KEY_VALUE, feedItemChat.getStringKeyValue());
                database.insert(tableName, null, cv);
            }
        }catch (Exception e){
            cv.put(COL_KEY_VALUE, feedItemChat.getStringKeyValue());
            database.insert(tableName, null, cv);
        }

        database.close();
    }
    public void AddUserInfo(FeedItemUserInfo feedItemUserInfo){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ISFAV, feedItemUserInfo.getStringIsFav());
        cv.put(COL_USER_ID, feedItemUserInfo.getStringId());
        cv.put(COL_PROF_PATH, feedItemUserInfo.getStringImgPath());
        cv.put(COL_NAME, feedItemUserInfo.getStringName());
        cv.put(COL_OCCUPATION, feedItemUserInfo.getStringOccu());
        int row  = database.update(TABLE_NAME_USER_INFO,cv,COL_MOBILE_NO+ "="+feedItemUserInfo.getStringMobileNo(),null);
        if(row==0) {
            cv.put(COL_MOBILE_NO, feedItemUserInfo.getStringMobileNo());
            database.insert(TABLE_NAME_USER_INFO, null, cv);
        }

    }
    public ArrayList<FeedItemUserInfo> GetUserInfo(){
        ArrayList<FeedItemUserInfo> arrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+ TABLE_NAME_USER_INFO;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                FeedItemUserInfo feedItemUserInfo = new FeedItemUserInfo();
                feedItemUserInfo.setStringName(cursor.getString(cursor.getColumnIndex(COL_NAME)));
                feedItemUserInfo.setStringOccu(cursor.getString(cursor.getColumnIndex(COL_OCCUPATION)));
                feedItemUserInfo.setStringImgPath(cursor.getString(cursor.getColumnIndex(COL_PROF_PATH)));
                feedItemUserInfo.setStringId(cursor.getString(cursor.getColumnIndex(COL_USER_ID)));
                feedItemUserInfo.setStringIsFav(cursor.getString(cursor.getColumnIndex(COL_ISFAV)));
                feedItemUserInfo.setStringMobileNo(cursor.getString(cursor.getColumnIndex(COL_MOBILE_NO)));
                arrayList.add(feedItemUserInfo);
            }while (cursor.moveToNext());
        }
        return arrayList;
    }
    public boolean isTableExists(String tableName) {
        SQLiteDatabase mDatabase = this.getReadableDatabase();

        Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
    public ArrayList<FeedItemChat> GetChatMsg(String tableName){
        ArrayList<FeedItemChat> arrayList = new ArrayList<>();
        if(isTableExists(tableName)) {
            String selectQuery = "SELECT * FROM " + tableName;
            SQLiteDatabase database1 = this.getReadableDatabase();
            Cursor cursor = database1.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    FeedItemChat feedItemChat = new FeedItemChat();
                    feedItemChat.setStringMsg(cursor.getString(cursor.getColumnIndex(COL_MSG)));
                    feedItemChat.setStringType(cursor.getString(cursor.getColumnIndex(COL_TYPE)));
                    feedItemChat.setStringTime(cursor.getString(cursor.getColumnIndex(COL_TIME)));
                    feedItemChat.setStringReadValue(cursor.getString(cursor.getColumnIndex(COL_READ_VALUE)));
                    if (cursor.getString(cursor.getColumnIndex(COL_MOBILE_NO)).equals(clsSharePreference.GetSharPrf(context.getString(R.string.SharPrfMobileNo))))
                        feedItemChat.setStringFlag("1");
                    else
                        feedItemChat.setStringFlag("2");
                    feedItemChat.setStringMobileNo(cursor.getString(cursor.getColumnIndex(COL_MOBILE_NO)));
                    feedItemChat.setStringImg(cursor.getString(cursor.getColumnIndex(COL_PROF_PATH)));
                    feedItemChat.setStringKeyValue(cursor.getString(cursor.getColumnIndex(COL_KEY_VALUE)));
                    arrayList.add(feedItemChat);
                } while (cursor.moveToNext());
            }
        }
        return arrayList;
    }

}
