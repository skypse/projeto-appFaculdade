package com.example.appdoacoes.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoacoes.R;
import com.example.appdoacoes.data.DatabaseHelper;

public class PublicarNecessidadeActivity extends AppCompatActivity {

    private EditText editTipo, editDescricao;
    private Button btnPublicar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_necessidade);

        editTipo = findViewById(R.id.editTextTipo);
        editDescricao = findViewById(R.id.editTextDescricaoNecessidade);
        btnPublicar = findViewById(R.id.buttonPublicar);

        dbHelper = new DatabaseHelper(this);

        // Em PublicarNecessidadeActivity.java, verifique o onClick do botão
        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipo = editTipo.getText().toString().trim();
                String descricao = editDescricao.getText().toString().trim();

                if (tipo.isEmpty()) {
                    Toast.makeText(PublicarNecessidadeActivity.this,
                            "Informe o tipo de necessidade", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar se há instituições cadastradas
                Cursor cursorInstituicoes = dbHelper.getReadableDatabase().rawQuery(
                        "SELECT id FROM " + DatabaseHelper.TABLE_INSTITUICOES, null);

                if (cursorInstituicoes.getCount() == 0) {
                    Toast.makeText(PublicarNecessidadeActivity.this,
                            "Nenhuma instituição cadastrada. Cadastre uma instituição primeiro.",
                            Toast.LENGTH_LONG).show();
                    cursorInstituicoes.close();
                    return;
                }
                cursorInstituicoes.close();

                // Usar a primeira instituição disponível (em uma app real, usaria a logada)
                int idInstituicao = 1; // Substituir por lógica de sessão posteriormente

                long resultado = dbHelper.inserirDoacao(idInstituicao, tipo, descricao);

                if (resultado != -1) {
                    Toast.makeText(PublicarNecessidadeActivity.this,
                            "Necessidade publicada com sucesso! ID: " + resultado,
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(PublicarNecessidadeActivity.this,
                            "Erro ao publicar necessidade", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}