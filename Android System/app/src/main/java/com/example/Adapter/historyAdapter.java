package com.example.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.historyModel;
import com.example.taskperformance.R;

import java.util.LinkedList;

public class historyAdapter extends RecyclerView.Adapter<historyAdapter.historyAdapterHolder> {

    Context context;
    LinkedList<historyModel> list = new LinkedList<>();
    public historyAdapter(Context context, LinkedList<historyModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public historyAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_view, parent, false);
        return new historyAdapter.historyAdapterHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull historyAdapterHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder.total.setText("TOTAL: " + list.get(position).getTotal());
        holder.year.setText("YEAR: " + String.format("%.0f", list.get(position).getYear()));
        holder.pic.setImageBitmap(list.get(position).getPic());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class historyAdapterHolder extends RecyclerView.ViewHolder {
        TextView name, total, year;
        ImageView pic;
        public historyAdapterHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.historyName);
            total = itemView.findViewById(R.id.historyTotal);
            year = itemView.findViewById(R.id.historyYear);
            pic = itemView.findViewById(R.id.profileView);
        }
    }
}
