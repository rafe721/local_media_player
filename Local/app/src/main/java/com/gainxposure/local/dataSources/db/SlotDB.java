package com.gainxposure.local.dataSources.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gainxposure.local.R;
import com.gainxposure.local.models.Slot;

import java.util.ArrayList;

/**
 * Created by Rahul on 12/12/2016.
 */

/**  DB Handler class for managing
 * */
public class SlotDB extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "SLOTS";

    private static final String KEY_MEDIA_ID = "MEDIA_ID";
    private static final String KEY_FILE_NAME = "FILE_NAME";
    private static final String KEY_SOURCE = "SOURCE";
    private static final String KEY_PATH = "PATH";
    private static final String KEY_SLOT_NO = "SLOT_NO";


    public SlotDB(Context context) {
        super(context, context.getString(R.string.db_name), null, Integer.parseInt(context.getString(R.string.db_version)));
    }

    public SlotDB(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, context.getString(R.string.db_name), factory, Integer.parseInt(context.getString(R.string.db_version)));
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_MEDIA_ID + " INTEGER, " + KEY_FILE_NAME + " TEXT, " + KEY_SOURCE + " TEXT, " + KEY_PATH + " TEXT, " + KEY_SLOT_NO + " INTEGER)";
        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    /* Inserts a new  Slot */
    public void addSlot(Slot slot) {
        if (null == slot) { // return on null.
            return;
        }
        int current_capacity;
        // 'source' safety check
        if (!"SERVER".equalsIgnoreCase(slot.getSource())){
            slot.setSource("UPLOAD");
            current_capacity = this.getUploadSlotCount();
        } else {
            current_capacity = this.getServerSlotCount();
        }

        // 'slot_no Safety Check
        if (slot.getSlot_no() == 0) {
            slot.setSlot_no(current_capacity + 1);
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MEDIA_ID, slot.getMedia_id());
        values.put(KEY_FILE_NAME, slot.getFile_name());
        values.put(KEY_SOURCE, slot.getSource());
        values.put(KEY_PATH, slot.getPath());
        values.put(KEY_SLOT_NO, slot.getSlot_no());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    /* */
    public ArrayList<Slot> getSlotList() {
        ArrayList<Slot> slotList = new ArrayList<Slot>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_SOURCE + ", " + KEY_SLOT_NO;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Slot slot = new Slot();
                    slot.setMedia_id(Integer.parseInt(cursor.getString(0)));
                    slot.setFile_name(cursor.getString(1));
                    slot.setSource(cursor.getString(2));
                    slot.setPath(cursor.getString(3));
                    slot.setSlot_no(Integer.parseInt(cursor.getString(4)));

                    // Adding slot to list
                    slotList.add(slot);
                } while (cursor.moveToNext());
            }

        } catch (Exception E){
            Log.i("LOCAL", "getSlotList: " + E.getMessage());
        }
        // return contact list
        return slotList;
    }

    /* */
    public Slot getSlotDetails (int slot_no, String source) {
        int current_capacity;
        // 'source' safety check
        if (!"SERVER".equalsIgnoreCase(source)){
            source = "UPLOAD";
            current_capacity = this.getUploadSlotCount();
        } else {
            current_capacity = this.getServerSlotCount();
        }

        // 'slot_no Safety Check
        if (slot_no>current_capacity) {
            slot_no = current_capacity;
        } else if (slot_no <= 0) {
            slot_no = 1;
        }
        Log.i("HandySan", "getSlotDetails: SlotNo: " + slot_no + " AND SOURCE: " + source);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] {KEY_MEDIA_ID,
                        KEY_SLOT_NO, KEY_FILE_NAME}, KEY_SLOT_NO + "=?",
                new String[] { String.valueOf(slot_no) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Slot slot = new Slot(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
        // return contact
        return slot;
    }

    /* */
    public int updateSlot(Slot aSlot) {
        Log.i("HandySan", "updateSlot: Slot: " + aSlot.toString());
        int current_capacity;
        // 'source' safety check
        if (!"SERVER".equalsIgnoreCase(aSlot.getSource())){
            aSlot.setSource("UPLOAD");
            current_capacity = this.getUploadSlotCount();
        } else {
            current_capacity = this.getServerSlotCount();
        }

        // 'slot_no Safety Check
        if (aSlot.getSlot_no()>current_capacity) {
            aSlot.setSlot_no(current_capacity);
        } else if (aSlot.getSlot_no() <= 0) {
            aSlot.setSlot_no(1);
        }

        SQLiteDatabase db = this.getWritableDatabase();
        // get Existing Slot details
        Slot oldSlot = this.getSlotDetails(0, aSlot.getSource());

        ContentValues values = new ContentValues();
        // Source never changes; Slot_No changes to 'UPLOAD' slots are not managed by this method
        if (oldSlot.getMedia_id() != aSlot.getMedia_id()) {
            values.put(KEY_MEDIA_ID, aSlot.getMedia_id());
        }
        if (oldSlot.getFile_name().equalsIgnoreCase(aSlot.getFile_name())) {
            values.put(KEY_FILE_NAME, aSlot.getFile_name());
        }
        if (oldSlot.getPath().equalsIgnoreCase(aSlot.getPath())) {
            values.put(KEY_PATH, aSlot.getPath());
        }

        // updating row
        return db.update(TABLE_NAME, values, KEY_SLOT_NO + " = ?, " + KEY_SOURCE + " = ?", new String[] { String.valueOf(aSlot.getSlot_no()), aSlot.getSource() });

    }

    /* ADD or Delete Slots depending on data From Server */
    public boolean updateSlotCapacity (int slot_count) {
        Log.i("HandySan", "updateSlotCapacity: Slot Count: " + slot_count);
        boolean status = true;
        // get Existing server Slot Capacity
        int count = this.getServerSlotCount();

        Log.i("HandySan", "updateSlotCapacity: Available Count: " + count + " and Query: " + getCountQuery());

        if (count < slot_count) {
            // insert more slots
            try {
                int rows = count + 1;
                while (rows <= slot_count) {
                    Log.i("HandySan", "updateSlotCapacity: Inserting more slots: " + rows);
                    Slot aSlot = new Slot(0, "Empty", "SERVER", "", rows); // as onnly the server slots have a predefined capacity
                    this.addSlot(aSlot);
                    /* SQLiteDatabase write = this.getWritableDatabase();
                    ContentValues slot_row = new ContentValues();
                    slot_row.put(this.KEY_MEDIA_ID, 0);
                    slot_row.put(this.KEY_SLOT_NO, rows);
                    slot_row.put(this.KEY_FILE_NAME, "none");

                    write.insert(TABLE_NAME, null, slot_row); */

                    Log.i("HandySan", "updateSlotCapacity: Insert: Complete");
                    rows++;
                }
            } catch (Exception e) {
                Log.i("HandySan", "updateSlotCapacity: " + e.getMessage() + " Cause "  + e.getCause());
                /* SQLiteDatabase write = this.getWritableDatabase();
                this.onCreate(write); */
            }
        } else if (count > slot_count) {
            // delete slots
            Log.i("HandySan", "updateSlotCapacity: Deleting slots");
            SQLiteDatabase write = this.getWritableDatabase();
            write.execSQL(getDeleteRowsQuery(slot_count));
        } else {
            Log.i("HandySan", "updateSlotCapacity: No Changes to slots");
        }

        return status;
    }

    public boolean deleteSlot (Slot aSlot) {
        Log.i("HandySan", "updateSlotCapacity: File_Name: " + aSlot.getFile_name());
        boolean status = true;

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_SOURCE + " = ? AND " + KEY_SLOT_NO + " = ?",
                new String[] { aSlot.getSource(), String.valueOf(aSlot.getSlot_no()) });
        // update Slot_No for all entries if source if UPLOAD
        this.updateUploadSlotCount(aSlot.getSlot_no());
        db.close();

        return status;
    }

    private boolean updateUploadSlotCount(int slot_no) {
        Log.i("HandySan", "updateSlotCapacity: source: UPLOAD; Slot No: " + slot_no);
        boolean status = true;
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_SOURCE + " = 'UPLOAD' AND " + KEY_SLOT_NO + " >= " + slot_no;
        Log.i("HandySan", "updateSlotCapacity: selectQuery: " + selectQuery);
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ContentValues values = new ContentValues();
                    values.put(KEY_SLOT_NO, cursor.getInt(4) - 1);
                    // Adding slot to list
                    db.update(TABLE_NAME, values, KEY_FILE_NAME + " = ?, " + KEY_SOURCE + " = ?", new String[] { cursor.getString(1), cursor.getString(2) });

                } while (cursor.moveToNext());
            }

        } catch (Exception E){
            Log.i("LOCAL", "updateUploadSlotCount: " + E.getMessage());
        }
        return status;
    }


    /* Private Methods */
    /** COUNT Query */
    private String getCountQuery() {
        return "Select count(*) from " + TABLE_NAME;

    }

    private String getDeleteRowsQuery(int count) {
        return "DELETE FROM " + this.TABLE_NAME + " WHERE " + this.KEY_SLOT_NO + " NOT IN ( select slot_no from " + this.TABLE_NAME + " LIMIT " + count + " ORDER BY " + this.KEY_SLOT_NO + ")";
    }

    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        // return count
        return count;
    }

    public int getServerSlotCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE SOURCE='SERVER'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        // return count
        return count;
    }

    public int getUploadSlotCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE SOURCE='UPLOAD'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        // return count
        return count;
    }
}
