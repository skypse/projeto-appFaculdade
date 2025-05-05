package com.example.appdoacoes;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appdoacoes.data.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instancia o banco de dados
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Log.d("MainActivity", "Instância do banco criada.");
    }
}
