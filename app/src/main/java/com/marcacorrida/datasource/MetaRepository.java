package com.marcacorrida.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.marcacorrida.datasource.DatabaseContract.MetaEntry;
import com.marcacorrida.model.Meta;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MetaRepository implements AutoCloseable {
    private MetaDbHelper dbHelper;

    public MetaRepository(Context context){
        this.dbHelper = new MetaDbHelper(context);
    }

    public boolean criar(Meta meta) {
        try(SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(MetaEntry.NUM_PASSOS, meta.getNumPassos());
            values.put(MetaEntry.DATA, meta.getData().getTime());
            return db.insert(MetaEntry.TABLE_NAME, null, values) != -1;
        }
    }

    public int atualizar(Meta meta, Date dia) {
        String selection = MetaEntry.DATA + " = ?";
        String[] selectionArgs = { String.valueOf(dia.getTime()) };

        try(SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(MetaEntry.NUM_PASSOS, meta.getNumPassos());
            values.put(MetaEntry.DATA, meta.getData().getTime());
            return db.update(MetaEntry.TABLE_NAME, values, selection, selectionArgs);
        }
    }

    @NotNull
    public List<Meta> listar(boolean ordemCrescente) {
        String sortOrder =  MetaEntry.DATA + (ordemCrescente ? " asc" : " desc");
        String[] projection = {
                MetaEntry._ID,
                MetaEntry.NUM_PASSOS,
                MetaEntry.DATA
        };

        try(SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    MetaEntry.TABLE_NAME,   // The table to query
                    null,             // The array of columns to return (pass null to get all)
                    null,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            )) {

            List<Meta> metas = new ArrayList<Meta>();
            while(cursor.moveToNext()) {
                int numPassos = cursor.getInt(cursor.getColumnIndexOrThrow(MetaEntry.NUM_PASSOS));
                Date data = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(MetaEntry.DATA)));
                metas.add( new Meta(numPassos, data) );
            }
            return metas;
        }
    }

    @Override
    public void close() {
        dbHelper.close();
    }
}
