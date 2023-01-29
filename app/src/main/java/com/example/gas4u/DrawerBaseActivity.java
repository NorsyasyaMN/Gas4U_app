package com.example.gas4u;

import androidx.annotation.NonNull;
        import androidx.appcompat.app.ActionBarDrawerToggle;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.view.MenuItem;
        import android.view.View;
        import android.widget.FrameLayout;

        import com.google.android.material.navigation.NavigationView;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_drawer_open, R.string.menu_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()){
            case R.id.nav_brand:
                startActivity(new Intent(this, BrandActivity.class));
                overridePendingTransition(0,0);
                break;
            case R.id.nav_track_order:
                startActivity(new Intent(this, UserTrackOrder.class));
                overridePendingTransition(0,0);
                break;
            case R.id.nav_shopping_cart:
                startActivity(new Intent(this, CartActivity.class));
                overridePendingTransition(0,0);
                break;
            case R.id.nav_user_profile:
                startActivity(new Intent(this, CustomerProfileActivity.class));
                overridePendingTransition(0,0);
                break;
            case R.id.nav_helpline:
                startActivity(new Intent(this, ViewHelplineActivity.class));
                overridePendingTransition(0,0);
                break;
            case R.id.nav_logout:
                startActivity(new Intent(this, LogoutActivity.class));
                break;
        }
        return false;
    }

    protected void allocateActivityTitle(String titleString){
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(titleString);
        }
    }
}