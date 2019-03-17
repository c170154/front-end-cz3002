package com.cz3002.shopfunding.API;

import android.content.Context;
import com.cz3002.shopfunding.Model.Contribution;
import com.cz3002.shopfunding.Model.FundingRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FundRequest {
    public static int createRequest(
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
        try {
            return json_response.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static FundingRequest fetchFundRequest(Context context, int id) {
        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONObject jsonObject = requestManager.getRequest(context, ENDPOINTS.FUND_REQUEST + id);
        try {
            String author = jsonObject.getJSONObject("author").getString("username");
            String creationDate = jsonObject.getString("creation_date");
            JSONObject product = jsonObject.getJSONObject("product");
            String productName = product.getString("name");
            String description = product.getString("description");
            String url = product.getString("original_url");
            float goal = (float) jsonObject.getDouble("goal");
            float remaining = (float) jsonObject.getDouble("remaining_fund_needed");

            FundingRequest fundingRequest = new FundingRequest(
                    id, author, productName, description, creationDate, url, goal, remaining
            );

            return fundingRequest;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<FundingRequest> fetchFundRequests(Context context) {
        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONArray json_response = requestManager.getRequest_JSONArray(context, ENDPOINTS.FUND_REQUEST + "mine");
        if (json_response != null) {
            ArrayList<FundingRequest> fundRequests = new ArrayList<>();
            try {
                for (int i=0; i < json_response.length(); i++) {
                    JSONObject jsonObject = json_response.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String author = jsonObject.getJSONObject("author").getString("username");
                    String creationDate = jsonObject.getString("creation_date");
                    JSONObject product = jsonObject.getJSONObject("product");
                    String productName = product.getString("name");
                    String description = product.getString("description");
                    String url = product.getString("original_url");
                    float goal = (float) jsonObject.getDouble("goal");
                    float remaining = (float) jsonObject.getDouble("remaining_fund_needed");
                    fundRequests.add(new FundingRequest(
                            id, author, productName, description, creationDate, url, goal, remaining
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return fundRequests;
        }

        return null;
    }

    public static ArrayList<FundingRequest> fetchFriendFundRequests(Context context, int userID) {
        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONArray json_response = requestManager.getRequest_JSONArray(
                context, ENDPOINTS.FUND_REQUEST + userID + ENDPOINTS.FRIEND_FUND_REQUEST);
        if (json_response != null) {
            ArrayList<FundingRequest> fundRequests = new ArrayList<>();
            try {
                for (int i=0; i < json_response.length(); i++) {
                    JSONObject jsonObject = json_response.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String author = jsonObject.getJSONObject("author").getString("username");
                    String creationDate = jsonObject.getString("creation_date");
                    JSONObject product = jsonObject.getJSONObject("product");
                    String productName = product.getString("name");
                    String description = product.getString("description");
                    String url = product.getString("original_url");
                    float goal = (float) jsonObject.getDouble("goal");
                    float remaining = (float) jsonObject.getDouble("remaining_fund_needed");
                    fundRequests.add(new FundingRequest(
                            id, author, productName, description, creationDate, url, goal, remaining
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return fundRequests;
        }

        return null;
    }

    public static boolean addContributors(
            Context context, int requestID, String[] usernames) {
        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        for (String username: usernames) {
            JSONObject payload = new JSONObject();
            try {
                payload.put("username", username);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject json_response = requestManager.postRequest(
                    context, ENDPOINTS.FUND_REQUEST + requestID + ENDPOINTS.ADD_FRIEND, payload);

            try {
                json_response.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public static Contribution[] fetchContributions(Context context, int requestID) {
        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONArray json_response = requestManager.getRequest_JSONArray(
                context, ENDPOINTS.FUND_REQUEST + requestID + ENDPOINTS.CONTRIBUTIONS);
        if (json_response != null) {
            ArrayList<Contribution> contributions = new ArrayList<>();
            try {
                for (int i=0; i < json_response.length(); i++) {
                    JSONObject jsonObject = json_response.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String contributor = jsonObject.getJSONObject("user").getString("username");
                    float amount = (float) jsonObject.getDouble("amount");
                    contributions.add(new Contribution(id, contributor, amount));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return contributions.toArray(new Contribution[contributions.size()]);
        }

        return null;
    }

    public static Float updateContribution(Context context, int contributionID, float amount) {
        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONObject payload = new JSONObject();
        try {
            payload.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject json_response = requestManager.patchRequest(
                context, ENDPOINTS.CONTRIBUTION + contributionID, payload);
        if (json_response != null) {
            try {
                return (float) json_response.getDouble("amount");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
