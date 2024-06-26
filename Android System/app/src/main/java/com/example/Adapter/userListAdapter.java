package com.example.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Helper.DateHelper;
import com.example.Helper.ProfileHelper;
import com.example.Helper.userInterfaceHelper;
import com.example.model.applyModel;
import com.example.model.listBorrowModel;
import com.example.taskperformance.R;
import com.example.taskperformance.lenderHome;

import java.util.LinkedList;

public class userListAdapter extends RecyclerView.Adapter<userListAdapter.userListAdapterHolder> {

    LinkedList<listBorrowModel> borrowList;
    ProfileHelper profileHelper;
    userInterfaceHelper UIHelper;
    Context context;
    String type;
    String borrowerName;
    double payment;

    public static final String borrower = "borrow",
            lender = "lender";

    public userListAdapter(LinkedList<listBorrowModel> borrowList, ProfileHelper profileHelper, userInterfaceHelper UIHelper, Context context, String type, String borrowerName) {
        this.borrowList = borrowList;
        this.profileHelper = profileHelper;
        this.UIHelper = UIHelper;
        this.context = context;
        this.type = type;
        this.borrowerName = borrowerName;
    }


    @NonNull
    @Override
    public userListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_view, parent, false);

        return new userListAdapter.userListAdapterHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull userListAdapterHolder holder, @SuppressLint("RecyclerView") int position) {
        double total = borrowList.get(position).getTotal() + (borrowList.get(position).getTotal() * (borrowList.get(position).getInterest() / 100));
        double remaining = borrowList.get(position).getRemaining() + (borrowList.get(position).getTotal() * (borrowList.get(position).getInterest() / 100));

        holder.dueDate.setText("DUE DATE: " + borrowList.get(position).getDate());
        payment = borrowList.get(position).getPayment();

        if(DateHelper.isDue(borrowList.get(position).getDate())) {
            holder.status.setText("WAITING FOR PAYMENT");
        } else {
            holder.status.setText("PAST DUE");
        }
        if(type.equals(borrower)) {
             holder.name.setText(borrowList.get(position).getName());
            holder.email.setText(borrowList.get(position).getEmail());
            holder.total.setText("Total: " + String.format("%.2f", total)+
                    " | Year: "+borrowList.get(position).getYear());
            holder.remaining.setText(String.format("%.2f", remaining)
                    + " | " + borrowList.get(position).getFrequency().toUpperCase());
            holder.pic.setImageBitmap(borrowList.get(position).getPic());

            if(!DateHelper.isDue(borrowList.get(position).getDate()))
            {
                payment = payment + (payment * (Math.abs((2.0 * DateHelper.calculateMonthsBetween(borrowList.get(position).getDate()))/100)));
                Log.d("pay", payment + "");
                holder.payBtn.setText("PAY NOW: " + String.format("%.2f", payment) + "\n(+"+
                        Math.abs(2 * DateHelper.calculateMonthsBetween(borrowList.get(position).getDate())) + "%)");
            }
            else {
                holder.payBtn.setText("PAY NOW: " + String.format("%.2f", payment));
            }
            holder.payBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double remaining = borrowList.get(position).getRemaining() - payment;
                    if(remaining < 0) remaining = 0;
                    UIHelper.setConfirmation("PAYMENT", "DO YOU REALLY WANT PAY?", "CANCEL", "PAY");
                    UIHelper.setNegativeConfirmation("cancel");
                    UIHelper.setPositiveConfirmation_pay(profileHelper, borrowerName, borrowList.get(position).getName(),
                            remaining);
                    UIHelper.setConfirmVisibility(true);

                }
            });
        }

        if(type.equals(lender)) {
            holder.payBtn.setVisibility(View.GONE);

            holder.name.setText(borrowList.get(position).getName());
            holder.email.setText(borrowList.get(position).getEmail());
            holder.total.setText("Total: "+String.format("%.2f", total));
            holder.remaining.setText("Remaining: "+String.format("%.2f", remaining)
                    + "\nYear: " + borrowList.get(position).getYear());
            holder.pic.setImageBitmap(borrowList.get(position).getPic());
        }

    }

    @Override
    public int getItemCount() {
        return borrowList == null ? 0 : borrowList.size();
    }

    public static class userListAdapterHolder extends RecyclerView.ViewHolder {

        TextView name, email, total, remaining, dueDate, status;
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

            dueDate = itemView.findViewById(R.id.lastDueTxt);
            status = itemView.findViewById(R.id.statusTxt);
        }
    }
}
