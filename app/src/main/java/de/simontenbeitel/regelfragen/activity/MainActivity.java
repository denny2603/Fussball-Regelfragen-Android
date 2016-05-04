package de.simontenbeitel.regelfragen.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.fragment.EinzelfragenRootFragment;
import de.simontenbeitel.regelfragen.fragment.ExamFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private static final String KEY_SELECTED_INDEX = "selected_item";
    @IdRes private int selectedItemId = 0;

    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mHandler = new Handler();
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        @IdRes int menuItemId = R.id.nav_drawer_einzelfragen;
        if (null != savedInstanceState) {
            menuItemId = savedInstanceState.getInt(KEY_SELECTED_INDEX, R.id.nav_drawer_einzelfragen);
        }
        MenuItem menuItem = navigationView.getMenu().findItem(menuItemId);
        onNavigationItemSelected(menuItem);
        menuItem.setChecked(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_SELECTED_INDEX, selectedItemId);
        super.onSaveInstanceState(outState);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (selectedItemId == id) {
            drawer.closeDrawers();
            return true;
        }

        boolean selectItem = false;
        switch (id) {
            case R.id.nav_drawer_einzelfragen:
                replaceContentFragment(new EinzelfragenRootFragment());
                selectedItemId = id;
                selectItem =  true;
                break;
            case R.id.nav_drawer_pruefung:
                replaceContentFragment(new ExamFragment());
                selectedItemId = id;
                selectItem =  true;
                break;
            case R.id.nav_drawer_statistik:

                selectItem =  true;
                break;
            case R.id.nav_drawer_impressum:

                break;
            case R.id.nav_drawer_einstellungen:

                break;
        }
        drawer.closeDrawers();
        return selectItem;
    }

    /**
     * Replaces the content fragment, but delays the operation if drawer is still open to avoid lags
     *
     * @param fragment The {@link Fragment} to be placed on the content container
     */
    protected void replaceContentFragment(final Fragment fragment) {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swapContentFragment(fragment);
                }
            }, 250);
        } else {
            swapContentFragment(fragment);
        }
    }

    private void swapContentFragment(final Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, fragment)
//                .addToBackStack("main_stack")
                .commit();
        invalidateOptionsMenu();
    }

}
