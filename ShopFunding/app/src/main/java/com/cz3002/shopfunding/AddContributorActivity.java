package com.cz3002.shopfunding;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.cz3002.shopfunding.API.User;
import com.cz3002.shopfunding.Adapter.FriendListAdapter;
import com.cz3002.shopfunding.Model.UserProfile;

import java.util.ArrayList;

public class AddContributorActivity extends BaseActivity {
    // RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private GetFriendListTask mGetFriendsTask;
    private ArrayList<String> selectedUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contributor);
        super.onCreateDrawer();
        getSupportActionBar().setTitle("Add Contributor(s)");

        selectedUsername = new ArrayList<String>();

        // Add contributor
        Button confirm = findViewById(R.id.btn_add_contributor);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("selected", selectedUsername);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // Initialize RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_friendlist);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FriendListAdapter(){
            @Override
            @NonNull
            public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_contributor, parent, false);

                FriendViewHolder vh = new FriendViewHolder(v);
                return vh;
            }

            @Override
            public void onBindViewHolder(FriendViewHolder holder, int position) {
                final String username = ((FriendListAdapter) adapter).get(position);
                holder.textView.setText(username);
                CheckBox checkBox = (CheckBox) holder.actionButton;
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            AddContributorActivity.this.selectedUsername.add(username);
                        } else {
                            AddContributorActivity.this.selectedUsername.remove(username);
                        }

                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);

        mGetFriendsTask = new GetFriendListTask(getApplicationContext(), this.userProfile);
        mGetFriendsTask.execute((Void) null);
    }

    // Async tasks for API call
    public class GetFriendListTask extends AsyncTask<Void, Void, ArrayList<String>> {
        private final Context mContext;
        private final int mUserID;

        GetFriendListTask(Context context, UserProfile userProfile) {
            mContext = context;
            mUserID = userProfile.get_user_id();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            return User.fetchFriendList(mContext, mUserID);
        }

        @Override
        protected void onPostExecute(final ArrayList<String> friendList) {
            mGetFriendsTask = null;

            if (friendList.size() > 0) {
                Intent previousActivity = getIntent();
                ArrayList<String> previousSelected = previousActivity.getStringArrayListExtra("selected");

                ArrayList<String> temp = new ArrayList<>();
                for (String username: friendList) {
                    if (!previousSelected.contains(username)) {
                        temp.add(username);
                    }
                }

                ((FriendListAdapter) adapter).addAll(temp);
            }
        }
    }
}
