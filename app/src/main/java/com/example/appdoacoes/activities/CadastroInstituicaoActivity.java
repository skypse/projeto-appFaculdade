package com.example.appdoacoes.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoacoes.R;
import com.example.appdoacoes.data.DatabaseHelper;

public class CadastroInstituicaoActivity extends AppCompatActivity {

    private EditText editNome, editDescricao, editTelefone, editNecessidades;
    private Button btnCadastrar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_instituicao);

        editNome = findViewById(R.id.editTextNomeInstituicao);
        editDescricao = findViewById(R.id.editTextDescricao);
        editTelefone = findViewById(R.id.editTextTelefoneInstituicao);
        editNecessidades = findViewById(R.id.editTextNecessidades);
        btnCadastrar = findViewById(R.id.buttonSalvarInstituicao);

        dbHelper = new DatabaseHelper(this);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = editNome.getText().toString().trim();
                String descricao = editDescricao.getText().toString().trim();
                String telefone = editTelefone.getText().toString().trim();
                String necessidades = editNecessidades.getText().toString().trim();

                if (nome.isEmpty()) {
                    Toast.makeText(CadastroInstituicaoActivity.this, "Digite o nome da instituição", Toast.LENGTH_SHORT).show();
                    return;
                }

                long resultado = dbHelper.inserirInstituicao(nome, descricao, telefone, necessidades);

                if (resultado != -1) {
                    Toast.makeText(CadastroInstituicaoActivity.this, "Instituição cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CadastroInstituicaoActivity.this, "Erro ao cadastrar instituição", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}