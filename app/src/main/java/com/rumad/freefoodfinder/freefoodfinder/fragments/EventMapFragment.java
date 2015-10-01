package com.rumad.freefoodfinder.freefoodfinder.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.rumad.freefoodfinder.freefoodfinder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventMapFragment extends Fragment {

    private GoogleMap map;

    public EventMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_map, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        super.onViewCreated(view,savedInstanceState);

        setUpMap();

    }

    private void setUpMap(){

        if(map == null){
            ((SupportMapFragment)(getChildFragmentManager().findFragmentById(R.id.events_map))).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                }
            });
        }


    }


}
