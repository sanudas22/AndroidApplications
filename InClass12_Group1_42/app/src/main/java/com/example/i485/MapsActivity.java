package com.example.i485;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Group1_42
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> latlnglist = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        String jsonString = loadJSONFromRaw(this);
        try {
            JSONObject root = new JSONObject(jsonString);
            JSONArray points = root.getJSONArray("points");
            for (int i = 0; i< points.length(); i++) {
                JSONObject jsonObject = points.getJSONObject(i);
                LatLng l1 = new LatLng(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"));
                latlnglist.add(l1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public String loadJSONFromRaw(Context context) {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.trip);
            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        int last = latlnglist.size()-1;
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/


        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(latlnglist);
        polylineOptions.clickable(false);

        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
        for(int i = 0; i < latlnglist.size();i++){
            latLngBuilder.include(latlnglist.get(i));
        }
        final LatLngBounds bounds = latLngBuilder.build();
        int padding = 40;
        mMap.addPolyline(polylineOptions);
        mMap.addMarker(new MarkerOptions().position(latlnglist.get(0)).title("Start"));
        mMap.addMarker(new MarkerOptions().position(latlnglist.get(last)).title("End"));
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
       // mMap.moveCamera(cu);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));
            }
        });
       // mMap.setLatLngBoundsForCameraTarget(latLngBuilder.build());
    }
}
