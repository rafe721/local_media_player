package com.gainxposure.local.dataSources.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gainxposure.local.models.Schedule;


/**
 * Created by Rahul on 12/12/2016.
 */

/**  DB Handler class for managing
 * */
public class CampaignDB extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "CAMPAIGN";
    private static final String KEY_CAMPAIGN_ID = "CAMPAIGN_ID";
    private static final String KEY_FILE_NAME = "FILE_NAME";

    /* Properties */
    public static final String PROP_CURRENT_IP = "CURRENT_IP";
    public static final String PROP_CURRENT_PORT = "CURRENT_PORT";

    public CampaignDB(Context context, String name, int version) {
        super(context, name, null, version);
    }

    public CampaignDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_CAMPAIGN_ID + " TEXT PRIMARY KEY, " + KEY_FILE_NAME + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    // Updating single contact
    public int updateSchedule(Schedule schedule) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CAMPAIGN_ID, schedule.getStart_time());
        values.put(KEY_FILE_NAME, schedule.getEnd_time());

        // updating row
        return db.update(TABLE_NAME, values, KEY_FILE_NAME + " = ?",
                new String[] { String.valueOf(schedule.getDay()) });
    }
}
