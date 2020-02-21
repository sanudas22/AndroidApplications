package com.example.a801135224_midterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList dataList = new ArrayList();
    public static String DATA = "data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        InputStream inputStream = getResources().openRawResource(R.raw.cities);
        String json = null;
        try {
            json = IOUtils.toString(inputStream, "UTF-8");
            JSONObject root = new JSONObject(json);
            JSONArray jsonArray = root.getJSONArray("data");

            for(int i = 0; i< jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Data data = new Data();
                data.city = jsonObject.getString("city");
                data.country = jsonObject.getString("country");
                dataList.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ArrayAdapter adapter
                = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, dataList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                Data data = (Data) dataList.get(i);
                intent.putExtra(DATA, data);
                startActivity(intent);
                //http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=8c393d4ccfb95b119e2fc83c8a5cbae9


            }
        });
    }


    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("raw/cities.json");

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

}
