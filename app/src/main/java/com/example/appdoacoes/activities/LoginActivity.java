package com.example.appdoacoes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoacoes.R;
import com.example.appdoacoes.data.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText editNomeLogin, editSenha;
    private Button btnEntrar, btnCadastrar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editNomeLogin = findViewById(R.id.editTextNomeLogin);
        editSenha = findViewById(R.id.editTextSenha);
        btnEntrar = findViewById(R.id.buttonEntrarLogin);
        btnCadastrar = findViewById(R.id.buttonCadastrarLogin);
        dbHelper = new DatabaseHelper(this);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = editNomeLogin.getText().toString().trim();
                String senha = editSenha.getText().toString().trim();

                if (nome.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHelper.verificarLogin(nome, senha)) {
                    Toast.makeText(LoginActivity.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    intent.putExtra("nome_doador", nome);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CadastroDoadorActivity.class));
            }
        });
    }
}