package com.marcacorrida.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.marcacorrida.model.Corrida;
import com.marcacorrida.datasource.DatabaseContract.CorridaEntry;

public class CorridaRepository implements AutoCloseable {
    private CorridaDbHelper dbHelper;

    public CorridaRepository(Context context){
        this.dbHelper = new CorridaDbHelper(context);
    }

    /**
     * Registra a Corrida especificada
     * @param corrida valor a ser salvo
     * @return true se bem sucedido, caso contrario false
     */
    public boolean criar(Corrida corrida) {
        try(SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(CorridaEntry.NOME, corrida.getNome());
            values.put(CorridaEntry.NUM_PASSOS, corrida.getNumPassos());
            values.put(CorridaEntry.DATA, corrida.getData().getTime());
            values.put(CorridaEntry.DURACAO, corrida.getDuracao());
            return db.insert(CorridaEntry.TABLE_NAME, null, values) != -1;
        }
    }

    /**
     * Retorna as Corridas com o nome especificado
     * @param nome nome a ser pesquisado
     * @return zero ou mais Corridas
     */
    @NotNull
    public List<Corrida> buscar(String nome) {
        String[] projection = {
                CorridaEntry._ID,
                CorridaEntry.NOME,
                CorridaEntry.NUM_PASSOS,
                CorridaEntry.DURACAO,
                CorridaEntry.DATA
        };
        String selection = CorridaEntry.NOME + " = ?";
        String[] selectionArgs = { nome };
        String sortOrder = CorridaEntry.NOME + " desc";

        try(SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    CorridaEntry.TABLE_NAME,   // The table to query
                    null,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            )) {

            List<Corrida> corridas = new ArrayList<Corrida>();
            while(cursor.moveToNext()) {
                String nomeCorrida = cursor.getString(cursor.getColumnIndexOrThrow(CorridaEntry.NOME));
                int numPassos = cursor.getInt(cursor.getColumnIndexOrThrow(CorridaEntry.NUM_PASSOS));
                long duracao = cursor.getLong(cursor.getColumnIndexOrThrow(CorridaEntry.DURACAO));
                Date data = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(CorridaEntry.DATA)));
                corridas.add( new Corrida(nomeCorrida, numPassos, duracao, data) );
            }
            return corridas;
        }
    }

    /**
     * Retorna as Corridas realizadas no periodo especificado
     * @param inicio inicio do periodo(inclusivo)
     * @param fim fim do periodo(inclusivo)
     * @return zero ou mais Corridas
     */
    @NotNull
    public List<Corrida> buscar(Date inicio, Date fim) {
        String[] projection = {
                CorridaEntry._ID,
                CorridaEntry.NOME,
                CorridaEntry.NUM_PASSOS,
                CorridaEntry.DURACAO,
                CorridaEntry.DATA
        };
        String selection = CorridaEntry.DATA + " >= ? AND " + CorridaEntry.DATA + " <= ?";
        String[] selectionArgs = { String.valueOf(inicio.getTime()), String.valueOf(fim.getTime()) };
        String sortOrder = CorridaEntry.DATA + " desc";

        try(SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    CorridaEntry.TABLE_NAME,   // The table to query
                    null,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            )) {

            List<Corrida> corridas = new ArrayList<Corrida>();
            while(cursor.moveToNext()) {
                String nomeCorrida = cursor.getString(cursor.getColumnIndexOrThrow(CorridaEntry.NOME));
                int numPassos = cursor.getInt(cursor.getColumnIndexOrThrow(CorridaEntry.NUM_PASSOS));
                long duracao = cursor.getLong(cursor.getColumnIndexOrThrow(CorridaEntry.DURACAO));
                Date data = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(CorridaEntry.DATA)));
                corridas.add( new Corrida(nomeCorrida, numPassos, duracao, data) );
            }
            return corridas;
        }
    }

    /**
     * Retorna as Corridas com numero de passos dentro do intervalo especificado
     * @param passoMin minimo de passos(inclusivo)
     * @param passoMax maximo de passos(inclusivo)
     * @return zero ou mais Corridas
     */
    @NotNull
    public List<Corrida> buscar(int passoMin, int passoMax) {
        String[] projection = {
                CorridaEntry._ID,
                CorridaEntry.NOME,
                CorridaEntry.NUM_PASSOS,
                CorridaEntry.DURACAO,
                CorridaEntry.DATA
        };
        String selection = CorridaEntry.NUM_PASSOS + " >= ? AND " + CorridaEntry.NUM_PASSOS + " <= ?";
        String[] selectionArgs = {  String.valueOf(passoMin), String.valueOf(passoMax) };
        String sortOrder = CorridaEntry.DATA + " desc";

        try(SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    CorridaEntry.TABLE_NAME,   // The table to query
                    null,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            )) {

            List<Corrida> corridas = new ArrayList<Corrida>();
            while(cursor.moveToNext()) {
                String nomeCorrida = cursor.getString(cursor.getColumnIndexOrThrow(CorridaEntry.NOME));
                int numPassos = cursor.getInt(cursor.getColumnIndexOrThrow(CorridaEntry.NUM_PASSOS));
                long duracao = cursor.getLong(cursor.getColumnIndexOrThrow(CorridaEntry.DURACAO));
                Date data = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(CorridaEntry.DATA)));
                corridas.add( new Corrida(nomeCorrida, numPassos, duracao, data) );
            }
            return corridas;
        }
    }

    /**
     * Altera os valores de zero ou mais Corridas com o mesmo nome da corrida especificada
     * @param corrida o novo valor da corrida
     * @param nome nome da corrida a ser atualizada
     * @return numero de linhas afetadas
     */
    public int atualizar(Corrida corrida, String nome) {
        String selection = CorridaEntry.NOME + " = ?";
        String[] selectionArgs = { nome };

        try(SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(CorridaEntry.NOME, corrida.getNome());
            values.put(CorridaEntry.NUM_PASSOS, corrida.getNumPassos());
            values.put(CorridaEntry.DATA, corrida.getData().getTime());
            values.put(CorridaEntry.DURACAO, corrida.getDuracao());
            return db.update(CorridaEntry.TABLE_NAME, values, selection, selectionArgs);
        }
    }

    /**
     * Exclui zero ou mais Corridas com o nome especificado
     * @param nome nome da Corrida a ser excluido
     * @return numero de linhas afetadas
     */
    public int excluir(String nome) {
        String selection = CorridaEntry.NOME + " = ?";
        String[] selectionArgs = { nome };

        try(SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            return db.delete(CorridaEntry.TABLE_NAME, selection, selectionArgs);
        }
    }

    /**
     *  Retorna todas Corridas armazenadas
     * @return zero ou mais Corridas
     */
    @NotNull
    public List<Corrida> listar(boolean ordemCrescente) {
        String sortOrder =  CorridaEntry.DATA + (ordemCrescente ? " asc" : " desc");
        String[] projection = {
                CorridaEntry._ID,
                CorridaEntry.NOME,
                CorridaEntry.NUM_PASSOS,
                CorridaEntry.DURACAO,
                CorridaEntry.DATA
        };

        try(SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    CorridaEntry.TABLE_NAME,   // The table to query
                    null,             // The array of columns to return (pass null to get all)
                    null,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            )) {

            List<Corrida> corridas = new ArrayList<Corrida>();
            while(cursor.moveToNext()) {
                String nome = cursor.getString(cursor.getColumnIndexOrThrow(CorridaEntry.NOME));
                int numPassos = cursor.getInt(cursor.getColumnIndexOrThrow(CorridaEntry.NUM_PASSOS));
                long duracao = cursor.getLong(cursor.getColumnIndexOrThrow(CorridaEntry.DURACAO));
                Date data = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(CorridaEntry.DATA)));
                corridas.add( new Corrida(nome, numPassos, duracao, data) );
            }
            return corridas;
        }
    }


    @Override
    public void close() throws IOException {
        if(dbHelper != null){
            dbHelper.close();
        }
    }

}
