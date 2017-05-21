package com.acterics.healthmonitor.ui;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.base.BaseCallback;
import com.acterics.healthmonitor.base.BaseFragment;
import com.acterics.healthmonitor.data.RestClient;
import com.acterics.healthmonitor.data.models.UserModel;
import com.acterics.healthmonitor.ui.drawerfragments.GeneralFragment;
import com.acterics.healthmonitor.ui.drawerfragments.complaint.ComplaintFragment;
import com.acterics.healthmonitor.utils.NavigationUtils;
import com.acterics.healthmonitor.utils.PreferenceUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String FRAGMENT_TAG = "com.acterics.healthmonitor.ui.FRAGMENT_TAG";

    @BindView(R.id.toolbar) Toolbar toolbar;
    private TextView tvName;
    private ImageView ivAvatar;

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
        ivAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_avatar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.holder_content, new GeneralFragment(), FRAGMENT_TAG)
                    .commit();
        }

        RestClient.getApiService()
                .getUser(PreferenceUtils.getRequestUserToken(this))
                .enqueue(new BaseCallback<UserModel>(this) {
                    @Override
                    public void onSuccess(@NonNull UserModel body) {
                        PreferenceUtils.saveUserInfo(context, body);
                        onDataLoaded(body);
                    }
                });



    }

    private void onDataLoaded(UserModel body) {
        tvName.setText(String.format("%s %s", body.getFirstName(), body.getLastName()));
        Glide.with(this)
                .load(body.getAvatar())
                .centerCrop()
                .into(ivAvatar);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            BaseFragment baseFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
            if (!baseFragment.onBackPressed()) {
                super.onBackPressed();
            }
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
        Fragment fragment = new Fragment();
        switch (id) {
            case R.id.drawer_general:
                fragment = new GeneralFragment();
                break;
            case R.id.drawer_complaint:
                fragment = new ComplaintFragment();
                break;
            case R.id.drawer_exit:
                PreferenceUtils.clearPreference(getApplicationContext());
                NavigationUtils.toAuthorization(this);
                finish();
                break;
        }
        transaction.replace(R.id.holder_content, fragment, FRAGMENT_TAG);
        transaction.commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
