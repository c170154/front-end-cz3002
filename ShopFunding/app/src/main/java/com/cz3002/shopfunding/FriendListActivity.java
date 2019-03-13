package com.cz3002.shopfunding;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.cz3002.shopfunding.API.User;
import com.cz3002.shopfunding.Adapter.FriendListAdapter;
import com.cz3002.shopfunding.Model.UserProfile;

import java.util.ArrayList;

public class FriendListActivity extends BaseActivity {
    // RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private GetFriendListTask mGetFriendsTask;
    private AddFriendTask mAddFriendTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        super.onCreateDrawer();
        getSupportActionBar().setTitle("My Friends");

        // Initialize RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.friendlist_recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FriendListAdapter();
        recyclerView.setAdapter(adapter);

        // Add friend handler
        Button addFriendButton = (Button) findViewById(R.id.add_friend_btn);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String friendUsername = ((EditText) findViewById(R.id.input_username)).getText().toString();
                mAddFriendTask = new AddFriendTask(getApplicationContext(),
                        FriendListActivity.this.userProfile, friendUsername);
                mAddFriendTask.execute((Void) null);
            }
        });

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

            if (friendList != null) {
                ((FriendListAdapter) adapter).addAll(friendList);
            }
        }
    }

    public class AddFriendTask extends AsyncTask<Void, Void, UserProfile> {
        private final Context mContext;
        private final int mUserID;
        private final String mFriendUsername;

        public AddFriendTask(Context context, UserProfile userProfile, String friendUsername) {
            mContext = context;
            mUserID = userProfile.get_user_id();
            mFriendUsername = friendUsername;
        }

        @Override
        protected UserProfile doInBackground(Void... params) {
            return User.addFriend(mContext, mUserID, mFriendUsername);
        }

        @Override
        protected void onPostExecute(final UserProfile userProfile) {
            mAddFriendTask = null;

            if (userProfile != null) {
                ((FriendListAdapter) adapter).add(userProfile.get_username());
            }
        }
    }
}
