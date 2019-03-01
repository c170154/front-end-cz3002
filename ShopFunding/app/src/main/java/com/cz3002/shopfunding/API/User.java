package com.cz3002.shopfunding.API;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;

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
            JSONObject json_response = requestManager.postRequest(ENDPOINTS.VERIFY_TOKEN, payload, null);
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

        JSONObject json_response = requestManager.postRequest(ENDPOINTS.GET_TOKEN, payload, null);
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

        JSONObject json_response = requestManager.postRequest(ENDPOINTS.CREATE_USER, payload, null);
        return json_response != null && !json_response.isNull("id");
    }

    public static Float fetch_balance(Context context, int user_id, String jwt_token) {
        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONObject json_response = requestManager.getRequest(ENDPOINTS.GET_USER_PROFILE + user_id, jwt_token);
        if (json_response != null) {
            try {
                return (float) json_response.getDouble("balance");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Float top_up(Context context, int user_id, int amount, String jwt_token) {
        RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
        JSONObject payload = new JSONObject();
        try {
            payload.put("user_id", user_id);
            payload.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json_response = requestManager.postRequest(ENDPOINTS.TOP_UP, payload, jwt_token);
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
