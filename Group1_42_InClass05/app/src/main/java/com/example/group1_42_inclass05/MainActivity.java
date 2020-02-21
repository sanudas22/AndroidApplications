package com.example.group1_42_inclass05;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.CircularArray;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * a. InClass Assignment 5
 * b. File Name.: Group1_42_InClass0
 * c. Full name of students of Group1 42.: SANU DAS
 */
public class MainActivity extends AppCompatActivity {

    private TextView searchkeyword;
    private ImageView imageView;
    public LinkedList ims;
    int index = 0;
    private ImageView prev;
    private ImageView next;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Photo Gallery");
        searchkeyword = findViewById(R.id.searchKeyword);
        imageView = findViewById(R.id.imageView);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setVisibility(View.INVISIBLE);
        if(isConnected()) {
            findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Keywords().execute("http://dev.theappsdr.com/apis/photos/keywords.php");
                }
            });
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(ims != null && ims.size() > index - 1 && index != 0){
                        index--;
                        new LoadImage().execute((String) ims.get(index));
                    }
                    else if(ims != null && index <= 0){
                        index = ims.size() - 1;
                        new LoadImage().execute((String) ims.get(index));
                    }
                }
            });
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(ims != null && ims.size() > index + 1 && index != ims.size()-1) {
                        index++;
                        new LoadImage().execute((String) ims.get(index));
                    }else if(ims != null && index >= ims.size()-1){
                        index = 0;
                        new LoadImage().execute((String) ims.get(index));
                    }
                }
            });
        } else {
            Toast.makeText(this, "There is no internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected() || networkInfo.getType() != ConnectivityManager.TYPE_WIFI) {
            return false;
        }
        return true;
    }

    private class Keywords extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = IOUtils.toString(connection.getInputStream(), "UTF8");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(final String s) {
            final String[] str = s.split(";");
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Choose a Keyword");

            builder.setItems(str, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            searchkeyword.setText(str[i]);
                            new LoadImageUrls(str[i]).execute("http://dev.theappsdr.com/apis/photos/index.php");
                        }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public class LoadImageUrls extends AsyncTask <String, Void, LinkedList> {
        String selectedKeyword;
        HttpURLConnection connection = null;
        public LoadImageUrls(String keyword) {
            selectedKeyword = keyword;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected LinkedList<String> doInBackground(String... strings) {
            LinkedList<String> keywords = new LinkedList<>();
            String strUrl = null;
            StringBuilder stringBuilder = new StringBuilder();
            try {
                strUrl = strings[0] + "?" +
                        "keyword=" + URLEncoder.encode(selectedKeyword, "UTF-8");
                URL url = new URL(strUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(";");
                }
                String str = stringBuilder.toString();
                String[] arrOfStr = str.split(";");

                for (String a : arrOfStr) {
                    Log.d("Added link:", a);
                    keywords.add(a);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
            return keywords;
        }

        @Override
        protected void onPostExecute(LinkedList s) {
            //imageUrls = s.spl"");
            ims = s;
            if(ims == null) {
                Toast.makeText(MainActivity.this, "No Images Found", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                return;
            }
            new LoadImage().execute((String) ims.get(0));
        }
    }

    public class LoadImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap image = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(strings[0]);
                System.out.println(url);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                image = BitmapFactory.decodeStream(connection.getInputStream());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressBar.setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(bitmap);
        }
    }
}
