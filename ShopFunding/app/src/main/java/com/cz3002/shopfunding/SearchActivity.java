package com.cz3002.shopfunding;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.cz3002.shopfunding.API.ProductQuery;
import com.cz3002.shopfunding.Adapter.ProductAdapter;
import com.cz3002.shopfunding.Model.Product;
import com.cz3002.shopfunding.Model.SearchResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {
    RecyclerView rv;
    ProgressBar progressBar;

    FetchProductsTask mFetchProductsTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        super.onCreateDrawer();
        getSupportActionBar().setTitle("Product Search");

        progressBar = findViewById(R.id.progress_fetch_product);

        SearchView sv = (SearchView) findViewById(R.id.searchview);
        sv.setIconifiedByDefault(false);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);

                mFetchProductsTask = new FetchProductsTask(getApplicationContext(), query);
                mFetchProductsTask.execute((Void) null);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });

        rv = (RecyclerView) findViewById(R.id.recyclerview_product);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    class FetchProductsTask extends AsyncTask<Void, Void, ArrayList<SearchResult>> {
        private final Context mContext;
        private final String mKeyword;

        public FetchProductsTask(Context context, String keyword) {
            mContext = context;
            mKeyword = keyword;
        }

        @Override
        protected ArrayList<SearchResult> doInBackground(Void... params) {
            return ProductQuery.getSearchResults(mContext, mKeyword);
        }

        @Override
        protected void onPostExecute(final ArrayList<SearchResult> searchResults) {
            mFetchProductsTask = null;

            if (searchResults != null) {
                final ProductAdapter adapter = new ProductAdapter(searchResults);
                progressBar.setVisibility(View.GONE);
                rv.setAdapter(adapter);
            }
        }
    }
}
