package com.cz3002.shopfunding;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.cz3002.shopfunding.API.FundRequest;
import com.cz3002.shopfunding.API.User;
import com.cz3002.shopfunding.Model.UserProfile;

import static java.lang.Integer.parseInt;

public class FundRequestActivity extends BaseActivity {
    private CreateRequestTask mCreateRequestTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_request);
        super.onCreateDrawer();
        getSupportActionBar().setTitle("Create Fund Request");

        final Button create = findViewById(R.id.button_create_request);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = ((TextView) findViewById(R.id.tv_product_name)).getText().toString();
                String description = ((TextView) findViewById(R.id.tv_product_description)).getText().toString();
                // TODO: get url from product page
                String url = "www.randomurl.com";
                String input = ((EditText) findViewById(R.id.input_quantity)).getText().toString();
                if (!input.equals("")) {
                    int quantity = parseInt(input);
                    float goal = quantity * 120;
                    mCreateRequestTask = new CreateRequestTask(getApplicationContext(), userProfile,
                            productName, description,url, goal);
                    mCreateRequestTask.execute((Void) null);
                } else {
                    // TODO: invalidate text field
                }
            }
        });
    }

    // Async tasks for API call
    public class CreateRequestTask extends AsyncTask<Void, Void, Boolean> {
        private final Context mContext;
        private final int mUserID;
        private final String mProductName;
        private final String mDescription;
        private final String mUrl;
        private final float mGoal;

        private String jwt_token;

        public CreateRequestTask(Context context, UserProfile userProfile,
                         String productName, String description, String url, float goal) {
            mUserID = userProfile.get_user_id();
            mContext = context;
            mProductName = productName;
            mDescription = description;
            mUrl = url;
            mGoal = goal;

            this.jwt_token = User.getJWTToken(context);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return FundRequest.createRequest(mContext, mProductName, mDescription, mUrl, mGoal, jwt_token);
        }

        @Override
        protected void onPostExecute(final Boolean isSuccessful) {
            if (isSuccessful) {
                Toast.makeText(FundRequestActivity.this, "Request created successfully!", Toast.LENGTH_SHORT).show();

            }
            mCreateRequestTask = null;
            Intent activityIntent = new Intent(FundRequestActivity.this, MainActivity.class);
            startActivity(activityIntent);
            FundRequestActivity.this.finish();
        }

        @Override
        protected void onCancelled() {
            mCreateRequestTask = null;
        }
    }
}
