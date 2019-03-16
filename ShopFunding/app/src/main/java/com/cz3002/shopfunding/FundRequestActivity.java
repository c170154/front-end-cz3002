package com.cz3002.shopfunding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.cz3002.shopfunding.API.FundRequest;
import com.cz3002.shopfunding.API.ProductQuery;
import com.cz3002.shopfunding.Adapter.FriendListAdapter;
import com.cz3002.shopfunding.Helper.DownloadImageTask;
import com.cz3002.shopfunding.Model.FundingRequest;
import com.cz3002.shopfunding.Model.Product;
import com.cz3002.shopfunding.Model.UserProfile;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.ArrayList;


public class FundRequestActivity extends BaseActivity {
    private ArrayList<String> mSelected;

    // RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    // Async API call task
    private CreateRequestTask mCreateRequestTask;
    private AddContributorsTask mAddContributorsTask;
    private FetchProductTask mFetchProductTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_request);
        super.onCreateDrawer();
        getSupportActionBar().setTitle("Create Fund Request");

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

        Intent intent = getIntent();
        int itemID = intent.getIntExtra("item_id", -1);
        int shopID = intent.getIntExtra("shop_id", -1);
        if (itemID != -1 && shopID != -1) {
            mFetchProductTask = new FetchProductTask(getApplicationContext(), itemID, shopID);
            mFetchProductTask.execute((Void) null);
        }
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
            mGoal = BigDecimal.valueOf(goal).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
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

    class FetchProductTask extends AsyncTask<Void, Void, Product> {
        private final Context mContext;
        private final int mItemID;
        private final int mShopID;

        public FetchProductTask(Context context, int itemID, int shopID) {
            mContext = context;
            mItemID = itemID;
            mShopID = shopID;
        }

        @Override
        protected Product doInBackground(Void... params) {
            return ProductQuery.getShopeeProduct(mContext, mItemID, mShopID);
        }

        @Override
        protected void onPostExecute(final Product product) {
            mFetchProductTask = null;

            if (product != null) {
                final String productName = product.getName();
                final String description = product.getDescription();
                final double price = product.getMinPrice();
                final String imageURL = product.getImageLinks().get(0);

                // Update UI
                ((TextView) findViewById(R.id.tv_product_name)).setText(productName);
                ((TextView) findViewById(R.id.tv_product_description)).setText(description);
                ((TextView) findViewById(R.id.tv_total_price)).setText("S$" + price);
                ImageView imageView = findViewById(R.id.image_product);
                Picasso.get()
                        .load(imageURL)
                        .placeholder(R.drawable.progress_animation)
                        .fit()
                        .centerInside()
                        .into(imageView);

                final Button create = findViewById(R.id.button_create_request);
                create.setEnabled(true);
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCreateRequestTask = new CreateRequestTask(
                                getApplicationContext(), userProfile, productName, description, imageURL, (float) price
                        );
                        mCreateRequestTask.execute((Void) null);
                    }
                });
            }
        }
    }
}
