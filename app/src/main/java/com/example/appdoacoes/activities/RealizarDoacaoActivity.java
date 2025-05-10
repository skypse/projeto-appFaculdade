package com.example.appdoacoes.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoacoes.R;
import com.example.appdoacoes.data.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RealizarDoacaoActivity extends AppCompatActivity {

    private static final String TAG = "RealizarDoacao";
    private DatabaseHelper dbHelper;
    private int necessidadeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_doacao);

        dbHelper = new DatabaseHelper(this);
        necessidadeId = getIntent().getIntExtra("necessidade_id", -1);

        if (necessidadeId == -1) {
            Toast.makeText(this, "Necessidade inválida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupViews();
    }

    @SuppressLint("Range")
    private void setupViews() {
        TextView textTipo = findViewById(R.id.textViewTipoNecessidade);
        TextView textDescricao = findViewById(R.id.textViewDescricaoNecessidade);
        TextView textInstituicao = findViewById(R.id.textViewInstituicao);
        EditText editQuantidade = findViewById(R.id.editTextQuantidade);
        EditText editObservacoes = findViewById(R.id.editTextObservacoes);
        Button btnConfirmar = findViewById(R.id.buttonConfirmarDoacao);

        Cursor cursor = dbHelper.obterNecessidadePorId(necessidadeId);
        if (cursor != null && cursor.moveToFirst()) {
            textTipo.setText("Tipo: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TIPO)));
            textDescricao.setText("Descrição: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DESCRICAO)));
            textInstituicao.setText("Instituição: " + cursor.getString(cursor.getColumnIndex("instituicao")));
            cursor.close();
        } else {
            Toast.makeText(this, "Erro ao carregar necessidade", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnConfirmar.setOnClickListener(v -> {
            String quantidadeStr = editQuantidade.getText().toString().trim();
            String observacoes = editObservacoes.getText().toString().trim();

            if (quantidadeStr.isEmpty()) {
                Toast.makeText(this, "Informe a quantidade", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int quantidade = Integer.parseInt(quantidadeStr);
                String data = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                // ID do doador fixo para exemplo (substituir pelo ID real do usuário logado)
                int doadorId = 1;

                long resultado = dbHelper.registrarDoacao(
                        doadorId,
                        necessidadeId,
                        quantidade,
                        observacoes
                );

                if (resultado != -1) {
                    Toast.makeText(this, "Doação registrada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao registrar doação", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Quantidade inválida", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Erro ao converter quantidade", e);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}