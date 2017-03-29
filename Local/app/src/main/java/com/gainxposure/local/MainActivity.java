package com.gainxposure.local;

import android.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.gainxposure.local.ui.adList.AdListFragment;
import com.gainxposure.local.ui.carousel.Carousel;
import com.gainxposure.local.ui.carousel.CarouselExo;
import com.gainxposure.local.ui.carousel.CarouselImage;
import com.gainxposure.local.ui.carousel.CarouselVideo;
import com.gainxposure.local.ui.carousel.TextFragment;
import com.gainxposure.local.ui.fileChooser.FileChooser;
import com.gainxposure.local.ui.playlist.PlaylistFragment;
import com.gainxposure.local.utils.PlaylistChangeListener;

public class MainActivity extends AppCompatActivity
        implements PlaylistChangeListener, AdListFragment.OnFragmentInteractionListener, TextFragment.OnFragmentInteractionListener, CarouselExo.OnFragmentInteractionListener, CarouselImage.OnFragmentInteractionListener, CarouselVideo.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener, Carousel.OnFragmentInteractionListener, PlaylistFragment.OnFragmentInteractionListener, FileChooser.OnFragmentInteractionListener {

    private View decorView;

    public Carousel carousel;
    public PlaylistFragment playlist;
    public FileChooser importMedia;
    public AdListFragment adList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        this.decorView = getWindow().getDecorView();

        /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        // instantiate the fragments.
        this.carousel = new Carousel();
        this.playlist = new PlaylistFragment();
        this.importMedia = new FileChooser();
        this.adList = new AdListFragment();

        this.playlist.setPlaylistChangeListener(this);
        this.adList.setPlaylistChangeListener(this);

        this.updateFragmentFrame(carousel);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        String title = item.getTitle().toString();
        Toast.makeText( this, title + ": Placebo", Toast.LENGTH_SHORT).show();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_import) { // fileChooser
            this.updateFragmentFrame(this.importMedia);
        } else if (id == R.id.nav_gallery) { // playlist

            this.updateFragmentFrame(this.adList);
            /* For the back button
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); */

        } else if (id == R.id.nav_slideshow) { //
            getSupportActionBar().hide();
            this.carousel.getAdaptor().notifyDataSetChanged();
            this.carousel.showPresent();
            this.updateFragmentFrame(this.carousel);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateFragmentFrame(Fragment aFragment) {
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment_placeholder, aFragment);
        if (!(aFragment instanceof Carousel)) {
            ft.addToBackStack("");
        }

        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPlaylistChange() {
        if (null != this.carousel) {
            this.carousel.getAdaptor().notifyDataSetChanged();
        }
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        this.decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        this.decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}
