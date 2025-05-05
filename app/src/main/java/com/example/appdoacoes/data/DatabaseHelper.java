package com.example.appdoacoes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AppDoacoes.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_DOADORES = "doadores";
    public static final String TABLE_INSTITUICOES = "instituicoes";
    public static final String TABLE_DOACOES = "doacoes";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação das tabelas

        String CREATE_DOADORES = "CREATE TABLE " + TABLE_DOADORES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "email TEXT," +
                "telefone TEXT" +
                ");";

        String CREATE_INSTITUICOES = "CREATE TABLE " + TABLE_INSTITUICOES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "descricao TEXT," +
                "telefone TEXT," +
                "necessidades TEXT" +
                ");";

        String CREATE_DOACOES = "CREATE TABLE " + TABLE_DOACOES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_doador INTEGER," +
                "id_instituicao INTEGER," +
                "tipo TEXT," +  // Ex: alimento, roupa, dinheiro
                "descricao TEXT," +
                "data TEXT," +
                "FOREIGN KEY(id_doador) REFERENCES doadores(id)," +
                "FOREIGN KEY(id_instituicao) REFERENCES instituicoes(id)" +
                ");";

        db.execSQL(CREATE_DOADORES);
        db.execSQL(CREATE_INSTITUICOES);
        db.execSQL(CREATE_DOACOES);

        Log.d("DatabaseHelperCreated", "Tabelas criadas com sucesso.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOACOES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOADORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTITUICOES);
        onCreate(db);
    }
}