package com.cz3002.shopfunding.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.cz3002.shopfunding.FundRequestDetailActivity;
import com.cz3002.shopfunding.Model.FundingRequest;
import com.cz3002.shopfunding.R;
import com.squareup.picasso.Picasso;

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
        private CardView fundRequest;
        private TextView productName;
        private TextView date;
        private TextView progress;
        private TextView goal;
        private ImageView imageView;

        private Context context;

        public FundRequestViewHolder(View v) {
            super(v);
            this.fundRequest = v.findViewById(R.id.card_fund_request);
            this.productName = v.findViewById(R.id.tv_product_name);
            this.date = v.findViewById(R.id.tv_creation_date);
            this.progress = v.findViewById(R.id.tv_progress);
            this.goal = v.findViewById(R.id.tv_goal);
            this.imageView = v.findViewById(R.id.image_product);

            context = v.getContext();
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
    public void onBindViewHolder(final FundRequestViewHolder holder, int position) {
        final FundingRequest fundRequest = mFundingRequests.get(position);

        holder.productName.setText(fundRequest.getProductName());
        holder.date.setText(fundRequest.getCreationDate());

        float goal = fundRequest.getGoal();
        float progress = (goal - fundRequest.getmRemainingFund()) / goal;
        holder.goal.setText("Goal: S$" + goal);
        holder.progress.setText(NumberFormat.getPercentInstance(Locale.US).format(progress));
        Picasso.get()
                .load(fundRequest.getUrl())
                .placeholder(R.drawable.progress_animation)
                .fit()
                .centerInside()
                .into(holder.imageView);

        // onClick handler to navigate to contribute activity
        holder.fundRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityIntent = new Intent(holder.context, FundRequestDetailActivity.class);
                activityIntent.putExtra("request_id", fundRequest.getID());
                holder.context.startActivity(activityIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFundingRequests.size();
    }
}
