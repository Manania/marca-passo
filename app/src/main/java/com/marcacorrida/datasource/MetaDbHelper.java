package com.marcacorrida.datasource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.marcacorrida.datasource.DatabaseContract.MetaEntry;

public class MetaDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = DatabaseContract.DATABASE_VERSION;
    public static final String DATABASE_NAME = DatabaseContract.DATABASE_NAME;

    private static final String SQL_CREATE_TABLE =
            "create table " + MetaEntry.TABLE_NAME + " (" +
                    MetaEntry._ID + " INTEGER PRIMARY KEY," +
                    MetaEntry.NUM_PASSOS + " INTEGER," +
                    MetaEntry.DATA + " INTEGER)";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseContract.CorridaEntry.TABLE_NAME;

    public MetaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
