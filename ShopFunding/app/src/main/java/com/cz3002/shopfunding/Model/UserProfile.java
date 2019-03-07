package com.cz3002.shopfunding.Model;

import android.content.Context;
import android.util.Base64;
import com.cz3002.shopfunding.API.User;
import org.json.JSONException;
import org.json.JSONObject;

public class UserProfile {
    private int user_id;
    private String username;

    public UserProfile(Context context) {
        String jwt_token = User.getJWTToken(context);
        JSONObject jwt_body = decode_jwt(jwt_token);
        if (jwt_body != null) {
            try {
                this.user_id = jwt_body.getInt("user_id");
                this.username = jwt_body.getString("username");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public UserProfile(int id, String username) {
        this.user_id = id;
        this.username = username;
    }

    private static JSONObject decode_jwt(String jwt_token) {
        String base64EncodedBody = jwt_token.split("\\.")[1];
        byte[] decodedBytes = Base64.decode(base64EncodedBody, Base64.URL_SAFE);
        try {
            return new JSONObject(new String(decodedBytes));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int get_user_id() {
        return this.user_id;
    }

    public String get_username() {
        return this.username;
    }
}

