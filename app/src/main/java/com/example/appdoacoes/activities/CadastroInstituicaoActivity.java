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

    private EditText editNomeDono, editEmailDono, editNomeInstituicao,
            editDescricao, editTelefone, editSenha;
    private Button btnCadastrar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_instituicao);

        editNomeDono = findViewById(R.id.editTextNomeDono);
        editEmailDono = findViewById(R.id.editTextEmailDono);
        editNomeInstituicao = findViewById(R.id.editTextNomeInstituicao);
        editDescricao = findViewById(R.id.editTextDescricao);
        editTelefone = findViewById(R.id.editTextTelefoneInstituicao);
        editSenha = findViewById(R.id.editTextSenhaInstituicao); // Adicione este campo no seu XML
        btnCadastrar = findViewById(R.id.buttonSalvarInstituicao);

        dbHelper = new DatabaseHelper(this);

        btnCadastrar.setOnClickListener(v -> {
            String nomeDono = editNomeDono.getText().toString().trim();
            String emailDono = editEmailDono.getText().toString().trim();
            String nomeInstituicao = editNomeInstituicao.getText().toString().trim();
            String descricao = editDescricao.getText().toString().trim();
            String telefone = editTelefone.getText().toString().trim();
            String senha = editSenha.getText().toString().trim();

            if (nomeDono.isEmpty() || nomeInstituicao.isEmpty() || telefone.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show();
                return;
            }

            long resultado = dbHelper.inserirInstituicao(nomeInstituicao, descricao, telefone, emailDono, senha);

            if (resultado != -1) {
                Toast.makeText(this, "Instituição cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao cadastrar instituição", Toast.LENGTH_SHORT).show();
            }
        });
    }
}