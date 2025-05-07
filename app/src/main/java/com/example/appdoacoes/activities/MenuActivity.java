package com.example.appdoacoes.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoacoes.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView txtBoasVindas = findViewById(R.id.textViewBoasVindas);
        String nome = getIntent().getStringExtra("nome_doador");
        txtBoasVindas.setText("Bem-vindo, " + nome + "!");
    }
}
