package com.cz3002.shopfunding.API;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

interface ENDPOINTS {
    String LOGIN_BASE_URL = "http://172.21.147.36:8001/user/";
    String CREATE_USER = LOGIN_BASE_URL;
    String VERIFY_TOKEN = LOGIN_BASE_URL + "verify-token/";
    String GET_TOKEN = LOGIN_BASE_URL + "obtain-token/";

    String BACKEND_BASE_URL = "http://172.21.147.36:8002/";
    String ADD_FRIEND = "/add_friend/";
    String GET_USER_PROFILE = BACKEND_BASE_URL + "user/get_profile/";
    String TOP_UP = BACKEND_BASE_URL + "user/top_up/";
    String FUND_REQUEST = BACKEND_BASE_URL + "fundrequest/";
    String CONTRIBUTIONS = "/contributions/";
    String FRIEND_FUND_REQUEST = "/get_friends_fund_request";
    String CONTRIBUTION = FUND_REQUEST + "contributions/";

    String PRODUCT_QUERY = "product_query/";
    String GET_SHOPEE_CAROUSEL = BACKEND_BASE_URL + PRODUCT_QUERY + "ShopeeCarousel";
    String GET_SHOPEE_PRODUCT = BACKEND_BASE_URL + PRODUCT_QUERY + "ShopeeProduct";
    String GET_SHOPEE_SEARCH_RESULTS = BACKEND_BASE_URL + PRODUCT_QUERY + "ShopeeSearchResults";
}

public class RequestManager {
    private static RequestManager mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Context mCtx;

    private RequestManager(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    static synchronized RequestManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestManager(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    private <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    JSONObject postRequest(final Context context, String url, JSONObject payload) {
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, payload, requestFuture, requestFuture)
        {
            /** Pass in authorization headers* */
            String jwt_token = User.getJWTToken(context);
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "");
                if (jwt_token != null) {
                    headers.put("Authorization", "JWT " + jwt_token);
                }
                return headers;
            }
        };
        addToRequestQueue(request);
        try {
            return requestFuture.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

    JSONObject getRequest(final Context context, String url) {
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, requestFuture, requestFuture)
        {
            /** Pass in authorization headers* */
            String jwt_token = User.getJWTToken(context);
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "");
                if (jwt_token != null) {
                    headers.put("Authorization", "JWT " + jwt_token);
                }
                return headers;
            }
        };
        addToRequestQueue(request);
        try {
            return requestFuture.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

    JSONArray getRequest_JSONArray(final Context context, String url) {
        RequestFuture<JSONArray> requestFuture = RequestFuture.newFuture();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, requestFuture, requestFuture)
        {
            /** Pass in authorization headers* */
            String jwt_token = User.getJWTToken(context);
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "JWT " + jwt_token);
                return headers;
            }
        };
        addToRequestQueue(request);
        try {
            return requestFuture.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

    JSONObject patchRequest(final Context context, String url, JSONObject payload) {
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PATCH, url, payload, requestFuture, requestFuture)
        {
            /** Pass in authorization headers* */
            String jwt_token = User.getJWTToken(context);
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "");
                if (jwt_token != null) {
                    headers.put("Authorization", "JWT " + jwt_token);
                }
                return headers;
            }
        };
        addToRequestQueue(request);
        try {
            return requestFuture.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }
}
