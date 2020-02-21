package com.example.group1_42_ic07;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
/**
 * a. InClass07
 * b. File Name.: Group1_42_Inclass07
 * c. Full name of students of Group1 42.: SANU DAS
 */
public class MainActivity extends AppCompatActivity {
    SeekBar seekBar;
    TextView tv_seekValue;
    int progChange;
    Button searchButton;
    EditText et_song;
    RadioGroup rd_selection;
    RadioButton rd_track, rd_artist;
    String sortSelection = "s_track_rating";
    ListView listView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("MusixMatch Track Search");

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        listView = findViewById(R.id.listView);
        rd_selection = findViewById(R.id.rd_selection);
        rd_artist = findViewById(R.id.rd_artist);
        rd_track = findViewById(R.id.rd_track);
        et_song = findViewById(R.id.et_song);
        tv_seekValue = findViewById(R.id.tv_limitvalue);
        searchButton = findViewById(R.id.searchbutton);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(20);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv_seekValue.setText(i+5+"");
                progChange = i+5;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        rd_selection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rd_track:
                        sortSelection = "s_track_rating";
                        break;
                    case R.id.rd_artist:
                        sortSelection = "s_artist_rating";
                        break;
                    default:
                        break;
                }
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_song.getText() != null){
                    String songKeyword = et_song.getText().toString();
                    String url = "https://api.musixmatch.com/ws/1.1/track.search?q="+songKeyword+"&"+sortSelection+"=desc&page_size="+progChange+"&apikey=04915302fec77f19200e27c985665d8c";
                    new LoadSongs().execute(url);
                }
                //https://api.musixmatch.com/ws/1.1/track.search?q=wake&s_track_rating=desc&page_size=10&apikey=04915302fec77f19200e27c985665d8c
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

    public class LoadSongs extends AsyncTask<String, Void, ArrayList> {

        @Override
        protected ArrayList doInBackground(String... strings) {
            ArrayList<Music> musicList = new ArrayList();
            try {
                URL url = new URL(strings[0]);
                Log.d("Demo", strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                Log.d("Demo", json);
                JSONObject root = new JSONObject(json);
                JSONObject message = root.getJSONObject("message");
                JSONObject body = message.getJSONObject("body");
                //Log.d("Demo", root.toString());
                JSONArray jsonArray = body.getJSONArray("track_list");

                for(int i = 0; i< jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject track = jsonObject.getJSONObject("track");
                   // Log.d("Demo", track.toString());
                    Music music = new Music();
                    music.track_name = track.getString("track_name");
                    music.album_name = track.getString("album_name");
                    music.artist_name = track.getString("artist_name");
                    music.updated_time = track.getString("updated_time");
                    music.track_share_url = track.getString("track_share_url");
                    musicList.add(music);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return musicList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(final ArrayList arrayList) {
            progressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(arrayList);
            if(arrayList.size()>0) {
                MusicAdapter adapter = new MusicAdapter(MainActivity.this, R.layout.music_custom,arrayList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Music music = (Music) arrayList.get(i);
                        String url = music.track_share_url;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
