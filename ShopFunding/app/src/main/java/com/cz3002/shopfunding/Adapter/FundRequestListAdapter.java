package com.cz3002.shopfunding.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cz3002.shopfunding.Model.FundingRequest;
import com.cz3002.shopfunding.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FundRequestListAdapter extends RecyclerView.Adapter<FundRequestListAdapter.FundRequestViewHolder> {
    private List<FundingRequest> mFundingRequests;

    public FundRequestListAdapter(ArrayList<FundingRequest> requests) {
        mFundingRequests = requests;
    }

    public static class FundRequestViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public TextView date;
        public TextView progress;
        public TextView goal;

        public FundRequestViewHolder(View v) {
            super(v);
            this.productName = v.findViewById(R.id.tv_product_name);
            this.date = v.findViewById(R.id.tv_creation_date);
            this.progress = v.findViewById(R.id.tv_progress);
            this.goal = v.findViewById(R.id.tv_goal);
        }
    }

    @Override
    @NonNull
    public FundRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_fund_request, parent, false);

        FundRequestViewHolder vh = new FundRequestViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FundRequestViewHolder holder, int position) {
        holder.productName.setText(mFundingRequests.get(position).getProductName());
        holder.date.setText(mFundingRequests.get(position).getCreationDate());
        float goal = mFundingRequests.get(position).getGoal();
        float progress = mFundingRequests.get(position).getProgress();
        holder.goal.setText(Float.toString(goal));
        NumberFormat format = NumberFormat.getPercentInstance(Locale.US);
        holder.progress.setText(format.format(progress / goal * 100));
    }

    @Override
    public int getItemCount() {
        return mFundingRequests.size();
    }
}
