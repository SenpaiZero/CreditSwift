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

import com.example.Helper.ProfileHelper;
import com.example.Helper.userInterfaceHelper;
import com.example.model.applyModel;
import com.example.taskperformance.R;

import java.util.LinkedList;

public class applyAdapter extends RecyclerView.Adapter<applyAdapter.applyAdapterHolder> {


    LinkedList<applyModel> applyList;
    ProfileHelper profileHelper;
    userInterfaceHelper UIHelper;
    Context context;

    public applyAdapter(LinkedList<applyModel> applyList, ProfileHelper profileHelper, userInterfaceHelper UIHelper, Context context, String lenderName) {
        this.applyList = applyList;
        this.profileHelper = profileHelper;
        this.UIHelper = UIHelper;
        this.context = context;
        this.lenderName = lenderName;
    }

    String lenderName;
    @NonNull
    @Override
    public applyAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.apply_view, parent, false);

        return new applyAdapter.applyAdapterHolder(view);
    }

    @Override
    @SuppressLint("RecyclerView")
    public void onBindViewHolder(@NonNull applyAdapterHolder holder,  int position) {
        holder.fullName.setText(applyList.get(position).getName().replaceAll("\\|", " "));
        holder.amount.setText("Amount: " + String.valueOf(applyList.get(position).getAmount()));
        holder.profile.setImageBitmap(applyList.get(position).getImg());
        holder.email.setText(applyList.get(position).getEmail());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileHelper.acceptDeclineCurrentLend(lenderName,
                        applyList.get(position).getName(), true);
            }
        });

        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileHelper.acceptDeclineCurrentLend(lenderName,
                        applyList.get(position).getName(), false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return applyList != null ? applyList.size() : 0;
    }

    public static class applyAdapterHolder extends RecyclerView.ViewHolder {

        TextView fullName, email, amount;
        Button accept, decline;
        ImageView profile;
        public applyAdapterHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.nameTxt);
            email = itemView.findViewById(R.id.emailViewTxt);
            amount = itemView.findViewById(R.id.amountViewTxt);

            accept = itemView.findViewById(R.id.applyAcceptBtn);
            decline = itemView.findViewById(R.id.applyDeclineBtn);

            profile = itemView.findViewById(R.id.profileView);
        }
    }
}
