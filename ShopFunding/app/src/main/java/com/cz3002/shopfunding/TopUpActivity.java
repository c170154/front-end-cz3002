package com.cz3002.shopfunding;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.cz3002.shopfunding.API.User;
import com.cz3002.shopfunding.Helper.FetchBalanceTask;
import com.cz3002.shopfunding.Model.UserProfile;

public class TopUpActivity extends BaseActivity {
    private GetBalanceTask mAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);
        super.onCreateDrawer();
        getSupportActionBar().setTitle("Top Up");

        mAsyncTask = new GetBalanceTask(getApplicationContext(), this.userProfile);
        mAsyncTask.execute((Void) null);
    }

    private class GetBalanceTask extends FetchBalanceTask {
        GetBalanceTask(Context context, UserProfile userProfile) {
            super(context, userProfile, User.getJWTToken(context));
        }

        @Override
        protected void onPostExecute(final Float balance) {
            mAsyncTask = null;
            TextView balance_display = TopUpActivity.this.findViewById(R.id.balance);
            balance_display.setText(String.format("%.2f", balance));
        }

        @Override
        protected void onCancelled() {
            mAsyncTask = null;
        }
    }
}
