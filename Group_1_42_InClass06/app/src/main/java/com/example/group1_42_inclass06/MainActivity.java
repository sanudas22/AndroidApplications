package com.example.group1_42_inclass06;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

/**
 * a. Homework 04
 * b. File Name.: Group1_42_Inclass06
 * c. Full name of students of Group1 42.: SANU DAS
 */
public class MainActivity extends AppCompatActivity {

    TextView category, description, title, publishedAt, pageNo;
    ImageView urlImage, prev, next;
    AlertDialog dialog;
    String[] items = {"business","entertainment", "general", "health", "science", "sports", "technology"};
    String selectedCategory;
    ProgressBar progressBar;
    int idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Main Activity");
        category = findViewById(R.id.category);
        description = findViewById(R.id.description);
        title = findViewById(R.id.title);
        publishedAt = findViewById(R.id.publishedAt);
        pageNo = findViewById(R.id.pages);
        urlImage = findViewById(R.id.urlImage);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        title.setVisibility(View.INVISIBLE);
        description.setVisibility(View.INVISIBLE);
        publishedAt.setVisibility(View.INVISIBLE);
        pageNo.setVisibility(View.INVISIBLE);
        urlImage.setVisibility(View.INVISIBLE);
        prev.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose Category");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedCategory = items[i];
                category.setText(items[i]);
                //Log.d("demo", "clicked"+i);
                new ContentDisplay().execute("https://newsapi.org/v2/top-headlines?category="+selectedCategory+"&apiKey=af6089c3fd1346398a4faf21c53c9dcc");
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        dialog = builder.create();

        findViewById(R.id.select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()){
                    dialog.show();
                }

            }
        });




    }

    public class ContentDisplay extends AsyncTask<String, Void, ArrayList>{

        @Override
        protected ArrayList doInBackground(String... strings) {
            ArrayList result = new ArrayList();
            String json;
            HttpURLConnection connection;
            try {
                URL url = new URL(strings[0]);
                //Log.d("demo", url.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                JSONObject root = new JSONObject(json);
                JSONArray articles = root.getJSONArray("articles");
                for(int i=0; i<20;i++) {
                    JSONObject articledata = articles.getJSONObject(i);

                    InformationContent info = new InformationContent();
                    info.title = articledata.getString("title").trim();
                    info.description = articledata.getString("description").trim();
                    info.publishedAt = articledata.getString("publishedAt").trim();
                    info.imageURL = articledata.getString("urlToImage").trim();

                    result.add(info);
                    Log.d("demo", result.toString());
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(final ArrayList result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.INVISIBLE);
            if(result != null) {
                Log.d("demo", result.get(0).toString());
                title.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                publishedAt.setVisibility(View.VISIBLE);
                pageNo.setVisibility(View.VISIBLE);
                urlImage.setVisibility(View.VISIBLE);
                prev.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);

                title.setText(((InformationContent) result.get(0)).title);
                description.setText(((InformationContent) result.get(0)).description);
                publishedAt.setText(((InformationContent) result.get(0)).publishedAt);
                Picasso.get().load(((InformationContent) result.get(0)).imageURL).into(urlImage);

                prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (idx == 0){
                            idx = result.size() - 1;
                            title.setText(((InformationContent) result.get(idx)).title);
                            description.setText(((InformationContent) result.get(idx)).description);
                            publishedAt.setText(((InformationContent) result.get(idx)).publishedAt);
                            Picasso.get().load(((InformationContent) result.get(idx)).imageURL).into(urlImage);
                            pageNo.setText(idx+1+" out of "+result.size());
                        }

                        else{
                            idx -= 1;
                            title.setText(((InformationContent) result.get(idx)).title);
                            description.setText(((InformationContent) result.get(idx)).description);
                            publishedAt.setText(((InformationContent) result.get(idx)).publishedAt);
                            Picasso.get().load(((InformationContent) result.get(idx)).imageURL).into(urlImage);
                            pageNo.setText(idx+1+" out of "+result.size());
                        }

                    }
                });

                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (idx == result.size() - 1){
                            idx =0;
                            title.setText(((InformationContent) result.get(idx)).title);
                            description.setText(((InformationContent) result.get(idx)).description);
                            publishedAt.setText(((InformationContent) result.get(idx)).publishedAt);
                            Picasso.get().load(((InformationContent) result.get(idx)).imageURL).into(urlImage);
                            pageNo.setText(idx+1+" out of "+result.size());
                        }
                        else{
                            idx += 1;
                            title.setText(((InformationContent) result.get(idx)).title);
                            description.setText(((InformationContent) result.get(idx)).description);
                            publishedAt.setText(((InformationContent) result.get(idx)).publishedAt);
                            Picasso.get().load(((InformationContent) result.get(idx)).imageURL).into(urlImage);
                            pageNo.setText(idx+1+" out of "+result.size());

                        }
                    }
                });
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
}
