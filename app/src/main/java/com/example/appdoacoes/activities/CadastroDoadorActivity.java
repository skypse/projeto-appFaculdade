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
                String nome = editNome.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String telefone = editTelefone.getText().toString().trim();
                String senha = editSenha.getText().toString().trim();

                if (nome.isEmpty()) {
                    Toast.makeText(CadastroDoadorActivity.this,
                            "Digite o nome", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(CadastroDoadorActivity.this,
                            "Digite um email válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (senha.isEmpty() || senha.length() < 6) {
                    Toast.makeText(CadastroDoadorActivity.this,
                            "Senha deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHelper.verificarEmailExistente(email)) {
                    Toast.makeText(CadastroDoadorActivity.this,
                            "Email já cadastrado", Toast.LENGTH_SHORT).show();
                    return;
                }

                long resultado = dbHelper.inserirDoador(nome, email, telefone, senha);

                if (resultado != -1) {
                    Toast.makeText(CadastroDoadorActivity.this,
                            "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CadastroDoadorActivity.this,
                            "Erro ao cadastrar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}