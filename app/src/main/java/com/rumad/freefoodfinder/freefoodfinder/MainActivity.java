package com.rumad.freefoodfinder.freefoodfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rumad.freefoodfinder.freefoodfinder.helpers.EventScraper;
import com.rumad.freefoodfinder.freefoodfinder.pojos.Event;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends Activity {


    private Button buschBtn, liviBtn, collegaeaveBtn, cookBtn;

    private final String BUSCH = "Busch";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        buschBtn = (Button)findViewById(R.id.busch_btn);
        liviBtn = (Button)findViewById(R.id.livi_btn);
        collegaeaveBtn = (Button)findViewById(R.id.ca_btn);
        cookBtn = (Button)findViewById(R.id.cook_btn);




        buschBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this,ShowEventsListActivity.class);

                intent.putExtra("campus",BUSCH);

                startActivity(intent);

            }
        });

        liviBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        collegaeaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
