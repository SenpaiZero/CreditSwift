package com.example.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.userLenderModel;
import com.example.model.usersModel;
import com.example.taskperformance.R;

import java.util.LinkedList;

public class userLenderAdapter extends RecyclerView.Adapter<userLenderAdapter.userLenderHolder> {

    LinkedList<userLenderModel> lenderList;
    Context context;
    boolean isUser;
    public userLenderAdapter(LinkedList<userLenderModel> lenderList, Context context, boolean isUser) {
        this.lenderList = lenderList;
        this.context = context;
        this.isUser = isUser;
    }


    @NonNull
    @Override
    public userLenderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_lenders_view, parent, false);

        return new userLenderAdapter.userLenderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userLenderHolder holder, int position) {
        holder.companyName.setText(lenderList.get(position).getCompanyName());
        holder.priceRange.setText("₱"+lenderList.get(position).getMinPrice() + "—" + "₱" +lenderList.get(position).getMaxPrice());
        holder.interest.setText(lenderList.get(position).getInterest() + "%  |  " + lenderList.get(position).getFrequency());
        holder.profile.setImageBitmap(lenderList.get(position).getPic());
        holder.email.setText(lenderList.get(position).getEmail());
        // This is for admin
        if(!isUser)
        {
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        // This is for user
        if(isUser)
        {
            holder.edit.setVisibility(View.INVISIBLE);
            holder.remove.setText("Apply");
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return lenderList != null ? lenderList.size() : 0;
    }

    public static class userLenderHolder extends RecyclerView.ViewHolder {

        TextView companyName, priceRange, interest, email;
        Button edit, remove;
        ImageView profile;
        public userLenderHolder(@NonNull View itemView) {
            super(itemView);

            companyName = itemView.findViewById(R.id.companyTxt);
            priceRange = itemView.findViewById(R.id.rangeTxt);
            interest = itemView.findViewById(R.id.interestTxt);
            profile = itemView.findViewById(R.id.profileView);
            email = itemView.findViewById(R.id.emailViewTxt);

            edit = itemView.findViewById(R.id.adminEditLenderBtn);
            remove = itemView.findViewById(R.id.adminRemoveLenderBtn);
        }
    }
}
