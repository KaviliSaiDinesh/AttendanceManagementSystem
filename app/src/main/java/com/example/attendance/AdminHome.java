package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView Alogout, registerbtn, Anotification, databasetv;

    DrawerLayout drawerLayoutA;
    String url = "https://console.firebase.google.com/project/attendance-72648/overview";
    NavigationView navigationViewA;
    Toolbar toolbarA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);


        drawerLayoutA = findViewById(R.id.drawer_layoutA);
        navigationViewA = findViewById(R.id.nav_viewA);
        toolbarA = findViewById(R.id.tool_bar_A);

        setSupportActionBar(toolbarA);
        navigationViewA.bringToFront();
        ActionBarDrawerToggle toggle  = new ActionBarDrawerToggle(this, drawerLayoutA, toolbarA, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayoutA.addDrawerListener(toggle);
        toggle.syncState();

        navigationViewA.setNavigationItemSelectedListener(this);
        navigationViewA.setCheckedItem(R.id.nav_home);

        Anotification= findViewById(R.id.Anotificationtv);
        Anotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), AdminNotificationActivity.class));

            }
        });

        registerbtn = findViewById(R.id.registertv);
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));

            }
        });

        databasetv = findViewById(R.id.databasetv);
        databasetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

            }
        });

        Alogout = findViewById(R.id.Alogouttv);
        Alogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {


        if(drawerLayoutA.isDrawerOpen(GravityCompat.START))
        {
            drawerLayoutA.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            case R.id.nav_database:

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                break;
            case R.id.nav_sendNotification:
                startActivity(new Intent(getApplicationContext(), AdminNotificationActivity.class));
                break;
            case R.id.nav_aboutus:
                startActivity(new Intent(getApplicationContext(), AboutUs.class));
                break;
            case R.id.nav_register:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share is Currently unavailable.", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayoutA.closeDrawer(GravityCompat.START);
        return true;
    }
}