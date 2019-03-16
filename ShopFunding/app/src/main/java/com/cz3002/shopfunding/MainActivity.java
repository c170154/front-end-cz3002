package com.cz3002.shopfunding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cz3002.shopfunding.API.FundRequest;
import com.cz3002.shopfunding.API.ProductQuery;
import com.cz3002.shopfunding.Adapter.FundRequestListAdapter;
import com.cz3002.shopfunding.Helper.DownloadImageTask;
import com.cz3002.shopfunding.Model.Carousel;
import com.cz3002.shopfunding.Model.FundingRequest;
import com.cz3002.shopfunding.Model.Product;
import com.cz3002.shopfunding.Model.UserProfile;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    // Carousel
    private CarouselView carouselView;

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
    private GetCarouselProductsTask mGetCarouselProductsTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreateDrawer();

        // Redirect button to search page
        CardView search = findViewById(R.id.card_search_product);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(activityIntent);
            }
        });

        // Initialize CarouselView
        carouselView = findViewById(R.id.carouselView);

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

        mGetCarouselProductsTask = new GetCarouselProductsTask(getApplicationContext());
        mGetCarouselProductsTask.execute((Void) null);
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
            mGetFriendRequestListTask = null;

            if (requests != null) {
                friendRequestAdapter = new FundRequestListAdapter(requests);
                friendRequestRecyclerView.setAdapter(friendRequestAdapter);
            }
        }
    }

    private class GetCarouselProductsTask extends AsyncTask<Void, Void, ArrayList<Carousel>> {
        private final Context mContext;

        GetCarouselProductsTask(Context context) {
            mContext = context;
        }

        @Override
        protected ArrayList<Carousel> doInBackground(Void... params) {
            return ProductQuery.getShopeeCarousel(mContext, 8);
        }

        @Override
        protected void onPostExecute(final ArrayList<Carousel> carousels) {
            mGetCarouselProductsTask = null;

            if (carousels != null) {
                final ImageListener imageListener = new ImageListener() {
                    @Override
                    public void setImageForPosition(int position, final ImageView imageView) {
                        final Carousel carousel = carousels.get(position);

                        imageView.setScaleType(ImageView.ScaleType.CENTER);
                        Picasso.get()
                                .load(carousel.getImage())
                                .placeholder(R.drawable.progress_animation)
                                .fit()
                                .centerInside()
                                .into(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent activityIntent = new Intent(
                                        MainActivity.this, FundRequestActivity.class
                                );
                                activityIntent.putExtra("item_id", carousel.getItemId());
                                activityIntent.putExtra("shop_id", carousel.getShopId());
                                startActivity(activityIntent);
                            }
                        });
                    }
                };
                carouselView.setImageListener(imageListener);
                carouselView.setPageCount(carousels.size());
            }
        }
    }
}
