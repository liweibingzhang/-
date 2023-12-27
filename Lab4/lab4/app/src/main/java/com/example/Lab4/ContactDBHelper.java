package com.example.Lab4;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "contacts";
    private static final String COLUMN_PROFILE = "profile";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone_number";

    public ContactDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT);";
        db.execSQL(createTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void saveImg(String base64Image) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PROFILE, base64Image);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public void saveContact(String name, String phone) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }


    @SuppressLint("Range")
    public String getContactName() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{COLUMN_NAME},
                null,
                null,
                null,
                null,
                null);

        String contactName = "";

        if (cursor != null && cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            cursor.close();
        }

        db.close();

        return contactName;
    }

    @SuppressLint("Range")
    public String getPhone() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{COLUMN_PHONE},
                null,
                null,
                null,
                null,
                null);

        String Phone = "";

        if (cursor != null && cursor.moveToFirst()) {
            Phone = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE));
            cursor.close();
        }

        db.close();

        return Phone;
    }

}

