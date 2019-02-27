package com.cz3002.shopfunding;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import com.cz3002.shopfunding.Adapter.FriendListAdapter;

public class FriendListActivity extends BaseActivity {
    // RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

//    private GetBalanceTask mAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);
        super.onCreateDrawer();
        getSupportActionBar().setTitle("My Friends");

        // Initialize RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.friendlist_recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FriendListAdapter(new String[]{"user1", "user2", "user3", "user4", "user5"});
        recyclerView.setAdapter(adapter);

        // Add friend handler
        Button addFriendButton = (Button) findViewById(R.id.add_friend_btn);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: update database
            }
        });

//        mAsyncTask = new GetBalanceTask(getApplicationContext(), this.userProfile);
//        mAsyncTask.execute((Void) null);
    }

//    private class GetFriendsTask extends FetchBalanceTask {
//        GetBalanceTask(Context context, UserProfile userProfile) {
//            super(context, userProfile, User.getJWTToken(context));
//        }
//
//        @Override
//        protected void onPostExecute(final Float balance) {
//            mAsyncTask = null;
//            TextView balance_display = FriendListActivity.this.findViewById(R.id.balance);
//            balance_display.setText(String.format("%.2f", balance));
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAsyncTask = null;
//        }
//    }
}
