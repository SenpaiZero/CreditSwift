package com.example.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.usersModel;
import com.example.taskperformance.R;

import java.util.LinkedList;

public class usersAdapter extends RecyclerView.Adapter<usersAdapter.usersHolder> {

    LinkedList<usersModel> userList;
    Context context;

    public usersAdapter(LinkedList<usersModel> usersList, Context context)
    {
        this.userList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public usersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_view, parent, false);

        return new usersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull usersHolder holder, int position) {
        holder.nameTxt.setText(userList.get(position).getName());
        holder.typeTxt.setText(userList.get(position).getType());
        holder.profile.setImageBitmap(userList.get(position).getPicture());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class usersHolder extends RecyclerView.ViewHolder {

        TextView nameTxt, typeTxt;
        ImageView profile;
        public usersHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.userNameTxt);
            typeTxt = itemView.findViewById(R.id.userTypeTxt);
            profile = itemView.findViewById(R.id.profileView);
        }
    }
}
