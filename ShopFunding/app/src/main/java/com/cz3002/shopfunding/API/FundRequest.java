package com.cz3002.shopfunding.API;

import android.content.Context;
import android.util.Log;
import com.cz3002.shopfunding.Model.FundingRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FundRequest {
    public static boolean createRequest(
            Context context, String productName, String description, String url, float goal
    ) {
        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONObject product = new JSONObject();
        try {
            product.put("name", productName);
            product.put("description", description);
            product.put("original_url", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject payload = new JSONObject();
        try {
            payload.put("product", product);
            payload.put("goal", goal);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json_response = requestManager.postRequest(context, ENDPOINTS.FUND_REQUEST, payload);
        return json_response != null && !json_response.isNull("name");
    }

    public static ArrayList<FundingRequest> fetchFundRequests(Context context) {
        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONArray json_response = requestManager.getRequest_JSONArray(context, ENDPOINTS.FUND_REQUEST);
        if (json_response != null) {
            ArrayList<FundingRequest> fundRequests = new ArrayList<>();
            try {
                for (int i=0; i < json_response.length(); i++) {
                    JSONObject jsonObject = json_response.getJSONObject(i);
                    String creationDate = jsonObject.getString("creation_date");
                    JSONObject product = jsonObject.getJSONObject("product");
                    String productName = product.getString("name");
                    String description = product.getString("description");
                    String url = product.getString("original_url");
                    float goal = (float) jsonObject.getDouble("goal");
                    fundRequests.add(new FundingRequest(productName, description, creationDate, url, goal));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return fundRequests;
        }

        return null;
    }
}
