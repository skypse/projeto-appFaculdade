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
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Tags organizadas por categoria
    private static final String TAG_DB = "DB_INFRA";  // infra do banco
    private static final String TAG_AUTH = "DB_AUTH";  // autenticação
    private static final String TAG_CRUD = "DB_CRUD";  // operações CRUD

    private static final String DATABASE_NAME = "AppDoacoes.db";
    private static final int DATABASE_VERSION = 8;
    public static final String TABLE_DOADORES = "doadores";
    public static final String TABLE_INSTITUICOES = "instituicoes";
    public static final String TABLE_DOACOES = "doacoes";

    public static final String COL_TIPO = "tipo";
    public static final String COL_DESCRICAO = "descricao";
    public static final String COL_INSTITUICAO = "nome_instituicao";
    public static final String COL_DATA = "data";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG_DB, "DatabaseHelper inicializado");
        inicializarDadosTeste();
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
    public boolean verificarLoginEmail(String email, String senha) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT id FROM " + TABLE_DOADORES +
                            " WHERE email = ? AND senha = ?",
                    new String[]{email, hashSenha(senha)}
            );

            boolean valido = cursor.getCount() > 0;
            Log.i(TAG_AUTH, "Login por email " + (valido ? "bem-sucedido" : "inválido"));
            return valido;
        } catch (Exception e) {
            Log.e(TAG_AUTH, "Erro na autenticação por email", e);
            return false;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public boolean verificarEmailExistente(String email) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT id FROM " + TABLE_DOADORES + " WHERE email = ?",
                    new String[]{email}
            );
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
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
    public long inserirInstituicao(String nome, String descricao, String telefone, String necessidades) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nome", nome);
            values.put("descricao", descricao);
            values.put("telefone", telefone);
            values.put("necessidades", necessidades);

            long id = db.insert(TABLE_INSTITUICOES, null, values);

            if (id != -1) {
                Log.i(TAG_CRUD, "Instituição cadastrada | ID: " + id);
            } else {
                Log.w(TAG_CRUD, "Falha no cadastro | Instituição: " + nome);
            }
            return id;
        } catch (Exception e) {
            Log.e(TAG_CRUD, "Erro ao cadastrar instituição", e);
            return -1;
        } finally {
            if (db != null) db.close();
        }
    }
    public long inserirDoacao(int idInstituicao, String tipo, String descricao) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id_instituicao", idInstituicao);
            values.put("tipo", tipo);
            values.put("descricao", descricao);
            values.put("data", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

            long id = db.insert(TABLE_DOACOES, null, values);

            if (id != -1) {
                Log.i(TAG_CRUD, "Doação cadastrada | ID: " + id);
            } else {
                Log.w(TAG_CRUD, "Falha no cadastro | Tipo: " + tipo);
            }
            return id;
        } catch (Exception e) {
            Log.e(TAG_CRUD, "Erro ao cadastrar doação", e);
            return -1;
        } finally {
            if (db != null) db.close();
        }
    }
    public Cursor listarTodasNecessidades() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT d.id as _id, d.tipo, d.descricao, i.nome as instituicao, d.data " +
                "FROM " + TABLE_DOACOES + " d " +
                "INNER JOIN " + TABLE_INSTITUICOES + " i ON d.id_instituicao = i.id " +
                "ORDER BY d.data DESC";

        Log.d(TAG_DB, "Executando query: " + query);
        Cursor cursor = db.rawQuery(query, null);

        // Log para debug - verificar quantos registros retornaram
        Log.d(TAG_DB, "Número de registros encontrados: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Log.d(TAG_DB, "Registro: " +
                        "ID=" + cursor.getLong(0) +
                        ", Tipo=" + cursor.getString(1) +
                        ", Instituição=" + cursor.getString(3));
            } while (cursor.moveToNext());
            cursor.moveToFirst();
        }

        return cursor;
    }
    public String obterNomePorEmail(String email) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT nome FROM " + TABLE_DOADORES + " WHERE email = ?",
                    new String[]{email}
            );

            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            }
            return "";
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }
    public void verificarDados() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Verificar instituições
        Cursor cursorInst = db.rawQuery("SELECT * FROM " + TABLE_INSTITUICOES, null);
        Log.d("DB_VERIFY", "Instituições: " + cursorInst.getCount());
        while (cursorInst.moveToNext()) {
            Log.d("DB_VERIFY", "Instituição: " + cursorInst.getString(1));
        }
        cursorInst.close();

        // Verificar doações
        Cursor cursorDoacoes = db.rawQuery("SELECT * FROM " + TABLE_DOACOES, null);
        Log.d("DB_VERIFY", "Doações: " + cursorDoacoes.getCount());
        while (cursorDoacoes.moveToNext()) {
            Log.d("DB_VERIFY", "Doação: " + cursorDoacoes.getString(2) +
                    " - Instituição ID: " + cursorDoacoes.getInt(1));
        }
        cursorDoacoes.close();
    }
    public void inicializarDadosTeste() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Inserir instituição de teste se não existir
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_INSTITUICOES + " LIMIT 1", null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put("nome", "Instituição Teste");
            values.put("descricao", "Instituição para testes");
            values.put("telefone", "11999999999");
            values.put("necessidades", "Alimentos,Roupas");
            db.insert(TABLE_INSTITUICOES, null, values);
            Log.d("DB_TEST", "Instituição teste criada");
        }
        cursor.close();
    }
}