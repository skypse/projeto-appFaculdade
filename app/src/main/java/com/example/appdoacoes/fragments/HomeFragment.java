package com.example.appdoacoes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.appdoacoes.R;
import com.example.appdoacoes.activities.ListaNecessidadesActivity;
import com.example.appdoacoes.activities.PublicarNecessidadeActivity;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private String userType;
    private String userName;

    public HomeFragment() {
        // Construtor vazio obrigatório
    }

    public static HomeFragment newInstance(String userType, String userName) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("userType", userType);
        args.putString("userName", userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userType = getArguments().getString("userType");
            userName = getArguments().getString("userName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Log.d(TAG, "Criando HomeFragment para: " + userType);

        Button btnVerNecessidades = view.findViewById(R.id.buttonVerNecessidades);
        Button btnPublicarNecessidade = view.findViewById(R.id.buttonPublicarNecessidade);

        // Configurar visibilidade dos botões conforme tipo de usuário
        if (userType.equals("doador")) {
            btnPublicarNecessidade.setVisibility(View.GONE);
            Log.d(TAG, "Ocultando botão Publicar Necessidade para doador");
        } else {
            btnPublicarNecessidade.setVisibility(View.VISIBLE);
            btnPublicarNecessidade.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), PublicarNecessidadeActivity.class));
            });
        }

        btnVerNecessidades.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListaNecessidadesActivity.class);
            intent.putExtra("user_type", userType);
            startActivity(intent);
        });

        return view;
    }
}