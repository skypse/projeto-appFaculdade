package com.example.appdoacoes.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.appdoacoes.R;
import com.example.appdoacoes.data.DatabaseHelper;

public class ListaNecessidadesActivity extends AppCompatActivity {

    private static final String TAG = "ListaNecessidades";
    private ListView listViewNecessidades;
    private DatabaseHelper dbHelper;
    private String userType;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_necessidades);

        listViewNecessidades = findViewById(R.id.listViewNecessidades);
        dbHelper = new DatabaseHelper(this);

        userType = getIntent().getStringExtra("user_type");
        if (userType == null) {
            userType = "doador";
            Log.w(TAG, "Tipo de usuário não definido, usando padrão: doador");
        }

        setupListView();
    }

    private void setupListView() {
        try {
            Cursor cursor = dbHelper.listarTodasNecessidades();

            if (cursor == null || cursor.getCount() == 0) {
                showEmptyState();
                return;
            }

            String[] fromColumns = {
                    DatabaseHelper.COL_TIPO,
                    DatabaseHelper.COL_DESCRICAO,
                    "instituicao",
                    DatabaseHelper.COL_DATA
            };

            int[] toViews = {
                    R.id.textViewTipo,
                    R.id.textViewDescricao,
                    R.id.textViewInstituicao,
                    R.id.textViewData
            };

            adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.item_necessidade,
                    cursor,
                    fromColumns,
                    toViews,
                    0
            );

            listViewNecessidades.setAdapter(adapter);
            listViewNecessidades.setOnItemClickListener((parent, view, position, id) -> {
                if (userType.equals("doador")) {
                    Intent intent = new Intent(this, RealizarDoacaoActivity.class);
                    intent.putExtra("necessidade_id", (int)id);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Detalhes da necessidade #" + id, Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar necessidades", e);
            showDatabaseError();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_necessidades, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    Cursor filteredCursor = dbHelper.filtrarNecessidades(newText);
                    adapter.changeCursor(filteredCursor);
                }
                return true;
            }
        });

        menu.findItem(R.id.action_add).setVisible(userType.equals("instituicao"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(this, PublicarNecessidadeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showEmptyState() {
        Toast.makeText(this, "Nenhuma necessidade cadastrada", Toast.LENGTH_SHORT).show();
    }

    private void showDatabaseError() {
        Toast.makeText(this, "Erro ao acessar o banco de dados", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            Cursor newCursor = dbHelper.listarTodasNecessidades();
            adapter.changeCursor(newCursor);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null && adapter.getCursor() != null) {
            adapter.getCursor().close();
        }
        dbHelper.close();
    }
}