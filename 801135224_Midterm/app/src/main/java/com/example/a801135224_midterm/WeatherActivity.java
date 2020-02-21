package com.example.a801135224_midterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity {

    TextView dataValue, temp, tempMax, tempMin, desc, humidity, windSpeed;
    Button forecast;
    ImageView imageView;
    public static String DATACLASSVALUE = "data";
    String dataClassValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        setTitle("Current Weather");
        dataValue = findViewById(R.id.dataValue);
        temp = findViewById(R.id.temperature);
        tempMax = findViewById(R.id.temperatureMax);
        tempMin = findViewById(R.id.temperatureMin);
        desc = findViewById(R.id.desc);
        humidity = findViewById(R.id.humidity);
        windSpeed = findViewById(R.id.windSpeed);
        forecast = findViewById(R.id.bt_forecast);
        imageView = findViewById(R.id.imageView);

        if(getIntent() != null && !getIntent().getExtras().isEmpty()) {
            Data data = (Data) getIntent().getExtras().getSerializable(MainActivity.DATA);
            dataClassValue = data.toString();
            dataValue.setText(data.toString());
            String url = "http://api.openweathermap.org/data/2.5/weather?q="+data.toString()+"&appid=8c393d4ccfb95b119e2fc83c8a5cbae9";
            if(isConnected()) {
                new LoadCurrentWeather().execute(url);
            } else {
                Toast.makeText(this, "Network Disconnected", Toast.LENGTH_SHORT).show();
            }

        }

        forecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherActivity.this, ForecastActivity.class);
                intent.putExtra(DATACLASSVALUE, dataClassValue);
                startActivity(intent);
                finish();

            }
        });
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected()) {
            return false;
        }
        return true;
    }

    public class LoadCurrentWeather extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... strings) {
            Weather weather = new Weather();
            try {
                URL url = new URL(strings[0]);
                Log.d("Demo", strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                JSONObject rootObject = new JSONObject(json);
                Log.d("Demo", rootObject.toString());
                JSONArray jsonArray = rootObject.getJSONArray("weather");
                for(int i =0; i<jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    weather.desc = jsonObject.getString("description");
                    weather.image = jsonObject.getString("icon");
                }
                JSONObject wind = rootObject.getJSONObject("wind");
                weather.windSpeed = wind.getString("speed");
                JSONObject main = rootObject.getJSONObject("main");
                weather.temp = main.getString("temp");
                weather.tempMax = main.getString("temp_max");
                weather.tempMin = main.getString("temp_min");
                weather.humidity = main.getString("humidity");


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            temp.setText("Temperature : "+weather.temp);
            tempMax.setText("Temperature Max : "+weather.tempMax);
            tempMin.setText("Temperature Min : "+weather.tempMin);
            desc.setText("Description : "+weather.desc);
            humidity.setText("Humidity : "+weather.humidity);
            windSpeed.setText("Wind Speed : "+weather.windSpeed);
            String url = "http://openweathermap.org/img/wn/"+weather.image+"@2x.png";//http://openweathermap.org/img/wn/10d@2x.png
            Picasso.get().load(url).into(imageView);
            //imageView
        }
    }
}


