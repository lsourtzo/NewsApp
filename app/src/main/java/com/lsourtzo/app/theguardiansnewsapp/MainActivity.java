package com.lsourtzo.app.theguardiansnewsapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    FragmentManager fragmentManager;
    ProgressBar loadingIndicator;

    String checkViewVisibility;

    //this text gona help us keep the search word
    SearchView searchView;
    String queryWord = "";
    // the default url ...
    // this cant be static ... it's gonna change adding more searching preferences
    String finalUrl = "https://content.guardianapis.com/search?q=&page-size=30&show-fields=thumbnail&api-key=6089f2c9-7906-4836-bbf2-a9b6cab55f02";
    String category = "";

    //Save Instance State to save the current search options on rotation.
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // Save custom values into the bundle
        savedInstanceState.putString("finalUrlS", finalUrl);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    //Restore Instance State to restore the current search options on rotation.
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        finalUrl = savedInstanceState.getString("finalUrlS");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // changing ActionBar Fonts ...
        // from custom font in assets/font folder
        TextView toolbarText = (TextView) toolbar.getChildAt(0);
        Typeface fonttype = Typeface.createFromAsset(getAssets(), "fonts/Merriweather-Bold.ttf");
        toolbarText.setTypeface(fonttype);


        //Drawer Options
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.getChildAt(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //at first time the app open
        if (savedInstanceState == null) {
            //call fragment
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = Fragment_Activity.class;
            Bundle bundle = new Bundle();
            bundle.putString("finalUrl", finalUrl);
            bundle.putInt("Page", 1);
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace(); }

            // set Fragmentclass Arguments
            fragment.setArguments(bundle);
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            try {
                loadingIndicator.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // return if the detail view is on screan
    public void isViewVisible(String isVV) {
        checkViewVisibility = isVV;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //If drawable is opes close it ...
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (checkViewVisibility == "false") {
                if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                    super.onBackPressed();
                    finish();
                    return;
                } else {
                    Toast.makeText(getBaseContext(), R.string.doubleClick, Toast.LENGTH_SHORT).show();
                }
                mBackPressed = System.currentTimeMillis();
            } else {
                // call close view from fragment activity
                Fragment_Activity frag = (Fragment_Activity) fragmentManager.findFragmentById(R.id.flContent);
                frag.closeView();
                //set visibility back to false because I just close the view
                checkViewVisibility = "false";
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryWord = query;
                applyNewUrl();
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        category = item.getTitle().toString();
        queryWord = "";

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        applyNewUrl();

        return true;
    }

    public void applyNewUrl() {
        Class fragmentClass = null;
        Fragment fragment = null;

        //Build new url
        StringBuilder newURL = new StringBuilder(200);
        // first part...
        newURL.append("https://content.guardianapis.com/search?q=");

        // Searching word
        if (category.equals("Home")) {
            category ="";
        } else if (category.equals("Greece")) {
            category ="";
            newURL.append("Greece");
        }
        if (queryWord.isEmpty()) {
            newURL.append("");
        } else {
            // this is important to search for more than one word ...
            queryWord = queryWord.replace(" ", ",");
            newURL.append(queryWord);
        }

        if (!category.isEmpty()){
            newURL.append("&section=" + category);
        }

        // Api key and thumbnails
        newURL.append("&page-size=30&show-fields=thumbnail&api-key=6089f2c9-7906-4836-bbf2-a9b6cab55f02");
        finalUrl = newURL.toString();

        //renew fragment activity
        try {
            fragmentClass = Fragment_Activity.class;
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putString("finalUrl", finalUrl);
        bundle.putInt("Page", 1);

        // set Fragmentclass Arguments
        fragment.setArguments(bundle);
        fragmentManager = getFragmentManager();
        // Replace the existing fragment...by Resetting the stack.
        fragmentManager.popBackStackImmediate("0", 0);
        int count = fragmentManager.getBackStackEntryCount();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(String.valueOf(count)).commit();
        // show loading indicator
        ProgressBar loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);
    }
}
