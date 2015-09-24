package com.rumad.freefoodfinder.freefoodfinder;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rumad.freefoodfinder.freefoodfinder.helpers.EventScraper;
import com.rumad.freefoodfinder.freefoodfinder.pojos.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ShowEventsListActivity extends ActionBarActivity {


    private ListView eventList;
    private EventAdapter eventAdapter;

    private String campusName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_events_list);

        eventList = (ListView)findViewById(R.id.event_list);

        Bundle bundle = getIntent().getExtras();

        campusName = bundle.getString("campus");

        Toast.makeText(ShowEventsListActivity.this,campusName,Toast.LENGTH_LONG).show();

        new getEventsTask().execute();




    }


    private class getEventsTask extends AsyncTask<Void,Void,ArrayList<Event>> {


        private ProgressDialog progressDialog;

        public getEventsTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ShowEventsListActivity.this);

            progressDialog.setTitle("Waiting...");

            progressDialog.setIndeterminate(true);

        }


        @Override
        protected ArrayList<Event> doInBackground(Void... params) {

            ArrayList<Event> events = null;

            try {
                events = EventScraper.getEvents();

            }
            catch (IOException e){
                Log.e("IOException", e.getMessage());
            }

            return events;
        }



        @Override
        protected void onPostExecute(ArrayList<Event> events) {
            super.onPostExecute(events);

            progressDialog.dismiss();

            eventAdapter = new EventAdapter(events);

            eventList.setAdapter(eventAdapter);
        }
    }

    private class EventAdapter extends BaseAdapter {

        List<Event> events;

        public EventAdapter(List<Event> foundEvents) {
            super();
            events = foundEvents;
        }

        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public Event getItem(int position) {
            return  events.get(position);
        }

        @Override
        public long getItemId(int position) {
            return  position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {



            if(convertView == null){
                convertView = LayoutInflater.from(ShowEventsListActivity.this).inflate(R.layout.event_item_cell_layout,null,false);
            }


            TextView nameView = (TextView)convertView.findViewById(R.id.event_name);

            Event myEvent = getItem(position);

            nameView.setText(myEvent.getName());

            if(myEvent.getCampus().equals(campusName)) {
                nameView.setBackgroundColor(Color.BLUE);
            }


            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_events_list, menu);
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
