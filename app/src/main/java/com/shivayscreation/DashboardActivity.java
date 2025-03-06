package com.shivayscreation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    BottomNavigationView mBottomNavigation;

    int HOME_MENU = R.id.nav_home;
    int WISHLIST_MENU = R.id.nav_wishlist;
    int CART_MENU = R.id.nav_cart;
    int PROFILE_MENU = R.id.nav_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mBottomNavigation = findViewById(R.id.dashboard_bottom);

        // Use the new method to set a listener for item clicks
        mBottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            // Replacing the switch with if-else to avoid 'constant expression required' issue
            if (item.getItemId() == HOME_MENU) {
                fragment = new HomeFragment();
            } else if (item.getItemId() == CART_MENU) {
                CartFragment.iTotalPrice = 0;
                fragment = new CartFragment();
            } else if (item.getItemId() == WISHLIST_MENU) {
                fragment = new WishlistFragment();
            } else if (item.getItemId() == PROFILE_MENU) {
                fragment = new ProfileFragment();
            }

            if (fragment != null) {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.dashboard_relative, fragment).commit();
            }
            return true;
        });

        // Load the HomeFragment initially if savedInstanceState is null
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dashboard_relative, new HomeFragment())
                    .commit();
            mBottomNavigation.setSelectedItemId(HOME_MENU); // Set the initial selection
        }
    }
}
