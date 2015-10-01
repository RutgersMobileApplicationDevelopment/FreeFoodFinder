package com.rumad.freefoodfinder.freefoodfinder;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.rumad.freefoodfinder.freefoodfinder.fragments.EventListFragment;
import com.rumad.freefoodfinder.freefoodfinder.fragments.EventMapFragment;


public class TabActivity extends ActionBarActivity {


    private FragmentTabHost fragmentTabHost;
    private FragmentManager fragmentManager;
    private String cn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alternate_main);


        Bundle bundle = getIntent().getExtras();

        cn = bundle.getString("campus");

        fragmentManager = getSupportFragmentManager();

        fragmentTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);



        fragmentTabHost.setup(this, fragmentManager, R.id.realtabcontent);


        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("list").setIndicator("List"), EventListFragment.class, null);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("map").setIndicator("Map"), EventMapFragment.class,null);



    }

    private String getCampusName(){
        return cn;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
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
}
