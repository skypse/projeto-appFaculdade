package com.example.appdoacoes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoacoes.activities.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonDoador = findViewById(R.id.buttonDoador);
        Button buttonInstituicao = findViewById(R.id.buttonInstituicao);

        buttonDoador.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("tipoUsuario", "doador");
            startActivity(intent);
        });

        buttonInstituicao.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("tipoUsuario", "instituicao");
            startActivity(intent);
        });
    }
}