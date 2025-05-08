package com.example.appdoacoes.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoacoes.R;
import com.example.appdoacoes.data.DatabaseHelper;

import java.util.Arrays;

public class ListaNecessidadesActivity extends AppCompatActivity {

    private ListView listViewNecessidades;
    private Button btnAdicionarNecessidade;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_necessidades);

        listViewNecessidades = findViewById(R.id.listViewNecessidades);
        btnAdicionarNecessidade = findViewById(R.id.buttonAdicionarNecessidade);
        dbHelper = new DatabaseHelper(this);
        dbHelper.verificarDados();

        carregarNecessidades();

        // configurando clique nos itens da lista
        listViewNecessidades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // implementar ação ao clicar em uma necessidade
                Toast.makeText(ListaNecessidadesActivity.this,
                        "Necessidade selecionada: " + id, Toast.LENGTH_SHORT).show();

                // pode abrir uma tela de detalhes ou iniciar processo de doação
            }
        });

        btnAdicionarNecessidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListaNecessidadesActivity.this,
                        PublicarNecessidadeActivity.class));
            }
        });
    }

    private void carregarNecessidades() {
        try {
            Cursor cursor = dbHelper.listarTodasNecessidades();

            if (cursor == null) {
                Toast.makeText(this, "Erro ao acessar o banco de dados", Toast.LENGTH_SHORT).show();
                return;
            }

            if (cursor.getCount() == 0) {
                Toast.makeText(this, "Nenhuma necessidade cadastrada ainda.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar colunas disponíveis (para debug)
            String[] columnNames = cursor.getColumnNames();
            Log.d("DB_DEBUG", "Colunas disponíveis: " + Arrays.toString(columnNames));

            // Configurar o adapter
            String[] fromColumns = {"tipo", "descricao", "instituicao"};
            int[] toViews = {R.id.textViewTipo, R.id.textViewDescricao, R.id.textViewInstituicao};

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.item_necessidade,
                    cursor,
                    fromColumns,
                    toViews,
                    0
            );

            listViewNecessidades.setAdapter(adapter);

            // log para verificar se tem dados
            Log.d("DB_DEBUG", "Adapter count: " + adapter.getCount());

        } catch (Exception e) {
            Toast.makeText(this, "Erro ao carregar necessidades: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("DB_ERROR", "Erro ao carregar necessidades", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarNecessidades();
    }
}