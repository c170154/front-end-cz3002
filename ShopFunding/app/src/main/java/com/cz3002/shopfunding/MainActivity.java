package com.cz3002.shopfunding;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import com.cz3002.shopfunding.API.FundRequest;
import com.cz3002.shopfunding.Adapter.FundRequestListAdapter;
import com.cz3002.shopfunding.Model.FundingRequest;
import com.cz3002.shopfunding.Model.UserProfile;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    // RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    // Async API call task
    private GetRequestListTask mGetRequestListTask;

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

        // Initialize RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_my_requests);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mGetRequestListTask = new GetRequestListTask(getApplicationContext(), this.userProfile);
        mGetRequestListTask.execute((Void) null);
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
                adapter = new FundRequestListAdapter(requests);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}
