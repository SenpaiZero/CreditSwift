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
import com.example.model.listBorrowModel;
import com.example.taskperformance.R;

import java.util.LinkedList;

public class userListAdapter extends RecyclerView.Adapter<userListAdapter.userListAdapterHolder> {

    LinkedList<listBorrowModel> borrowList;
    ProfileHelper profileHelper;
    userInterfaceHelper UIHelper;
    Context context;
    String type;

    public static final String borrower = "borrow",
            lender = "lender";

    public userListAdapter(LinkedList<listBorrowModel> borrowList, ProfileHelper profileHelper, userInterfaceHelper UIHelper, Context context, String type) {
        this.borrowList = borrowList;
        this.profileHelper = profileHelper;
        this.UIHelper = UIHelper;
        this.context = context;
        this.type = type;
    }


    @NonNull
    @Override
    public userListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_view, parent, false);

        return new userListAdapter.userListAdapterHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull userListAdapterHolder holder, int position) {
        if(type.equals(borrower)) {
            holder.name.setText(borrowList.get(position).getName());
            holder.email.setText(borrowList.get(position).getEmail());
            holder.total.setText("Total: " + borrowList.get(position).getTotal()+
                    " | Year: "+borrowList.get(position).getYear());
            holder.remaining.setText(borrowList.get(position).getRemaining()
                    + "| " + borrowList.get(position).getFrequency().toUpperCase());
            holder.pic.setImageBitmap(borrowList.get(position).getPic());

            holder.payBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        if(type.equals(lender)) {
            holder.payBtn.setVisibility(View.INVISIBLE);

            holder.name.setText(borrowList.get(position).getName());
            holder.email.setText(borrowList.get(position).getEmail());
            holder.total.setText("Total: " +borrowList.get(position).getTotal());
            holder.remaining.setText("Remaining: "+borrowList.get(position).getRemaining()
                    + "\nYear: " + borrowList.get(position).getYear());
            holder.pic.setImageBitmap(borrowList.get(position).getPic());
        }
    }

    @Override
    public int getItemCount() {
        return borrowList == null ? 0 : borrowList.size();
    }

    public static class userListAdapterHolder extends RecyclerView.ViewHolder {

        TextView name, email, total, remaining;
        ImageView pic;
        Button payBtn;
        public userListAdapterHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.companyTxt);
            email = itemView.findViewById(R.id.emailViewTxt);
            total = itemView.findViewById(R.id.totalViewTxt);
            remaining = itemView.findViewById(R.id.remainingFreqViewTxt);

            pic = itemView.findViewById(R.id.profileView);
            payBtn = itemView.findViewById(R.id.payNowBtn);
        }
    }
}
