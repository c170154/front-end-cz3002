package com.cz3002.shopfunding.Helper;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;
import com.alexzh.circleimageview.CircleImageView;
import com.alexzh.circleimageview.ItemSelectedListener;
import com.cz3002.shopfunding.*;
import com.cz3002.shopfunding.API.User;
import com.cz3002.shopfunding.Model.UserProfile;

public class SideNavigation {
    SideNavigation() {}

    public static void init(final Activity activity, final DrawerLayout drawerLayout, UserProfile userProfile) {
        TextView username_display = activity.findViewById(R.id.nav_username);
        username_display.setText(userProfile.get_username());

        CircleImageView UserImage = activity.findViewById(R.id.user_img);
        UserImage.setOnItemSelectedClickListener(new ItemSelectedListener() {
            @Override
            public void onSelected(View view) {
                // Do nothing
            }

            @Override
            public void onUnselected(View view) {
                // Do nothing
            }
        });

        TextView Home = activity.findViewById(R.id.menu_home);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(activity instanceof MainActivity)) {
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                } else {
                    drawerLayout.closeDrawers();
                }
            }
        });

        TextView FriendList = activity.findViewById(R.id.menu_friend);
        FriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(activity instanceof FriendListActivity)) {
                    activity.startActivity(new Intent(activity, FriendListActivity.class));
                    activity.finish();
                } else {
                    drawerLayout.closeDrawers();
                }
            }
        });

        TextView TopUp = activity.findViewById(R.id.menu_topup);
        TopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(activity instanceof TopUpActivity)) {
                    activity.startActivity(new Intent(activity, TopUpActivity.class));
                    activity.finish();
                } else {
                    drawerLayout.closeDrawers();
                }
            }
        });

        TextView TransactionHistory = activity.findViewById(R.id.menu_transaction);
        TransactionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(activity instanceof TransactionActivity)) {
                    activity.startActivity(new Intent(activity, TransactionActivity.class));
                    activity.finish();
                } else {
                    drawerLayout.closeDrawers();
                }
            }
        });

        TextView About = activity.findViewById(R.id.menu_about);
        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        TextView Logout = activity.findViewById(R.id.menu_logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, LoginActivity.class));
                User.removeJWTToken(activity.getApplicationContext());
                activity.finish();
            }
        });
    }
}
