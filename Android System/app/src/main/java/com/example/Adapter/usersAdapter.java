package com.example.Adapter;

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
import com.example.model.userLenderModel;
import com.example.model.usersModel;
import com.example.taskperformance.R;

import java.util.LinkedList;

public class usersAdapter extends RecyclerView.Adapter<usersAdapter.usersHolder> {

    LinkedList<usersModel> userList;
    Context context;
    String type;
    ProfileHelper profileHelper;
    userInterfaceHelper UIHelper;
    public static final String admin = "admin", user = "user", archive = "archive";
    public usersAdapter(LinkedList<usersModel> userList, Context context, String type, ProfileHelper profileHelper) {
        this.userList = userList;
        this.context = context;
        this.type = type;
        this.profileHelper = profileHelper;
    }

    public usersAdapter(LinkedList<usersModel> userList, Context context, String type, ProfileHelper profileHelper, userInterfaceHelper UIHelper) {
        this.userList = userList;
        this.context = context;
        this.type = type;
        this.profileHelper = profileHelper;
        this.UIHelper = UIHelper;
    }
    @NonNull
    @Override
    public usersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_borrowers_view, parent, false);

        return new usersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull usersHolder holder, int position) {
        holder.nameTxt.setText(userList.get(position).getName());
        holder.typeTxt.setText(userList.get(position).getType());
        holder.profile.setImageBitmap(userList.get(position).getPicture());
        holder.email.setText(userList.get(position).getEmail());
        holder.fullname.setText(userList.get(position).getFullname());

        if(type.equals(admin)) {
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.setConfirmation("ARCHIVE", "DO YOU REALLY WANT TO ARCHIVE THIS PROFILE?", "CANCEL", "ARCHIVE");
                    UIHelper.setNegativeConfirmation("cancel");
                    UIHelper.setPositiveConfirmation("archive",profileHelper, holder.nameTxt.getText().toString(), false);
                    UIHelper.setConfirmVisibility(true);
                }
            });
        }
        else if(type.equals(archive)) {
            holder.edit.setVisibility(View.INVISIBLE);
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.setConfirmation("UNARCHIVE", "DO YOU REALLY WANT TO UNARCHIVE THIS PROFILE?", "CANCEL", "UNARCHIVE");
                    UIHelper.setNegativeConfirmation("cancel");
                    UIHelper.setPositiveConfirmation("unarchive",profileHelper, holder.nameTxt.getText().toString(), false);
                    UIHelper.setConfirmVisibility(true);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class usersHolder extends RecyclerView.ViewHolder {

        TextView nameTxt, typeTxt, email, fullname;
        ImageView profile;
        Button remove, edit;
        public usersHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.userNameTxt);
            typeTxt = itemView.findViewById(R.id.userTypeTxt);
            email = itemView.findViewById(R.id.emailTxt);
            profile = itemView.findViewById(R.id.profileView);
            fullname = itemView.findViewById(R.id.fullname);
            remove = itemView.findViewById(R.id.usersRemoveBtn);
            edit = itemView.findViewById(R.id.usersEditBtn);
        }
    }
}
