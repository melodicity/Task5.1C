package com.example.task51c;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

// Helper class to manage the DB
public class playlistDBHelper extends SQLiteOpenHelper implements BaseColumns {
    public static final String DATABASE_NAME = "playlist.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "playlist_table";
    public static final String COLUMN_URL = "url";

    public playlistDBHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    // Create a database for storing data locally
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
        _ID + " INTEGER PRIMARY KEY, " +
        COLUMN_URL + " TEXT NOT NULL);");
    }

    // On version change, delete the old database
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
