package com.example.appdoacoes.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdoacoes.R;

public class DonationHistoryAdapter extends RecyclerView.Adapter<DonationHistoryAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;

    public DonationHistoryAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_donation_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        holder.textDonorName.setText(cursor.getString(cursor.getColumnIndex("doador_nome")));
        holder.textDonationType.setText(cursor.getString(cursor.getColumnIndex("tipo")));
        holder.textQuantity.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("quantidade"))));
        holder.textDate.setText(cursor.getString(cursor.getColumnIndex("data")));
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public void changeCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDonorName, textDonationType, textQuantity, textDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDonorName = itemView.findViewById(R.id.textViewDonorName);
            textDonationType = itemView.findViewById(R.id.textViewDonationType);
            textQuantity = itemView.findViewById(R.id.textViewQuantity);
            textDate = itemView.findViewById(R.id.textViewDate);
        }
    }
}