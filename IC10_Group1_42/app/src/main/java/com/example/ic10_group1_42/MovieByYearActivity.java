package com.example.ic10_group1_42;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

public class MovieByYearActivity extends AppCompatActivity {

    TextView title, description, genre, rating, year, imdb;
    ImageView first, last, prev, next;
    ArrayList<Movie> movies;
    Button finish;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_by_year);

        setTitle("Movies by Year");
        title = findViewById(R.id.titleValue);
        description = findViewById(R.id.descriptionValue);
        genre = findViewById(R.id.genreValue);
        rating = findViewById(R.id.ratingValue);
        year = findViewById(R.id.yearValue);
        imdb = findViewById(R.id.imdbValue);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        first = findViewById(R.id.first);
        last = findViewById(R.id.last);
        finish = findViewById(R.id.finish);
        if(getIntent() != null && getIntent().getExtras() != null) {
            movies = this.getIntent().getExtras().getParcelableArrayList(MainActivity.MOVIE_LIST);
            displayMovie(movies.get(index));
        }
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(movies != null && movies.size() > index - 1 && index != 0){
                    index--;
                    displayMovie(movies.get(index));
                }
                else if(movies != null && index <= 0){
                    index = movies.size() - 1;
                    displayMovie(movies.get(index));
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(movies != null && movies.size() > index + 1 && index != movies.size()-1) {
                    index++;
                    displayMovie(movies.get(index));
                }else if(movies != null && index >= movies.size()-1){
                    index = 0;
                    displayMovie(movies.get(index));
                }
            }
        });

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayMovie(movies.get(0));
            }
        });

        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lastIndex = movies.size()-1;
                displayMovie(movies.get(lastIndex));
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void displayMovie(Movie movie) {
        title.setText(movie.getName());
        description.setText(movie.getDescription());
        description.setMovementMethod(new ScrollingMovementMethod());
        year.setText(movie.getYear()+"");
        imdb.setText(movie.getImdb());
        rating.setText(movie.getRating()+"/5");
        genre.setText(movie.getGenreValue());
    }
}
