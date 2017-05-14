package com.acterics.healthmonitor.ui;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.ui.drawerfragments.CardioMonitorFragment;
import com.acterics.healthmonitor.ui.drawerfragments.GeneralFragment;
import com.acterics.healthmonitor.ui.drawerfragments.IssuesFragment;
import com.acterics.healthmonitor.ui.drawerfragments.SettingsFragment;
import com.acterics.healthmonitor.utils.NavigationUtils;
import com.acterics.healthmonitor.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    private TextView tvName;

    //TODO add sync with drawer items and current fragments
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("onCreate");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tvName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.holder_content, new GeneralFragment())
                    .commit();
        }
        tvName.setText(PreferenceUtils.getUserName(getApplicationContext()));
    }

    @Override
    public void onBackPressed() {
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.id.drawer_general:
                transaction.replace(R.id.holder_content, new GeneralFragment());
                break;
            case R.id.drawer_cardio_monitor:
                transaction.replace(R.id.holder_content, new CardioMonitorFragment());
                break;
            case R.id.drawer_settings:
                transaction.replace(R.id.holder_content, new SettingsFragment());
                break;
            case R.id.drawer_issues:
                transaction.replace(R.id.holder_content, new IssuesFragment());
                break;
            case R.id.nav_share:
            case R.id.nav_send:
                break;
            case R.id.drawer_exit:
                PreferenceUtils.clearPreference(getApplicationContext());
                NavigationUtils.toAuthorization(this, false);
                finish();
                break;
        }
        transaction.commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
