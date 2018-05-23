package com.vasistha.bankingsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vasistha.bankingsystem.Activities.LoginActivity;
import com.vasistha.bankingsystem.fragments.Home;
import com.vasistha.bankingsystem.fragments.Feedback;
import com.vasistha.bankingsystem.fragments.MyProfile;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    public String UserID;
    public String UserEmailID;

    //Firebase authorization object.
    public FirebaseAuth firebaseAuth;
    public FirebaseUser firebaseUser;

    Intent intent;
    AlertDialog signOutDialog;

    android.support.v4.app.Fragment fragment = null;
    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        UserID = firebaseUser.getUid();
        UserEmailID = firebaseUser.getEmail();

        intent = new Intent(this,LoginActivity.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Sign Out!");
        alertDialogBuilder.setMessage("Are you sure you want to Sign Out?").setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                firebaseAuth.signOut();
                finish();
                startActivity(intent);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        signOutDialog = alertDialogBuilder.create();

        fragment = new Home();
        fragmentManager.beginTransaction().replace(R.id.mainContent,fragment).commit();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(firebaseAuth.getCurrentUser() == null)
        {
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener()
        {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                if(fragment != null)
                {
                    fragmentManager.beginTransaction().replace(R.id.mainContent,fragment).commit();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if(id == R.id.nav_profile)
        {
            fragment = new MyProfile();
        }
        else if(id == R.id.nav_home)
        {
            fragment = new Home();
        }
        else if(id == R.id.nav_feedback)
        {
            fragment = new Feedback();
        }
        else if(id == R.id.nav_signout)
        {
            signOutDialog.show();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
