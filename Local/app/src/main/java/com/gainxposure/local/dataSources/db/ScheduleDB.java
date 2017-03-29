package com.gainxposure.local.dataSources.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.gainxposure.local.R;
import com.gainxposure.local.models.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul on 12/12/2016.
 */

/**  DB Handler class for managing
 * */
public class ScheduleDB extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "SCHEDULE";
    private static final String KEY_DAY = "DAY";
    private static final String KEY_START_TIME = "START_TIME";
    private static final String KEY_END_TIME = "END_TIME";

    public String[] days = {"Sunday", "Monday", "Tuesday", "", "Tuesday", "Friday", "Saturday"};

    public ScheduleDB(Context context) {
        super(context, context.getString(R.string.db_name), null, Integer.parseInt(context.getString(R.string.db_version)));
    }

    public ScheduleDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, context.getString(R.string.db_name), factory, Integer.parseInt(context.getString(R.string.db_version)));
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_DAY + " TEXT PRIMARY KEY, " + KEY_START_TIME + " TEXT, " + KEY_END_TIME + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
        // insert a default value for all the days.
        for (String day :
             days) {
            this.addSchedule(new Schedule(day, "9:00 AM", "6:00 PM"));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    // Adding new contact
    void addSchedule(Schedule aSchedule) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DAY, aSchedule.getStart_time());
        values.put(KEY_START_TIME, aSchedule.getStart_time());
        values.put(KEY_END_TIME, aSchedule.getEnd_time());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public Schedule getScheduleFor(String aDay) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_DAY,
                        KEY_START_TIME, KEY_END_TIME }, KEY_DAY + "=?",
                new String[] { aDay }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Schedule schedule = new Schedule(cursor.getString(0),
                cursor.getString(1), cursor.getString(2));
         db.close();
        // return schedule
        return schedule;
    }

    // Getting All Contacts
    public List<Schedule> getCompleteSchedule() {
        List<Schedule> scheduleList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Schedule contact = new Schedule();
                contact.setDay(cursor.getString(0));
                contact.setStart_time(cursor.getString(1));
                contact.setEnd_time(cursor.getString(2));
                // Adding contact to list
                scheduleList.add(contact);
            } while (cursor.moveToNext());
        }

        db.close();
        // return contact list
        return scheduleList;
    }

    // Updating single contact
    public int updateSchedule(Schedule schedule) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_START_TIME, schedule.getStart_time());
        values.put(KEY_END_TIME, schedule.getEnd_time());

        // updating row
        return db.update(TABLE_NAME, values, KEY_DAY + " = ?",
                new String[] { String.valueOf(schedule.getDay()) });
    }


}
