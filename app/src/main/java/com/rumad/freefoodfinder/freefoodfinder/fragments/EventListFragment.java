package com.rumad.freefoodfinder.freefoodfinder.fragments;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rumad.freefoodfinder.freefoodfinder.R;
import com.rumad.freefoodfinder.freefoodfinder.TabActivity;
import com.rumad.freefoodfinder.freefoodfinder.helpers.EventScraper;
import com.rumad.freefoodfinder.freefoodfinder.pojos.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends Fragment {


    private ListView eventList;
    private EventAdapter eventAdapter;

    private String campusName;

    public EventListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_show_events_list,null,false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        super.onViewCreated(view,savedInstanceState);

        eventList = (ListView)view.findViewById(R.id.event_list);

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

            progressDialog = new ProgressDialog(getActivity());

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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.event_item_cell_layout,null,false);
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



}
