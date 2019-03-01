package com.cz3002.shopfunding.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alexzh.circleimageview.CircleImageView;
import com.cz3002.shopfunding.R;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendViewHolder> {
    private String[] mUsername;

    public FriendListAdapter(String[] mUsername) {
        this.mUsername = mUsername;
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public FriendViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.friend_username);
            CircleImageView imageView = v.findViewById(R.id.friend_img);
            imageView.setEnabled(false);
        }
    }

    @Override
    @NonNull
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_friend, parent, false);

        FriendViewHolder vh = new FriendViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        holder.textView.setText(this.mUsername[position]);
    }

    @Override
    public int getItemCount() {
        return this.mUsername.length;
    }
}
