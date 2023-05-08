package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Studenthome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomSNavigationView;

    HomeSFragment homeSFragment = new HomeSFragment();
    NotificationSFragment notificationSFragment = new NotificationSFragment();
    SettingsSFragment settingsSFragment = new SettingsSFragment();

    DrawerLayout drawerLayoutS;
    NavigationView navigationViewS;
    Toolbar toolbarS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studenthome);


        bottomSNavigationView = findViewById(R.id.bottom_Snavigation);

        drawerLayoutS = findViewById(R.id.drawer_layoutS);
        navigationViewS = findViewById(R.id.nav_viewS);
        toolbarS = findViewById(R.id.tool_bar_S);

        setSupportActionBar(toolbarS);
//        navigationViewS.bringToFront();
        ActionBarDrawerToggle toggle  = new ActionBarDrawerToggle(this, drawerLayoutS, toolbarS, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayoutS.addDrawerListener(toggle);
        toggle.syncState();
        navigationViewS.bringToFront();
        navigationViewS.setNavigationItemSelectedListener(this);
        navigationViewS.setCheckedItem(R.id.nav_viewS);


        BadgeDrawable badgeDrawable = bottomSNavigationView.getOrCreateBadge(R.id.Snotification);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(7);

        getSupportFragmentManager().beginTransaction().replace(R.id.containerS, homeSFragment).commit();


        bottomSNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item)
            {
                switch(item.getItemId())
                {
                    case R.id.Shome:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerS, homeSFragment).commit();
                        return true;
                    case R.id.Snotification:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerS, notificationSFragment).commit();
                        return true;
                    case R.id.Ssettings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerS, settingsSFragment).commit();
                        return true;
                }

                return false;
            }
        });


    }

    @Override
    public void onBackPressed() {


        if(drawerLayoutS.isDrawerOpen(GravityCompat.START))
        {
            drawerLayoutS.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.nav_home_s:
                getSupportFragmentManager().beginTransaction().replace(R.id.containerS, homeSFragment).commit();
                break;
            case R.id.nav_logout_s:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            case R.id.nav_profile_s:

                Intent intent = new Intent(getApplicationContext(), ProfileSActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_viewattendanceS:
                startActivity(new Intent(getApplicationContext(), ViewAttendanceS.class));
                break;
            case R.id.nav_aboutusS:
                startActivity(new Intent(getApplicationContext(), AboutUs.class));
                break;
            case R.id.nav_bus:
                startActivity(new Intent(getApplicationContext(), DepInfo.class));
                break;
            case R.id.nav_shareS:
                Toast.makeText(this, "Share is Currently unavailable.", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayoutS.closeDrawer(GravityCompat.START);
        return true;
    }
}