package com.example.appdoacoes.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.appdoacoes.R;
import com.example.appdoacoes.fragments.HistoryFragment;
import com.example.appdoacoes.fragments.HomeFragment;
import com.example.appdoacoes.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "MenuActivity";
    private String userType;
    private String userName;
    private BottomNavigationView bottomNav;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Obter tipo de usuário e nome do Intent
        if (getIntent() != null && getIntent().getExtras() != null) {
            userType = getIntent().getStringExtra("user_type");
            userName = getIntent().getStringExtra("nome_usuario");

            if (userType == null) {
                userType = "doador"; // Valor padrão caso seja nulo
                Log.w(TAG, "Tipo de usuário nulo, usando padrão: doador");
            }
        } else {
            finish(); // Encerra se não tiver os dados necessários
            return;
        }

        Log.d(TAG, "Tipo de usuário: " + userType);
        Log.d(TAG, "Nome do usuário: " + userName);

        // Configuração da navegação
        bottomNav = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();

        // Carrega o fragmento inicial
        if (savedInstanceState == null) {
            loadFragment(HomeFragment.newInstance(userType, userName), false);
        }
    }

    private void setupBottomNavigation() {
        try {
            Menu menu = bottomNav.getMenu();
            MenuItem historyItem = menu.findItem(R.id.nav_history);

            // Mostra o histórico apenas para instituições
            if (historyItem != null) {
                historyItem.setVisible(userType != null && userType.equals("instituicao"));
            }

            bottomNav.setOnNavigationItemSelectedListener(item -> {
                try {
                    if (item.getItemId() == R.id.nav_home) {
                        loadFragment(HomeFragment.newInstance(userType, userName), true);
                        return true;
                    } else if (item.getItemId() == R.id.nav_history) {
                        if (userType.equals("instituicao")) {
                            loadFragment(HistoryFragment.newInstance(), true);
                            return true;
                        } else {
                            Toast.makeText(this, "Acesso restrito a instituições", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    } else if (item.getItemId() == R.id.nav_profile) {
                        loadFragment(ProfileFragment.newInstance(userType, userName), true);
                        return true;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Erro na navegação: " + e.getMessage());
                    Toast.makeText(this, "Erro ao carregar a tela", Toast.LENGTH_SHORT).show();
                }
                return false;
            });
        } catch (Exception e) {
            Log.e(TAG, "Erro ao configurar navegação: " + e.getMessage());
        }
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment);

            if (addToBackStack) {
                transaction.addToBackStack(null);
            }

            transaction.commit();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar fragmento: " + e.getMessage());
            Toast.makeText(this, "Erro ao carregar conteúdo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualiza a visibilidade do item de histórico
        if (bottomNav != null) {
            Menu menu = bottomNav.getMenu();
            MenuItem historyItem = menu.findItem(R.id.nav_history);
            if (historyItem != null) {
                historyItem.setVisible(userType.equals("instituicao"));
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Configuração para duplo clique para sair
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel();
                super.onBackPressed();
                return;
            } else {
                backToast = Toast.makeText(this, "Pressione voltar novamente para sair", Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedTime = System.currentTimeMillis();
        }
    }
}