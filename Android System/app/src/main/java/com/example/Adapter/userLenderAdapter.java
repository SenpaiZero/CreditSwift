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

import com.example.Helper.ProfileHelper;
import com.example.Helper.userInterfaceHelper;
import com.example.model.userLenderModel;
import com.example.model.usersModel;
import com.example.taskperformance.R;
import com.example.taskperformance.adminHome;
import com.example.taskperformance.userHome;

import java.util.LinkedList;

public class userLenderAdapter extends RecyclerView.Adapter<userLenderAdapter.userLenderHolder> {

    LinkedList<userLenderModel> lenderList;
    Context context;
    String type;
    ProfileHelper profileHelper;
    userInterfaceHelper UIHelper;
    userHome user_home;
    public static final String admin = "admin",
            user = "user",
            archive = "archive";
    public userLenderAdapter(LinkedList<userLenderModel> lenderList, Context context, String type, ProfileHelper profileHelper) {
        this.lenderList = lenderList;
        this.context = context;
        this.type = type;
        this.profileHelper = profileHelper;
    }
    public userLenderAdapter(LinkedList<userLenderModel> lenderList, Context context, String type, ProfileHelper profileHelper, userInterfaceHelper UIHelper) {
        this.lenderList = lenderList;
        this.context = context;
        this.type = type;
        this.profileHelper = profileHelper;
        this.UIHelper = UIHelper;
    }

    public void setUser_home(userHome user_home) {
        this.user_home = user_home;
    }

    @NonNull
    @Override
    public userLenderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_lenders_view, parent, false);

        return new userLenderAdapter.userLenderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userLenderHolder holder, int position) {
        holder.companyName.setText(lenderList.get(position).getCompanyName().replaceAll("_", " "));
        holder.priceRange.setText("₱"+lenderList.get(position).getMinPrice() + "—" + "₱" +lenderList.get(position).getMaxPrice());
        holder.interest.setText(lenderList.get(position).getInterest() + "%  |  " + lenderList.get(position).getFrequency());
        holder.profile.setImageBitmap(lenderList.get(position).getPic());
        holder.email.setText(lenderList.get(position).getEmail());
        // This is for admin
        if(type.equals(admin))
        {
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.setConfirmation("ARCHIVE", "DO YOU REALLY WANT TO ARCHIVE THIS PROFILE?", "CANCEL", "ARCHIVE");
                    UIHelper.setNegativeConfirmation("cancel");
                    UIHelper.setPositiveConfirmation("archive",profileHelper, holder.companyName.getText().toString(), true);
                    UIHelper.setConfirmVisibility(true);
                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adminHome.admin.editLender(holder.companyName.getText().toString().replaceAll(" ", "_"));
                }
            });
        }

        // This is for user
        if(type.equals(user))
        {
            holder.edit.setVisibility(View.INVISIBLE);
            holder.remove.setText("Apply");
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //apply btn
                    if(user_home != null)
                        user_home.openApplyInfo(holder.companyName.getText().toString().replaceAll(" ", "_"));
                }
            });
        }

        if(type.equals(archive)) {
            holder.edit.setVisibility(View.INVISIBLE);
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.setConfirmation("UNARCHIVE", "DO YOU REALLY WANT TO UNARCHIVE THIS PROFILE?", "CANCEL", "UNARCHIVE");
                    UIHelper.setNegativeConfirmation("cancel");
                    UIHelper.setPositiveConfirmation("unarchive",profileHelper, holder.companyName.getText().toString(), true);
                    UIHelper.setConfirmVisibility(true);
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
