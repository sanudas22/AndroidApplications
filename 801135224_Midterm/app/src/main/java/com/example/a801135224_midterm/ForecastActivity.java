package com.example.a801135224_midterm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ForecastActivity extends AppCompatActivity {

    TextView dataV;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        dataV = findViewById(R.id.data);
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        //mAdapter = new ForecastAdapter(new ArrayList<Forecast>());
       // recyclerView.setAdapter(mAdapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(getIntent() != null && !getIntent().getExtras().isEmpty()) {
            String data = getIntent().getExtras().getString(WeatherActivity.DATACLASSVALUE);
            dataV.setText(data);
            String url = "http://api.openweathermap.org/data/2.5/forecast?q="+data+"&appid=8c393d4ccfb95b119e2fc83c8a5cbae9";
            //http://api.openweathermap.org/data/2.5/forecast?q=London,us&appid=8c393d4ccfb95b119e2fc83c8a5cbae9

            if(isConnected()) {
                new LoadCurrentForecast().execute(url);
            } else {
                Toast.makeText(this, "Network Disconnected", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected()) {
            return false;
        }
        return true;
    }

    public class LoadCurrentForecast extends AsyncTask<String, Void, ArrayList> {

        @Override
        protected ArrayList doInBackground(String... strings) {
            ArrayList<Forecast> arrayList = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                Log.d("Demo", strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                JSONObject rootObject = new JSONObject(json);
                Log.d("Demo", rootObject.toString());
                JSONArray jsonArray = rootObject.getJSONArray("list");
                for(int i =0; i<jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject main = jsonObject.getJSONObject("main");
                    Forecast forecast = new Forecast();
                    forecast.time = jsonObject.getString("dt_txt");
                    forecast.temp = main.getString("temp");
                    forecast.humidity = main.getString("humidity");
                    JSONObject sys = jsonObject.getJSONObject("sys");
                    JSONArray statusArray = jsonObject.getJSONArray("weather");
                    JSONObject obj = (JSONObject) statusArray.get(0);
                    forecast.skyStatus = obj.getString("description");
                    forecast.icon = obj.getString("icon");
                    Log.d("Demo", obj.getString("icon"));
                    arrayList.add(forecast);

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            super.onPostExecute(arrayList);
            Log.d("Demo", arrayList.toString());
            if(arrayList.size()>0) {
                mAdapter= new ForecastAdapter(arrayList);
                recyclerView.setAdapter(mAdapter);
                //mAdapter.notifyDataSetChanged();

            }
        }
    }
}
