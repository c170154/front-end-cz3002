package com.cz3002.shopfunding.Helper;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import com.cz3002.shopfunding.API.User;
import com.cz3002.shopfunding.LoginActivity;
import com.cz3002.shopfunding.MainActivity;
import com.cz3002.shopfunding.R;

public class SideNavigation {
    SideNavigation() {}

    public static void init(final Activity activity) {
        TextView home = activity.findViewById(R.id.menu_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        TextView about = activity.findViewById(R.id.menu_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        TextView logout = activity.findViewById(R.id.menu_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, LoginActivity.class));
                User.removeJWTToken(activity.getApplicationContext());
                activity.finish();
            }
        });
    }
}
