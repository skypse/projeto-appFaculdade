package com.example.appdoacoes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoacoes.R;
import com.example.appdoacoes.data.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText editEmailLogin, editSenha;
    private Button btnEntrar, btnCadastrar;
    private DatabaseHelper dbHelper;
    private String tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmailLogin = findViewById(R.id.editTextEmailLogin);
        editSenha = findViewById(R.id.editTextSenha);
        btnEntrar = findViewById(R.id.buttonEntrarLogin);
        btnCadastrar = findViewById(R.id.buttonCadastrarLogin);
        dbHelper = new DatabaseHelper(this);

        tipoUsuario = getIntent().getStringExtra("tipoUsuario");
        Log.d(TAG, "Tipo de usuário para login: " + tipoUsuario);

        // Atualiza o texto do botão de cadastro conforme o tipo de usuário
        btnCadastrar.setText(tipoUsuario.equals("doador") ? "Cadastrar Doador" : "Cadastrar Instituição");

        btnEntrar.setOnClickListener(v -> {
            String email = editEmailLogin.getText().toString().trim();
            String senha = editSenha.getText().toString().trim();

            // Validação dos campos
            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verifica o tipo de usuário primeiro
            String tipo = dbHelper.verificarTipoUsuario(email);
            Log.d(TAG, "Tipo retornado do banco: " + tipo);

            if (tipo.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verifica se o tipo de login corresponde ao tipo de usuário
            if (!tipo.equals(tipoUsuario)) {
                Toast.makeText(LoginActivity.this,
                        tipoUsuario.equals("doador") ?
                                "Este email é de uma instituição" :
                                "Este email é de um doador",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Verifica as credenciais
            if (dbHelper.verificarCredenciais(email, senha, tipo)) {
                String nome = dbHelper.obterNomePorEmail(email, tipo);
                Log.d(TAG, "Login bem-sucedido para: " + nome);

                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                intent.putExtra("nome_usuario", nome);
                intent.putExtra("user_type", tipo);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Email ou senha inválidos", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Tentativa de login falhou para: " + email);
            }
        });

        btnCadastrar.setOnClickListener(v -> {
            Log.d(TAG, "Iniciando cadastro para: " + tipoUsuario);
            if (tipoUsuario.equals("doador")) {
                startActivity(new Intent(LoginActivity.this, CadastroDoadorActivity.class));
            } else {
                startActivity(new Intent(LoginActivity.this, CadastroInstituicaoActivity.class));
            }
        });
    }
}