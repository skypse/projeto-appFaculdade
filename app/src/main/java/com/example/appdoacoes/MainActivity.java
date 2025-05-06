package com.example.appdoacoes;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoacoes.activities.CadastroDoadorActivity;
import com.example.appdoacoes.data.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instancia o banco de dados
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Log.d("MainActivity", "Instância do banco criada.");

        // encontrando o botão "Entrar"
        Button buttonEntrar = findViewById(R.id.buttonEntrar);

        // evento de clique para navegar para a CadastroDoadorActivity
        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent para navegar para a CadastroDoadorActivity
                Intent intent = new Intent(MainActivity.this, CadastroDoadorActivity.class);
                startActivity(intent);
            }
        });
    }
}
