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

public class TeacherHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

//    TextView Tlogout;

    BottomNavigationView bottomTNavigationView;

    HomeTFragment homeTFragment = new HomeTFragment();
    NotificationTFragment notificationTFragment = new NotificationTFragment();
    SettingstTFragment settingstTFragment = new SettingstTFragment();

    DrawerLayout drawerLayoutT;
    NavigationView navigationViewT;
    Toolbar toolbarT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);

        bottomTNavigationView = findViewById(R.id.bottom_Tnavigation);

        drawerLayoutT = findViewById(R.id.drawer_layoutT);
        navigationViewT = findViewById(R.id.nav_viewT);
        toolbarT = findViewById(R.id.toolbarT);

        setSupportActionBar(toolbarT);
        navigationViewT.bringToFront();
        ActionBarDrawerToggle toggle  = new ActionBarDrawerToggle(this, drawerLayoutT, toolbarT, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayoutT.addDrawerListener(toggle);
        toggle.syncState();

        navigationViewT.setNavigationItemSelectedListener(this);
        navigationViewT.setCheckedItem(R.id.nav_viewT);




        BadgeDrawable badgeDrawable = bottomTNavigationView.getOrCreateBadge(R.id.notification);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(8);

        getSupportFragmentManager().beginTransaction().replace(R.id.containerT, homeTFragment).commit();

        bottomTNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item)
            {
                switch(item.getItemId())
                {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerT, homeTFragment).commit();
                        return true;
                    case R.id.notification:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerT, notificationTFragment).commit();
                        return true;
                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerT, settingstTFragment).commit();
                        return true;
                }

                return false;
            }
        });


    }

    @Override
    public void onBackPressed() {


        if(drawerLayoutT.isDrawerOpen(GravityCompat.START))
        {
            drawerLayoutT.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.nav_home_t:
                getSupportFragmentManager().beginTransaction().replace(R.id.containerT, homeTFragment).commit();
                break;
            case R.id.nav_logout_t:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            case R.id.nav_profile_t:
                Intent intent = new Intent(getApplicationContext(), ProfileTActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_viewattendanceT:
                startActivity(new Intent(getApplicationContext(), ViewAttendanceActivity.class));
                break;
            case R.id.nav_aboutusT:
                startActivity(new Intent(getApplicationContext(), AboutUs.class));
                break;
            case R.id.nav_takeattendance:
                startActivity(new Intent(getApplicationContext(), TakeAttedance.class));
                break;
            case R.id.nav_shareT:
                Toast.makeText(this, "Share is Currently unavailable.", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayoutT.closeDrawer(GravityCompat.START);
        return true;
    }
}