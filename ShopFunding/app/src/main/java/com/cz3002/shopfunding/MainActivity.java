package com.cz3002.shopfunding;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.cz3002.shopfunding.API.FundRequest;
import com.cz3002.shopfunding.Adapter.FundRequestListAdapter;
import com.cz3002.shopfunding.Model.FundingRequest;
import com.cz3002.shopfunding.Model.UserProfile;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    // RecyclerView
    private RecyclerView requestRecyclerView;
    private RecyclerView.LayoutManager requestLayoutManager;
    private RecyclerView.Adapter requestAdapter;

    private RecyclerView friendRequestRecyclerView;
    private RecyclerView.LayoutManager friendRequestLayoutManager;
    private RecyclerView.Adapter friendRequestAdapter;

    // Async API call task
    private GetRequestListTask mGetRequestListTask;
    private GetFriendRequestListTask mGetFriendRequestListTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreateDrawer();

        Button test = findViewById(R.id.button_test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityIntent = new Intent(MainActivity.this, FundRequestActivity.class);
                startActivity(activityIntent);
            }
        });

        ImageButton search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(activityIntent);
            }
        });

        // Initialize RecyclerView
        requestRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_my_requests);
        requestLayoutManager = new LinearLayoutManager(this);
        requestRecyclerView.setLayoutManager(requestLayoutManager);
        requestRecyclerView.setNestedScrollingEnabled(false);

        friendRequestRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_friendrequest);
        friendRequestLayoutManager = new LinearLayoutManager(this);
        friendRequestRecyclerView.setLayoutManager(friendRequestLayoutManager);
        friendRequestRecyclerView.setNestedScrollingEnabled(false);

        fetcbRequests();
    }

    @Override
    protected void onResume() {
        super.onResume();

        fetcbRequests();
    }

    private void fetcbRequests() {
        mGetRequestListTask = new GetRequestListTask(getApplicationContext(), this.userProfile);
        mGetRequestListTask.execute((Void) null);

        mGetFriendRequestListTask = new GetFriendRequestListTask(getApplicationContext(), this.userProfile);
        mGetFriendRequestListTask.execute((Void) null);
    }

    // Async API call task
    private class GetRequestListTask extends AsyncTask<Void, Void, ArrayList<FundingRequest>> {
        private final Context mContext;
        private final int mUserID;

        GetRequestListTask(Context context, UserProfile userProfile) {
            mContext = context;
            mUserID = userProfile.get_user_id();
        }

        @Override
        protected ArrayList<FundingRequest> doInBackground(Void... params) {
            return FundRequest.fetchFundRequests(mContext);
        }

        @Override
        protected void onPostExecute(final ArrayList<FundingRequest> requests) {
            mGetRequestListTask = null;

            if (requests != null) {
                requestAdapter = new FundRequestListAdapter(requests);
                requestRecyclerView.setAdapter(requestAdapter);
            }
        }
    }

    private class GetFriendRequestListTask extends AsyncTask<Void, Void, ArrayList<FundingRequest>> {
        private final Context mContext;
        private final int mUserID;

        GetFriendRequestListTask(Context context, UserProfile userProfile) {
            mContext = context;
            mUserID = userProfile.get_user_id();
        }

        @Override
        protected ArrayList<FundingRequest> doInBackground(Void... params) {
            return FundRequest.fetchFriendFundRequests(mContext, userProfile.get_user_id());
        }

        @Override
        protected void onPostExecute(final ArrayList<FundingRequest> requests) {
            mGetRequestListTask = null;

            if (requests != null) {
                friendRequestAdapter = new FundRequestListAdapter(requests);
                friendRequestRecyclerView.setAdapter(friendRequestAdapter);
            }
        }
    }
}
