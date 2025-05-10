package com.example.appdoacoes.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdoacoes.R;
import com.example.appdoacoes.adapters.DonationHistoryAdapter;
import com.example.appdoacoes.data.DatabaseHelper;

public class HistoryFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private DonationHistoryAdapter adapter;

    // Método newInstance() necessário
    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        dbHelper = new DatabaseHelper(getActivity());

        // Configuração do RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadDonationHistory();

        return view;
    }

    private void loadDonationHistory() {
        try {
            Cursor cursor = dbHelper.getDonationsHistory();

            if (cursor != null && cursor.getCount() > 0) {
                adapter = new DonationHistoryAdapter(getContext(), cursor);
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getContext(), "Nenhuma doação registrada", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao carregar histórico", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbHelper != null) {
            dbHelper.close();
        }
        if (adapter != null) {
            adapter.changeCursor(null);
        }
    }
}