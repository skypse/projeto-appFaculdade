package com.example.appdoacoes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Tags organizadas por categoria
    private static final String TAG_DB = "DB_INFRA";  // infra do banco
    private static final String TAG_AUTH = "DB_AUTH";  // autenticação
    private static final String TAG_CRUD = "DB_CRUD";  // operações CRUD

    private static final String DATABASE_NAME = "AppDoacoes.db";
    private static final int DATABASE_VERSION = 4;
    public static final String TABLE_DOADORES = "doadores";
    public static final String TABLE_INSTITUICOES = "instituicoes";
    public static final String TABLE_DOACOES = "doacoes";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG_DB, "DatabaseHelper inicializado");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " + TABLE_DOADORES + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL," +
                    "email TEXT," +
                    "telefone TEXT," +
                    "senha TEXT)");

            db.execSQL("CREATE TABLE " + TABLE_INSTITUICOES + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL," +
                    "descricao TEXT," +
                    "telefone TEXT," +
                    "necessidades TEXT)");

            db.execSQL("CREATE TABLE " + TABLE_DOACOES + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_doador INTEGER," +
                    "id_instituicao INTEGER," +
                    "tipo TEXT," +
                    "descricao TEXT," +
                    "data TEXT," +
                    "FOREIGN KEY(id_doador) REFERENCES doadores(id)," +
                    "FOREIGN KEY(id_instituicao) REFERENCES instituicoes(id))");

            Log.i(TAG_DB, "Estrutura do banco criada com sucesso");
        } catch (Exception e) {
            Log.e(TAG_DB, "Falha na criação do banco", e);
            throw e;
        }
    }

    private String hashSenha(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return Base64.encodeToString(
                    digest.digest(senha.getBytes("UTF-8")),
                    Base64.DEFAULT
            );
        } catch (Exception e) {
            Log.e(TAG_AUTH, "Falha ao gerar hash", e);
            throw new RuntimeException("Erro de criptografia");
        }
    }

    public long inserirDoador(String nome, String email, String telefone, String senha) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nome", nome);
            values.put("email", email);
            values.put("telefone", telefone);
            values.put("senha", hashSenha(senha));

            long id = db.insert(TABLE_DOADORES, null, values);

            if (id != -1) {
                Log.i(TAG_CRUD, "Doador cadastrado | ID: " + id);
            } else {
                Log.w(TAG_CRUD, "Falha no cadastro | Nome: " + nome);
            }
            return id;
        } catch (Exception e) {
            Log.e(TAG_CRUD, "Erro ao cadastrar", e);
            return -1;
        } finally {
            if (db != null) db.close();
        }
    }

    public boolean verificarLogin(String nome, String senha) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT id FROM " + TABLE_DOADORES + " WHERE nome = ? AND senha = ?",
                    new String[]{nome, hashSenha(senha)}
            );

            boolean valido = cursor.getCount() > 0;
            Log.i(TAG_AUTH, "Login " + (valido ? "bem-sucedido" : "inválido"));
            return valido;
        } catch (Exception e) {
            Log.e(TAG_AUTH, "Erro na autenticação", e);
            return false;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOACOES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOADORES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTITUICOES);
            onCreate(db);
            Log.w(TAG_DB, "Banco atualizado | v" + oldVersion + "→v" + newVersion);
        } catch (Exception e) {
            Log.e(TAG_DB, "Falha na atualização", e);
        }
    }
}