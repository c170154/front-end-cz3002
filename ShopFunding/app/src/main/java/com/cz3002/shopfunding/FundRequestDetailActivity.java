package com.cz3002.shopfunding;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.cz3002.shopfunding.API.FundRequest;
import com.cz3002.shopfunding.Adapter.ContributorAdapter;
import com.cz3002.shopfunding.Model.Contribution;
import com.cz3002.shopfunding.Model.FundingRequest;
import com.squareup.picasso.Picasso;

public class FundRequestDetailActivity extends BaseActivity {
    // RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private FetchRequestTask mFetchRequestTask;
    private FetchContributorsTask mFetchContributorsTask;
    private UpdateContributionTask mUpdateContributionTask;

    private Contribution mContribution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_request_detail);
        super.onCreateDrawer();
        getSupportActionBar().setTitle("Fund Request");

        // Initialize RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_contributors);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        int requestID = getIntent().getIntExtra("request_id", -1);

        mFetchRequestTask = new FetchRequestTask(getApplicationContext(), requestID);
        mFetchRequestTask.execute((Void) null);
        mFetchContributorsTask = new FetchContributorsTask(getApplicationContext(), requestID);
        mFetchContributorsTask.execute((Void) null);

        ((Button) findViewById(R.id.button_create_request)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float amount = Float.parseFloat(
                        ((EditText) findViewById(R.id.input_contribution_amount)).getText().toString());
                mUpdateContributionTask = new UpdateContributionTask(
                        getApplicationContext(), mContribution.getID(), mContribution.getAmount() + amount);
                mUpdateContributionTask.execute((Void) null);
            }
        });
    }

    public class FetchRequestTask extends AsyncTask<Void, Void, FundingRequest> {
        private final Context mContext;
        private final int mID;

        FetchRequestTask(Context context, int id) {
            mContext = context;
            mID = id;
        }

        @Override
        protected FundingRequest doInBackground(Void... params) {
            return FundRequest.fetchFundRequest(mContext, mID);
        }

        @Override
        protected void onPostExecute(final FundingRequest fundingRequest) {
             mFetchRequestTask = null;

             float goal = fundingRequest.getGoal();
             float remainingFund = fundingRequest.getmRemainingFund();
             if (remainingFund == 0) {
                 findViewById(R.id.section_contribution).setVisibility(View.GONE);
             }
             float funded_amount = goal - remainingFund;
             updateProgressBar(funded_amount, goal);
            ((TextView) findViewById(R.id.tv_product_name)).setText(fundingRequest.getProductName());
            ((TextView) findViewById(R.id.tv_product_description)).setText(fundingRequest.getDescription());
            ((TextView) findViewById(R.id.tv_total_price)).setText("S$" + Float.toString(fundingRequest.getGoal()));
            ImageView imageView = findViewById(R.id.image_product);
            Picasso.get()
                    .load(fundingRequest.getUrl())
                    .placeholder(R.drawable.progress_animation)
                    .fit()
                    .centerInside()
                    .into(imageView);
        }
    }

    private void updateProgressBar(float amountFunded, float goal) {
        int progress = (int) ((amountFunded / goal) * 100);
        ((ProgressBar) findViewById(R.id.progressbar_funded)).setProgress(progress, true);
        if (amountFunded == goal) {
            ((TextView) findViewById(R.id.tv_progress)).setText(
                    "This item is fully funded and it is on its way! Good Job!");
        } else {
            ((TextView) findViewById(R.id.tv_progress)).setText("S$" + amountFunded + " out of S$" + goal + " funded");
        }
    }

    public class FetchContributorsTask extends AsyncTask<Void, Void, Contribution[]> {
        private final Context mContext;
        private final int mID;

        FetchContributorsTask(Context context, int id) {
            mContext = context;
            mID = id;
        }

        @Override
        protected Contribution[] doInBackground(Void... params) {
            return FundRequest.fetchContributions(mContext, mID);
        }

        @Override
        protected void onPostExecute(final Contribution[] contributions) {
            mFetchContributorsTask = null;

            for (Contribution contribution: contributions) {
                if (contribution.getUsername().equals(FundRequestDetailActivity.this.userProfile.get_username())) {
                    FundRequestDetailActivity.this.mContribution = contribution;
                    break;
                }
            }

            adapter = new ContributorAdapter(contributions);
            recyclerView.setAdapter(adapter);
        }
    }

    public class UpdateContributionTask extends AsyncTask<Void, Void, Float> {
        private final Context mContext;
        private final int mID;
        private final float mAmount;

        UpdateContributionTask(Context context, int id, float amount) {
            mContext = context;
            mID = id;
            mAmount = amount;
        }

        @Override
        protected Float doInBackground(Void... params) {
            return FundRequest.updateContribution(mContext, mID, mAmount);
        }

        @Override
        protected void onPostExecute(final Float amount) {
            mUpdateContributionTask = null;
            ((EditText) findViewById(R.id.input_contribution_amount)).onEditorAction(EditorInfo.IME_ACTION_DONE);

            if (amount > 0) {
                finish();
                startActivity(getIntent());
            }
        }
    }
}
