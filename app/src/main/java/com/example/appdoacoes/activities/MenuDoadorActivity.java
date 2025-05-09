package com.example.appdoacoes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoacoes.R;

public class MenuDoadorActivity extends AppCompatActivity {

    // No MenuActivity.java, atualize o onCreate:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView txtBoasVindas = findViewById(R.id.textViewBoasVindas);
        Button btnVerNecessidades = findViewById(R.id.buttonVerNecessidades);
        Button btnPublicarNecessidade = findViewById(R.id.buttonPublicarNecessidade);

        String nome = getIntent().getStringExtra("nome_doador");
        txtBoasVindas.setText("Bem-vindo, " + nome + "!");

        btnVerNecessidades.setOnClickListener(v -> {
            startActivity(new Intent(MenuDoadorActivity.this, ListaNecessidadesActivity.class));
        });

        btnPublicarNecessidade.setOnClickListener(v -> {
            startActivity(new Intent(MenuDoadorActivity.this, PublicarNecessidadeActivity.class));
        });
    }
}
