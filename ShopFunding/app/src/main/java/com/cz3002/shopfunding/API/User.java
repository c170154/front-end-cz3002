package com.cz3002.shopfunding.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

interface ENDPOINTS {
    String BASE_URL = "http://10.0.2.2:8000/user/";
    String VERIFY_TOKEN = BASE_URL + "verify-token/";
    String GET_TOKEN = BASE_URL + "obtain-token/";
    String CREATE_USER = BASE_URL;
}

public class User {
    private static String PREF_NAME = "APP_PREFS";
    private static String JWT_PREF_NAME = "JWT_TOKEN";

    public static void storeJWTToken(Context context, String jwt_token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(JWT_PREF_NAME, jwt_token);
        editor.apply();
    }

    public static void removeJWTToken(Context context) {
        storeJWTToken(context, null);
    }

    public static boolean checkLoginStatus(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String jwt_token = prefs.getString(JWT_PREF_NAME, null);

        if (jwt_token != null) {
            RequestManager requestManager = RequestManager.getInstance(context.getApplicationContext());
            JSONObject payload = new JSONObject();
            try {
                payload.put("token", jwt_token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject json_response = requestManager.postRequest(ENDPOINTS.VERIFY_TOKEN, payload);
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

        JSONObject json_response = requestManager.postRequest(ENDPOINTS.GET_TOKEN, payload);
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

        JSONObject json_response = requestManager.postRequest(ENDPOINTS.CREATE_USER, payload);
        return json_response != null && !json_response.isNull("id");
    }
}
