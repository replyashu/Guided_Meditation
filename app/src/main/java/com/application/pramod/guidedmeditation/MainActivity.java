package com.application.pramod.guidedmeditation;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Vibrator v;
    com.application.pramod.guidedmeditation.Params param;
    PowerManager pm;
    PowerManager.WakeLock wl;
    private final String FRAGMENT_TAG_SWIPE = "swipe";
    private final String FRAGMENT_TAG_FLIP = "flip";
    private final String FRAGMENT_TAG_SWIPE_HELP = "swipe_help";
    private final String FRAGMENT_TAG_FLIP_HELP = "flip_help";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        param = new com.application.pramod.guidedmeditation.Params();

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
        wl.acquire();

        // Get instance of Vibrator from current Context
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Initialize the swipe mode initially
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Handle the camera action
        Fragment_swipe fragmentM = new Fragment_swipe();
        fragmentTransaction.add(R.id.frag_space, fragmentM, FRAGMENT_TAG_SWIPE);
        //fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
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

        switch (item.getItemId()) {
            case R.id.reset: {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Reset counter")
                        .setMessage("Do you want to reset")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                countText.setText("0");
                                param.setCount(0); // Reset count
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                dialog.show();
            }
            return true;
            case R.id.set: {
                {
                    final EditText taskEditText = new EditText(this);
                    taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    taskEditText.setRawInputType(Configuration.KEYBOARD_12KEY);

                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("Set the count")
                            .setMessage("Set the count at which the alert is required")
                            .setView(taskEditText)
                            .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    param.setCount_limit(Integer.parseInt(String.valueOf(taskEditText.getText())));
                                }
                            })
                            .setNegativeButton("No", null)
                            .create();
                    dialog.show();
                }
            }
            return true;
            case R.id.check:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Count value is " + param.getCount_limit().toString())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //                               countText.setText("0");
                            }
                        })
                        .create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_swipe) {

            checkAvailableFragmentRemove(FRAGMENT_TAG_FLIP);
            checkAvailableFragmentRemove(FRAGMENT_TAG_SWIPE);
            checkAvailableFragmentRemove(FRAGMENT_TAG_SWIPE);
            checkAvailableFragmentRemove(FRAGMENT_TAG_SWIPE_HELP);
            checkAvailableFragmentRemove(FRAGMENT_TAG_FLIP_HELP);

            // Handle the camera action
            Fragment_swipe fragmentM = new Fragment_swipe();
            fragmentTransaction.add(R.id.frag_space, fragmentM, FRAGMENT_TAG_SWIPE);
            //fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_flip) {
            checkAvailableFragmentRemove(FRAGMENT_TAG_SWIPE);
            checkAvailableFragmentRemove(FRAGMENT_TAG_SWIPE);
            checkAvailableFragmentRemove(FRAGMENT_TAG_FLIP);
            checkAvailableFragmentRemove(FRAGMENT_TAG_SWIPE_HELP);
            checkAvailableFragmentRemove(FRAGMENT_TAG_FLIP_HELP);


            // Handle the camera action
            Fragment_flip fragmentF = new Fragment_flip();
            fragmentTransaction.add(R.id.frag_space, fragmentF, FRAGMENT_TAG_FLIP);
            //fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_help_swipe) {
            checkAvailableFragmentRemove(FRAGMENT_TAG_SWIPE);
            checkAvailableFragmentRemove(FRAGMENT_TAG_SWIPE);
            checkAvailableFragmentRemove(FRAGMENT_TAG_FLIP);
            checkAvailableFragmentRemove(FRAGMENT_TAG_SWIPE_HELP);
            checkAvailableFragmentRemove(FRAGMENT_TAG_FLIP_HELP);

            // Handle Help activity
            Fragment_help_flip fragmentHF = new Fragment_help_flip();
            fragmentTransaction.add(R.id.frag_space, fragmentHF, FRAGMENT_TAG_SWIPE_HELP);
            //fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_help_flip) {
            checkAvailableFragmentRemove(FRAGMENT_TAG_SWIPE);
            checkAvailableFragmentRemove(FRAGMENT_TAG_SWIPE);
            checkAvailableFragmentRemove(FRAGMENT_TAG_FLIP);
            checkAvailableFragmentRemove(FRAGMENT_TAG_SWIPE_HELP);
            checkAvailableFragmentRemove(FRAGMENT_TAG_FLIP_HELP);

            // Handle Help activity
            Fragment_help_swipe fragmentHS = new Fragment_help_swipe();
            fragmentTransaction.add(R.id.frag_space, fragmentHS, FRAGMENT_TAG_FLIP_HELP);
            //fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkAvailableFragmentRemove(String tag) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if(fragment != null) {
            //fragmentTransaction.remove(fragmentManager.findFragmentByTag(tag)).commit();
            fragmentTransaction.remove(fragmentManager.findFragmentByTag(tag)).commit();
        }
    }
}
