package com.cz3002.shopfunding.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alexzh.circleimageview.CircleImageView;
import com.cz3002.shopfunding.R;

import java.util.ArrayList;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendViewHolder> {
    private ArrayList<String> mUsername;

    public FriendListAdapter() {
        this.mUsername = new ArrayList<>();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public View actionButton;

        public FriendViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.friend_username);
            CircleImageView imageView = v.findViewById(R.id.friend_img);
            imageView.setEnabled(false);
            actionButton = v.findViewById(R.id.button_friend_action);
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
        holder.textView.setText(this.mUsername.get(position));
    }

    @Override
    public int getItemCount() {
        return this.mUsername.size();
    }

    public String get(int position) {
        return mUsername.get(position);
    }

    public void add(String username) {
        this.mUsername.add(username);
        this.notifyDataSetChanged();
    }

    public void addAll(ArrayList<String> usernames) {
        this.mUsername.addAll(usernames);
        this.notifyDataSetChanged();
    }

    public void set(ArrayList<String> usernames) {
        this.mUsername = usernames;
        this.notifyDataSetChanged();
    }
}
