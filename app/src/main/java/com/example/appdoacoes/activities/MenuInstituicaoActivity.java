package com.example.appdoacoes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoacoes.R;

public class MenuInstituicaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_instituicao);

        TextView txtBoasVindas = findViewById(R.id.textViewBoasVindas);
        Button btnVerNecessidades = findViewById(R.id.buttonVerNecessidades);
        Button btnPublicarNecessidade = findViewById(R.id.buttonPublicarNecessidade);

        String nome = getIntent().getStringExtra("nome_usuario");
        txtBoasVindas.setText("Bem-vindo, " + nome + "!");

        btnVerNecessidades.setOnClickListener(v -> {
            startActivity(new Intent(MenuInstituicaoActivity.this, ListaNecessidadesActivity.class));
        });

        btnPublicarNecessidade.setOnClickListener(v -> {
            startActivity(new Intent(MenuInstituicaoActivity.this, PublicarNecessidadeActivity.class));
        });
    }
}