package com.cz3002.shopfunding.API;

import android.content.Context;
import android.content.SharedPreferences;
import com.cz3002.shopfunding.Model.UserProfile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {
    private static String PREF_NAME = "APP_PREFS";
    private static String JWT_PREF_NAME = "JWT_TOKEN";

    public static void storeJWTToken(Context context, String jwt_token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(JWT_PREF_NAME, jwt_token);
        editor.apply();
    }

    public static String getJWTToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return prefs.getString(JWT_PREF_NAME, null);
    }

    public static void removeJWTToken(Context context) {
        storeJWTToken(context, null);
    }

    public static boolean checkLoginStatus(Context context) {
        String jwt_token = getJWTToken(context);

        if (jwt_token != null) {
            RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
            JSONObject payload = new JSONObject();
            try {
                payload.put("token", jwt_token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject json_response = requestManager.postRequest(context, ENDPOINTS.VERIFY_TOKEN, payload);
            return json_response != null && !json_response.isNull("token");
        }

        return false;
    }

    public static String login(Context context, String username, String password) {
        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONObject payload = new JSONObject();
        try {
            payload.put("username", username);
            payload.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json_response = requestManager.postRequest(context, ENDPOINTS.GET_TOKEN, payload);
        if (json_response != null && !json_response.isNull("token")) {
            try {
                return json_response.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static boolean register(Context context, String username, String password) {
        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONObject payload = new JSONObject();
        try {
            payload.put("username", username);
            payload.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json_response = requestManager.postRequest(context, ENDPOINTS.CREATE_USER, payload);
        try {
            requestManager.getRequest(context, ENDPOINTS.GET_USER_PROFILE + json_response.getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json_response != null && !json_response.isNull("id");
    }

    public static ArrayList<String> fetchFriendList(Context context, int user_id) {
        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONObject json_response = requestManager.getRequest(context, ENDPOINTS.GET_USER_PROFILE + user_id);
        if (json_response != null) {
            ArrayList<String> friendList = new ArrayList<>();
            JSONArray jsonArray = null;
            try {
                jsonArray = json_response.getJSONArray("friends");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            assert jsonArray != null;
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    friendList.add(jsonObject.getString("username"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return friendList;
        }

        return null;
    }

    public static UserProfile addFriend(Context context, int userID, String friendUsername) {
        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONObject payload = new JSONObject();
        try {
            payload.put("username", friendUsername);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = ENDPOINTS.BACKEND_BASE_URL + "user/" + userID + ENDPOINTS.ADD_FRIEND;
        JSONObject json_response = requestManager.postRequest(context, url, payload);
        if (json_response != null) {
            try {
                return new UserProfile(json_response.getInt("id"), json_response.getString("username"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Float fetch_balance(Context context, int user_id) {
        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONObject json_response = requestManager.getRequest(context, ENDPOINTS.GET_USER_PROFILE + user_id);
        if (json_response != null) {
            try {
                return (float) json_response.getDouble("balance");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Float top_up(Context context, int user_id, int amount) {
        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONObject payload = new JSONObject();
        try {
            payload.put("user_id", user_id);
            payload.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json_response = requestManager.postRequest(context, ENDPOINTS.TOP_UP, payload);
        if (json_response != null) {
            try {
                return (float) json_response.getDouble("balance");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
