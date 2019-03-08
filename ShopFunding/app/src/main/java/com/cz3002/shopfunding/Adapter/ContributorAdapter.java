package com.cz3002.shopfunding.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alexzh.circleimageview.CircleImageView;
import com.cz3002.shopfunding.Model.Contribution;
import com.cz3002.shopfunding.R;

import java.util.ArrayList;

public class ContributorAdapter extends RecyclerView.Adapter<ContributorAdapter.ContributorViewHolder> {
    private ArrayList<Contribution> mContributions;

    public ContributorAdapter(Contribution[] contributions) {
        mContributions = new ArrayList<>();
        for (Contribution contribution: contributions) {
            mContributions.add(contribution);
        }
    }

    static class ContributorViewHolder extends RecyclerView.ViewHolder {
        TextView number;
        TextView contributorName;
        TextView contributedAmount;

        ContributorViewHolder(View v) {
            super(v);
            number = v.findViewById(R.id.tv_number);
            contributorName = v.findViewById(R.id.tv_contributor_name);
            contributedAmount = v.findViewById(R.id.tv_contributed_amount);

            CircleImageView imageView = v.findViewById(R.id.friend_img);
            imageView.setEnabled(false);
        }
    }

    @Override
    @NonNull
    public ContributorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_contributor, parent, false);

        ContributorViewHolder vh = new ContributorViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ContributorViewHolder holder, int position) {
        String username = this.mContributions.get(position).getUsername();
        float amount = this.mContributions.get(position).getAmount();

        holder.number.setText((position + 1) + ". ");
        holder.contributorName.setText(username);
        holder.contributedAmount.setText("S$" + amount);
    }

    @Override
    public int getItemCount() {
        return mContributions.size();
    }
}
