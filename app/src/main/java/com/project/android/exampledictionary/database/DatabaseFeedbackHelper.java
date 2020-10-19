package com.project.android.exampledictionary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.android.exampledictionary.model.Feedback;

import java.util.ArrayList;

public class DatabaseFeedbackHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Feedback";
    public static final String TABLE_NAME = "feedback_table";
    public static final String COLUMN_1 = "ID";
    public static final String COLUMN_2 = "EMAIL";
    public static final String COLUMN_3 = "SUBJECT";
    public static final String COLUMN_4 = "MESSAGE";

    public DatabaseFeedbackHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, EMAIL TEXT, SUBJECT TEXT, MESSAGE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String email, String subject, String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_2, email);
        contentValues.put(COLUMN_3, subject);
        contentValues.put(COLUMN_4, message);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<Feedback> getAllData() {
        ArrayList<Feedback> feedbacksList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        while (res.moveToNext()) {
            String id = res.getString(0);   //0 is the number of id column in your database table
            String email = res.getString(1);
            String subject = res.getString(2);
            String message = res.getString(3);

            Feedback feedback = new Feedback(id, email, subject, message);
            feedbacksList.add(feedback);
        }
        return feedbacksList;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }
}
