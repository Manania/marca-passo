package com.marcacorrida.datasource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import  com.marcacorrida.datasource.DatabaseContract.CorridaEntry;

public class CorridaDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = DatabaseContract.DATABASE_NAME;

    private static final String SQL_CREATE_TABLE =
            "create table " + CorridaEntry.TABLE_NAME + " (" +
                    CorridaEntry._ID + " INTEGER PRIMARY KEY," +
                    CorridaEntry.NOME + " TEXT," +
                    CorridaEntry.DURACAO + " INTEGER," + //sqllite armazena tempo e data como TEXT, REAL OU INTEGER
                    CorridaEntry.NUM_PASSOS + " INTEGER," +
                    CorridaEntry.DATA + " INTEGER)";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + CorridaEntry.TABLE_NAME;

    public CorridaDbHelper(Context context) {
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