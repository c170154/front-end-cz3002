package com.cz3002.shopfunding;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.cz3002.shopfunding.API.User;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RedirectTask redirectTask = new RedirectTask();
        redirectTask.execute((Void) null);
    }

    private class RedirectTask extends AsyncTask<Void, Void, Boolean> {
        RedirectTask() {}

        @Override
        protected Boolean doInBackground(Void... params) {
            return User.checkLoginStatus(SplashActivity.this);
        }

        @Override
        protected void onPostExecute(final Boolean isLoggedin) {
            Intent activityIntent;

            if (isLoggedin) {
                activityIntent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                activityIntent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            startActivity(activityIntent);
            SplashActivity.this.finish();
        }
    }
}
