package com.cz3002.shopfunding.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.cz3002.shopfunding.FundRequestActivity;
import com.cz3002.shopfunding.FundRequestDetailActivity;
import com.cz3002.shopfunding.MainActivity;
import com.cz3002.shopfunding.Model.Product;
import com.cz3002.shopfunding.Model.SearchResult;
import com.cz3002.shopfunding.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    ArrayList<SearchResult> mProducts;

    public ProductAdapter(ArrayList<SearchResult> products)
    {
        mProducts = products;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private CardView product;
        private TextView productName;
        private ImageView imageView;

        private Context context;

        public ProductViewHolder(View v) {
            super(v);
            this.product = v.findViewById(R.id.card_product);
            this.productName = v.findViewById(R.id.tv_product_name);
            this.imageView = v.findViewById(R.id.image_product);

            this.context = v.getContext();
        }
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_product, null);
        ProductViewHolder holder = new ProductViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final SearchResult product = mProducts.get(position);
        product.getImageLinks().get(0);
        holder.productName.setText(product.getName());
        Picasso.get()
                .load(product.getImageLinks().get(0))
                .placeholder(R.drawable.progress_animation)
                .fit()
                .centerInside()
                .into(holder.imageView);

        // onClick handler to navigate to contribute activity
        holder.product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityIntent = new Intent(holder.context, FundRequestActivity.class);
                activityIntent.putExtra("item_id", product.getItemId());
                activityIntent.putExtra("shop_id", product.getShopId());
                holder.context.startActivity(activityIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }
}
