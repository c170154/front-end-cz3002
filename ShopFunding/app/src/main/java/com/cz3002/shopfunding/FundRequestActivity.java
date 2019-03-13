package com.cz3002.shopfunding;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.cz3002.shopfunding.API.FundRequest;
import com.cz3002.shopfunding.Adapter.FriendListAdapter;
import com.cz3002.shopfunding.Model.FundingRequest;
import com.cz3002.shopfunding.Model.UserProfile;

import java.util.ArrayList;


public class FundRequestActivity extends BaseActivity {
    private CreateRequestTask mCreateRequestTask;
    private AddContributorsTask mAddContributorsTask;

    private ArrayList<String> mSelected;

    // RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_request);
        super.onCreateDrawer();
        getSupportActionBar().setTitle("Create Fund Request");

        final Button create = findViewById(R.id.button_create_request);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = ((TextView) findViewById(R.id.tv_product_name)).getText().toString();
                String description = ((TextView) findViewById(R.id.tv_product_description)).getText().toString();
                // TODO: get url from product page
                String url = "www.randomurl.com";
                float goal = 120;
                mCreateRequestTask = new CreateRequestTask(getApplicationContext(), userProfile,
                        productName, description,url, goal);
                mCreateRequestTask.execute((Void) null);
            }
        });

        mSelected = new ArrayList<>();

        // Add friends
        final Button addFriend = findViewById(R.id.button_invite_friend);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityIntent = new Intent(FundRequestActivity.this, AddContributorActivity.class);
                activityIntent.putExtra("selected", mSelected);
                startActivityForResult(activityIntent, 1);
            }
        });

        // Initialize RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_invited_friend);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FriendListAdapter();
        recyclerView.setAdapter(adapter);
    }

    // This method is invoked when target activity return result data back.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        switch (requestCode)
        {
            // This request code is set by startActivityForResult(intent, REQUEST_CODE_1) method.
            case 1:
                if(resultCode == RESULT_OK)
                {
                    ArrayList<String> receivedList = dataIntent.getStringArrayListExtra("selected");
                    mSelected.addAll(receivedList);
                    if (mSelected != null) {
                        ((FriendListAdapter) adapter).addAll(receivedList);
                    }
                }
        }
    }

    // Async tasks for API call
    class CreateRequestTask extends AsyncTask<Void, Void, Integer> {
        private final Context mContext;
        private final int mUserID;
        private final String mProductName;
        private final String mDescription;
        private final String mUrl;
        private final float mGoal;

        public CreateRequestTask(Context context, UserProfile userProfile,
                         String productName, String description, String url, float goal) {
            mUserID = userProfile.get_user_id();
            mContext = context;
            mProductName = productName;
            mDescription = description;
            mUrl = url;
            mGoal = goal;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return FundRequest.createRequest(mContext, mProductName, mDescription, mUrl, mGoal);
        }

        @Override
        protected void onPostExecute(final Integer id) {
            mCreateRequestTask = null;
            if (id != -1) {
                mAddContributorsTask = new AddContributorsTask(mContext, id, mSelected.toArray(new String[0]));
                mAddContributorsTask.execute((Void) null);
            }
        }
    }

    class AddContributorsTask extends AsyncTask<Void, Void, Boolean> {
        private final Context mContext;
        private final int mRequestID;
        private final String[] mUsernames;

        public AddContributorsTask(Context context, int requestID, String[] usernames) {
            mContext = context;
            mRequestID = requestID;
            mUsernames = usernames;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return FundRequest.addContributors(mContext, mRequestID, mUsernames);
        }

        @Override
        protected void onPostExecute(final Boolean isSuccessful) {
            mAddContributorsTask = null;

            if (isSuccessful) {
                Toast.makeText(mContext, "Request created successfully!", Toast.LENGTH_SHORT).show();
            }

            Intent activityIntent = new Intent(FundRequestActivity.this, MainActivity.class);
            startActivity(activityIntent);
            FundRequestActivity.this.finish();
        }
    }
}