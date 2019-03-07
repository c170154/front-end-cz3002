package com.cz3002.shopfunding;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.cz3002.shopfunding.API.User;
import com.cz3002.shopfunding.Model.UserProfile;

import static java.lang.Integer.parseInt;

public class TopUpActivity extends BaseActivity {
    private GetBalanceTask mGetBalanceTask;
    private TopUpTask mTopUpTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);
        super.onCreateDrawer();
        getSupportActionBar().setTitle("Top Up");

        final Button topup = findViewById(R.id.btn_topup);
        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = ((EditText) findViewById(R.id.input_amount)).getText().toString();
                if (!input.equals("")) {
                    int amount = parseInt(input);
                    mTopUpTask = new TopUpTask(getApplicationContext(), userProfile, amount);
                    mTopUpTask.execute((Void) null);
                    topup.setClickable(false);
                }
            }
        });

        mGetBalanceTask = new GetBalanceTask(getApplicationContext(), this.userProfile);
        mGetBalanceTask.execute((Void) null);
    }

    private class GetBalanceTask extends AsyncTask<Void, Void, Float> {
        private Context mContext;
        private int mUserID;

        GetBalanceTask(Context context, UserProfile userProfile) {
            mContext = context;
            mUserID = userProfile.get_user_id();
        }

        @Override
        protected Float doInBackground(Void... params) {
            return User.fetch_balance(mContext, mUserID);
        }

        @Override
        protected void onPostExecute(final Float balance) {
            mGetBalanceTask = null;

            TextView balance_display = TopUpActivity.this.findViewById(R.id.tv_balance);
            balance_display.setText(String.format("S$%.2f", balance));
        }
    }

    public class TopUpTask extends AsyncTask<Void, Void, Float> {
        private final Context mContext;
        private final int mUserID;
        private final int mAmount;

        public TopUpTask(Context context, UserProfile userProfile, int amount) {
            mUserID = userProfile.get_user_id();
            mContext = context;
            mAmount = amount;
        }

        @Override
        protected Float doInBackground(Void... params) {
            return User.top_up(mContext, mUserID, mAmount);
        }

        @Override
        protected void onPostExecute(final Float balance) {
            mTopUpTask = null;
            Toast.makeText(TopUpActivity.this, "Top up successfully!", Toast.LENGTH_SHORT).show();

            TextView balance_display = TopUpActivity.this.findViewById(R.id.tv_balance);
            balance_display.setText(String.format("S$%.2f", balance));

            final Button topup = findViewById(R.id.btn_topup);
            topup.setClickable(true);
        }
    }
}
