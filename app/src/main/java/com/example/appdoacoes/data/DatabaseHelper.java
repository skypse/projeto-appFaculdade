package com.example.appdoacoes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Constantes
    public static final String COL_TIPO = "tipo";
    public static final String COL_DESCRICAO = "descricao";
    public static final String COL_DATA = "data";

    // Tags para logs
    private static final String TAG_DB = "DB_INFRA";
    private static final String TAG_AUTH = "DB_AUTH";
    private static final String TAG_CRUD = "DB_CRUD";
    private static final String TAG_TEST = "DB_TEST";

    // Configurações do banco
    private static final String DATABASE_NAME = "AppDoacoes.db";
    private static final int DATABASE_VERSION = 17;

    // Tabelas e colunas
    public static final String TABLE_DOADORES = "doadores";
    public static final String TABLE_INSTITUICOES = "instituicoes";
    public static final String TABLE_DOACOES = "doacoes";
    public static final String TABLE_DOACOES_REGISTRADAS = "doacoes_registradas";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG_DB, "DatabaseHelper inicializado");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Tabela de doadores
            db.execSQL("CREATE TABLE " + TABLE_DOADORES + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL," +
                    "email TEXT UNIQUE," +
                    "telefone TEXT," +
                    "senha TEXT)");

            // Tabela de instituições
            db.execSQL("CREATE TABLE " + TABLE_INSTITUICOES + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL," +
                    "descricao TEXT," +
                    "telefone TEXT," +
                    "email TEXT UNIQUE," +
                    "senha TEXT," +
                    "necessidades TEXT)");

            // Tabela de necessidades (doações)
            db.execSQL("CREATE TABLE " + TABLE_DOACOES + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_instituicao INTEGER," +
                    "tipo TEXT," +
                    "descricao TEXT," +
                    "data TEXT," +
                    "status TEXT DEFAULT 'Ativa'," +
                    "FOREIGN KEY(id_instituicao) REFERENCES " + TABLE_INSTITUICOES + "(id))");

            // Tabela de registros de doações
            db.execSQL("CREATE TABLE " + TABLE_DOACOES_REGISTRADAS + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_doador INTEGER NOT NULL," +
                    "id_necessidade INTEGER NOT NULL," +
                    "quantidade INTEGER," +
                    "observacoes TEXT," +
                    "data TEXT NOT NULL," +
                    "status TEXT NOT NULL DEFAULT 'Pendente'," +
                    "FOREIGN KEY(id_doador) REFERENCES " + TABLE_DOADORES + "(id)," +
                    "FOREIGN KEY(id_necessidade) REFERENCES " + TABLE_DOACOES + "(id))");

            Log.i(TAG_DB, "Estrutura do banco criada com sucesso");
            inicializarDadosTeste(db);
        } catch (Exception e) {
            Log.e(TAG_DB, "Falha na criação do banco", e);
            throw e;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOACOES_REGISTRADAS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOACOES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTITUICOES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOADORES);
            onCreate(db);
            Log.w(TAG_DB, "Banco atualizado de v" + oldVersion + " para v" + newVersion);
        } catch (Exception e) {
            Log.e(TAG_DB, "Falha na atualização do banco", e);
        }
    }

    // inicializar dados de teste
    private void inicializarDadosTeste(SQLiteDatabase db) {
        try {
            // Verificar se já existe alguma instituição
            Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_INSTITUICOES + " LIMIT 1", null);
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put("nome", "Instituição Teste");
                values.put("descricao", "Instituição para testes");
                values.put("telefone", "11999999999");
                values.put("email", "instituicao@teste.com");
                values.put("senha", hashSenha("123456"));
                values.put("necessidades", "Alimentos,Roupas");

                long id = db.insert(TABLE_INSTITUICOES, null, values);
                Log.d(TAG_TEST, "Instituição teste criada com ID: " + id);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG_TEST, "Erro ao criar dados de teste", e);
        }
    }

    // Métodos para doadores
    public long inserirDoador(String nome, String email, String telefone, String senha) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("nome", nome);
            values.put("email", email);
            values.put("telefone", telefone);
            values.put("senha", hashSenha(senha));

            long id = db.insert(TABLE_DOADORES, null, values);

            if (id != -1) {
                Log.i(TAG_CRUD, "Doador cadastrado | ID: " + id);
            } else {
                Log.w(TAG_CRUD, "Falha no cadastro de doador");
            }
            return id;
        } catch (Exception e) {
            Log.e(TAG_CRUD, "Erro ao cadastrar doador", e);
            return -1;
        } finally {
            db.close();
        }
    }

    // Métodos para instituições
    public long inserirInstituicao(String nome, String descricao, String telefone, String email, String senha) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("nome", nome);
            values.put("descricao", descricao);
            values.put("telefone", telefone);
            values.put("email", email);
            values.put("senha", hashSenha(senha));

            long id = db.insert(TABLE_INSTITUICOES, null, values);

            if (id != -1) {
                Log.i(TAG_CRUD, "Instituição cadastrada | ID: " + id);
            } else {
                Log.w(TAG_CRUD, "Falha no cadastro de instituição");
            }
            return id;
        } catch (Exception e) {
            Log.e(TAG_CRUD, "Erro ao cadastrar instituição", e);
            return -1;
        } finally {
            db.close();
        }
    }

    // Métodos para necessidades/doações
    public long inserirDoacao(int idInstituicao, String tipo, String descricao) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("id_instituicao", idInstituicao);
            values.put("tipo", tipo);
            values.put("descricao", descricao);
            values.put("data", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

            long id = db.insert(TABLE_DOACOES, null, values);

            if (id != -1) {
                Log.i(TAG_CRUD, "Necessidade cadastrada | ID: " + id);
            } else {
                Log.w(TAG_CRUD, "Falha no cadastro de necessidade");
            }
            return id;
        } catch (Exception e) {
            Log.e(TAG_CRUD, "Erro ao cadastrar necessidade", e);
            return -1;
        } finally {
            db.close();
        }
    }

    public Cursor listarTodasNecessidades() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT d.id as _id, d.tipo, d.descricao, i.nome as instituicao, d.data " +
                "FROM " + TABLE_DOACOES + " d " +
                "INNER JOIN " + TABLE_INSTITUICOES + " i ON d.id_instituicao = i.id " +
                "WHERE d.status = 'Ativa' " +
                "ORDER BY d.data DESC";

        Log.d(TAG_DB, "Executando query: " + query);
        Cursor cursor = db.rawQuery(query, null);
        Log.d(TAG_DB, "Necessidades encontradas: " + cursor.getCount());

        return cursor;
    }

    // Métodos de autenticação
    public boolean verificarEmailExistente(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id FROM " + TABLE_DOADORES + " WHERE email = ?",
                new String[]{email}
        );
        boolean existe = cursor.getCount() > 0;
        cursor.close();

        if (!existe) {
            cursor = db.rawQuery(
                    "SELECT id FROM " + TABLE_INSTITUICOES + " WHERE email = ?",
                    new String[]{email}
            );
            existe = cursor.getCount() > 0;
            cursor.close();
        }

        Log.d(TAG_AUTH, "Verificação de email: " + email + " - " + (existe ? "Existente" : "Não encontrado"));
        return existe;
    }

    public String verificarTipoUsuario(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id FROM " + TABLE_DOADORES + " WHERE email = ?",
                new String[]{email}
        );

        if (cursor.getCount() > 0) {
            cursor.close();
            Log.d(TAG_AUTH, "Tipo de usuário: Doador");
            return "doador";
        }
        cursor.close();

        cursor = db.rawQuery(
                "SELECT id FROM " + TABLE_INSTITUICOES + " WHERE email = ?",
                new String[]{email}
        );

        if (cursor.getCount() > 0) {
            cursor.close();
            Log.d(TAG_AUTH, "Tipo de usuário: Instituição");
            return "instituicao";
        }
        cursor.close();

        Log.d(TAG_AUTH, "Tipo de usuário não encontrado");
        return "";
    }

    public boolean verificarCredenciais(String email, String senha, String tipoUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        String tableName = tipoUsuario.equals("doador") ? TABLE_DOADORES : TABLE_INSTITUICOES;

        Cursor cursor = db.rawQuery(
                "SELECT id FROM " + tableName +
                        " WHERE email = ? AND senha = ?",
                new String[]{email, hashSenha(senha)}
        );

        boolean valido = cursor.getCount() > 0;
        cursor.close();

        Log.d(TAG_AUTH, "Verificação de credenciais: " +
                (valido ? "Válidas" : "Inválidas") + " para " + email);
        return valido;
    }

    public String obterNomePorEmail(String email, String tipoUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        String tableName = tipoUsuario.equals("doador") ? TABLE_DOADORES : TABLE_INSTITUICOES;

        Cursor cursor = db.rawQuery(
                "SELECT nome FROM " + tableName + " WHERE email = ?",
                new String[]{email}
        );

        if (cursor.moveToFirst()) {
            String nome = cursor.getString(0);
            cursor.close();
            Log.d(TAG_AUTH, "Nome obtido para " + email + ": " + nome);
            return nome;
        }
        cursor.close();
        return "";
    }

    // Métodos para registro de doações
    public long registrarDoacao(int doadorId, int necessidadeId, int quantidade, String observacoes) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("id_doador", doadorId);
            values.put("id_necessidade", necessidadeId);
            values.put("quantidade", quantidade);
            values.put("observacoes", observacoes);
            values.put("data", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

            return db.insert(TABLE_DOACOES_REGISTRADAS, null, values);
        } finally {
            db.close();
        }
    }

    public Cursor obterNecessidadePorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT d.id as _id, d.tipo, d.descricao, i.nome as instituicao, d.data " +
                "FROM " + TABLE_DOACOES + " d " +
                "INNER JOIN " + TABLE_INSTITUICOES + " i ON d.id_instituicao = i.id " +
                "WHERE d.id = ? AND d.status = 'Ativa'";

        Log.d(TAG_DB, "Obtendo necessidade por ID: " + id);
        return db.rawQuery(query, new String[]{String.valueOf(id)});
    }

    // Métodos auxiliares
    private String hashSenha(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(senha.getBytes("UTF-8"));
            return Base64.encodeToString(hash, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG_AUTH, "Falha ao gerar hash da senha", e);
            throw new RuntimeException("Erro de criptografia");
        }
    }

    public void verificarDados() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] tabelas = {TABLE_DOADORES, TABLE_INSTITUICOES, TABLE_DOACOES, TABLE_DOACOES_REGISTRADAS};

        for (String tabela : tabelas) {
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tabela, null);
            if (cursor.moveToFirst()) {
                Log.d(TAG_DB, "Registros em " + tabela + ": " + cursor.getInt(0));
            }
            cursor.close();
        }
    }
    public Cursor filtrarNecessidades(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT d.id as _id, d.tipo, d.descricao, i.nome as instituicao, d.data " +
                "FROM " + TABLE_DOACOES + " d " +
                "INNER JOIN " + TABLE_INSTITUICOES + " i ON d.id_instituicao = i.id " +
                "WHERE (d.tipo LIKE ? OR d.descricao LIKE ? OR i.nome LIKE ?) AND d.status = 'Ativa' " +
                "ORDER BY d.data DESC";
        return db.rawQuery(sql, new String[]{"%" + query + "%", "%" + query + "%", "%" + query + "%"});
    }
    public void atualizarStatusNecessidade(int necessidadeId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("status", status);
            db.update(TABLE_DOACOES, values, "id = ?", new String[]{String.valueOf(necessidadeId)});
        } finally {
            db.close();
        }
    }
    public Cursor getDonationsHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT dr.id, d.nome as doador_nome, dn.tipo, dr.quantidade, dr.data " +
                "FROM " + TABLE_DOACOES_REGISTRADAS + " dr " +
                "INNER JOIN " + TABLE_DOADORES + " d ON dr.id_doador = d.id " +
                "INNER JOIN " + TABLE_DOACOES + " dn ON dr.id_necessidade = dn.id " +
                "ORDER BY dr.data DESC";
        return db.rawQuery(query, null);
    }
}