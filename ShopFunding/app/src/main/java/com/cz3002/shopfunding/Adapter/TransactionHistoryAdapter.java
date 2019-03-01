package com.cz3002.shopfunding.Adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cz3002.shopfunding.Model.Transaction;
import com.cz3002.shopfunding.R;

import java.util.List;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.TransactionViewHolder> {
    private List<Transaction> mTransactions;

    public TransactionHistoryAdapter(List<Transaction> transactions) {
        mTransactions = transactions;
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        public TextView position;
        public TextView name;
        public TextView date;
        public TextView amount;

        public TransactionViewHolder(View v) {
            super(v);
            this.position = v.findViewById(R.id.tv_transaction_number);
            this.name = v.findViewById(R.id.tv_transaction_name);
            this.date = v.findViewById(R.id.tv_transaction_date);
            this.amount = v.findViewById(R.id.tv_transaction_amount);
        }
    }

    @Override
    @NonNull
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_transaction, parent, false);

        TransactionViewHolder vh = new TransactionViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        holder.position.setText((position + 1) + ". ");
        holder.name.setText(mTransactions.get(position).getName());
        holder.date.setText(mTransactions.get(position).getDate());

        int amount = mTransactions.get(position).getAmount();

        if (amount < 0) {
            holder.amount.setText("-S$" + (amount * -1));
            holder.amount.setTextColor(Color.RED);
        } else {
            holder.amount.setText("S$" + amount);
            holder.amount.setTextColor(Color.GREEN);
        }

    }

    @Override
    public int getItemCount() {
        return mTransactions.size();
    }
}
