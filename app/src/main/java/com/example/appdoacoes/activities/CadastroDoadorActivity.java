package com.example.appdoacoes.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoacoes.R;
import com.example.appdoacoes.data.DatabaseHelper;

public class CadastroDoadorActivity extends AppCompatActivity {

    private EditText editNome, editEmail, editTelefone, editSenha; // Adicionei o EditText para senha
    private Button btnCadastrar;
    private DatabaseHelper dbHelper;
    private static final String TAG = "CadastroDoadorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_doador);

        // referenciar os campos
        editNome = findViewById(R.id.editTextNome);
        editEmail = findViewById(R.id.editTextEmail);
        editTelefone = findViewById(R.id.editTextTelefone);
        editSenha = findViewById(R.id.editTextSenha);
        btnCadastrar = findViewById(R.id.buttonSalvar);

        dbHelper = new DatabaseHelper(this);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Botão salvar pressionado");

                String nome = editNome.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String telefone = editTelefone.getText().toString().trim();
                String senha = editSenha.getText().toString().trim();

                Log.d(TAG, "Dados coletados: Nome = " + nome + ", Email = " + email +
                        ", Telefone = " + telefone + ", Senha = [PROTEGIDO]");

                if (nome.isEmpty()) {
                    Toast.makeText(CadastroDoadorActivity.this, "Digite o nome", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Nome não preenchido");
                    return;
                }

                if (senha.isEmpty()) {
                    Toast.makeText(CadastroDoadorActivity.this, "Digite uma senha", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Senha não preenchida");
                    return;
                }

                long resultado = dbHelper.inserirDoador(nome, email, telefone, senha);

                if (resultado != -1) {
                    Toast.makeText(CadastroDoadorActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Cadastro inserido com ID: " + resultado);
                    finish();
                } else {
                    Toast.makeText(CadastroDoadorActivity.this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Erro ao cadastrar doador no banco");
                }
            }
        });
    }
}