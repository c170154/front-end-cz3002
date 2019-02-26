package com.cz3002.shopfunding.Helper;

import android.content.Context;
import android.os.AsyncTask;
import com.cz3002.shopfunding.API.User;
import com.cz3002.shopfunding.Model.UserProfile;

/**
 * Represents an asynchronous database read task used to fetch user's balance.
 */
public class FetchBalanceTask extends AsyncTask<Void, Void, Float> {
    private final Context mContext;
    private final int mUserID;
    private String jwt_token;

    public FetchBalanceTask(Context context, UserProfile userProfile, String jwt_token) {
        mUserID = userProfile.get_user_id();
        mContext = context;
        this.jwt_token = jwt_token;
    }

    @Override
    protected Float doInBackground(Void... params) {
        return User.fetch_balance(mContext, mUserID, jwt_token);
    }
}
