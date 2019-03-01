package com.cz3002.shopfunding.API;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

interface ENDPOINTS {
    String LOGIN_BASE_URL = "http://10.0.2.2:8000/user/";
    String CREATE_USER = LOGIN_BASE_URL;
    String VERIFY_TOKEN = LOGIN_BASE_URL + "verify-token/";
    String GET_TOKEN = LOGIN_BASE_URL + "obtain-token/";

    String BACKEND_BASE_URL = "http://10.0.2.2:5000/";
    String GET_USER_PROFILE = BACKEND_BASE_URL + "user/get_profile/";
    String TOP_UP = BACKEND_BASE_URL + "user/top_up/";
    String FUND_REQUEST = BACKEND_BASE_URL + "fundrequest/";
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

    JSONObject postRequest(String url, JSONObject payload, final String jwt_token) {
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, payload, requestFuture, requestFuture)
        {
            /** Pass in authorization headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
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

    JSONObject getRequest(String url, final String jwt_token) {
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, requestFuture, requestFuture)
        {
            /** Pass in authorization headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
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

    JSONArray getRequest_JSONArray(String url, final String jwt_token) {
        RequestFuture<JSONArray> requestFuture = RequestFuture.newFuture();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, requestFuture, requestFuture)
        {
            /** Pass in authorization headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
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
}
