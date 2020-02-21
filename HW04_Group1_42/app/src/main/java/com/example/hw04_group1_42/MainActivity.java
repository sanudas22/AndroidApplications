package com.example.hw04_group1_42;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * a. Homework 04
 * b. File Name.: hw04_group1_42
 * c. Full name of students of Group1 42.: SANU DAS
 */
public class MainActivity extends AppCompatActivity {

    private Button add, edit, delete, byYear, byRating;
    public ArrayList<Movie> moviesList = new ArrayList();
    public ArrayList moviesNames = new ArrayList();
    final static int REQ_CODE = 1990;
    final static String RESULT_KEY = "result";
    Movie selectedMovie;
    static String MOVIE_SELECTED;
    static String MOVIE_LIST;
    boolean movieSelected = false;
    int selectedIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("My Favorite Movies");
        add = findViewById(R.id.add);
        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);
        byYear = findViewById(R.id.byYear);
        byRating = findViewById(R.id.byRating);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putParcelableArrayListExtra(MOVIE_LIST,moviesList);
                startActivityForResult(intent, REQ_CODE);

            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(moviesNames.isEmpty() || moviesNames == null) {
                    Toast.makeText(MainActivity.this, "No movie to edit", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String[] movieNameStrings = (String[]) moviesNames.toArray(new String[0]);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pick a Movie");
                builder.setItems(movieNameStrings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, AddActivity.class);
                        for (Movie m :moviesList) {
                            if(m.getName().equals(movieNameStrings[which])){
                                selectedMovie = m;
                            }
                        }
                        movieSelected = true;
                        selectedIndex = moviesList.indexOf(selectedMovie);
                        intent.putExtra(MOVIE_SELECTED, selectedMovie);
                        startActivityForResult(intent, REQ_CODE);

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(moviesNames.isEmpty() || moviesNames == null) {
                    Toast.makeText(MainActivity.this, "No movie to delete", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String[] movieNameStrings = (String[]) moviesNames.toArray(new String[0]);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pick a Movie");
                builder.setItems(movieNameStrings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, AddActivity.class);
                        for (Movie m :moviesList) {
                            if(m.getName().equals(movieNameStrings[which])){
                                selectedMovie = m;
                            }
                        }
                        moviesList.remove(selectedMovie);
                        moviesNames.remove(movieNameStrings[which]);
                        Toast.makeText(MainActivity.this, "Deleted Succesfully: "+movieNameStrings[which], Toast.LENGTH_SHORT).show();

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        byYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(moviesNames.isEmpty() || moviesNames == null) {
                    Toast.makeText(MainActivity.this, "No movie to show", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent("com.example.hw04_group1_42.intent.action.VIEWMOVIEBYYEAR");
                intent.putParcelableArrayListExtra(MOVIE_LIST,moviesList);
                startActivity(intent);
            }
        });

        byRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(moviesNames.isEmpty() || moviesNames == null) {
                    Toast.makeText(MainActivity.this, "No movie to show", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent("com.example.hw04_group1_42.intent.action.VIEWMOVIEBYRATING");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putParcelableArrayListExtra(MOVIE_LIST,moviesList);
                startActivity(intent);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode,resultCode,intent);
        if(requestCode == REQ_CODE) {
            if(resultCode==RESULT_OK && intent.getExtras().containsKey(RESULT_KEY)) {
                Movie movie = (Movie) intent.getExtras().getParcelable(RESULT_KEY);
                if(movieSelected) {
                    moviesNames.remove((selectedMovie.getName()));
                    moviesList.remove(selectedMovie);
                    moviesList.add(selectedIndex,movie);
                } else{
                    moviesList.add(movie);
                }
                moviesNames.add(movie.getName());
            }
        }

    }
}
