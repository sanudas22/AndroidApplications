package com.example.hw04_group1_42;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {
    private SeekBar seekBar;
    private TextView ratingValue;
    private EditText et_name, et_desc, et_year, et_imdb;
    private Spinner spinner;
    private Button saveChanges;
    String name, description, imdb, genreValue;
    int rating, genre, year;
    Movie movieSelectedForEdit;
    ArrayList<Movie> moviesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ratingValue = findViewById(R.id.ratingValue);
        et_desc = findViewById(R.id.et_desc);
        et_name = findViewById(R.id.et_name);
        spinner = findViewById(R.id.spinner);
        et_year = findViewById(R.id.year);
        et_imdb = findViewById(R.id.imdbURL);
        seekBar = findViewById(R.id.seekBar);
        saveChanges = findViewById(R.id.save);
        seekBar.setMax(5);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ratingValue.setText(i+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        if(getIntent() != null && getIntent().getExtras() != null) {
            movieSelectedForEdit = getIntent().getExtras().getParcelable(MainActivity.MOVIE_SELECTED);
            moviesList = this.getIntent().getExtras().getParcelableArrayList(MainActivity.MOVIE_LIST);
            if(movieSelectedForEdit != null) {
                et_name.setText(movieSelectedForEdit.getName());
                et_desc.setText(movieSelectedForEdit.getDescription());
                et_year.setText(movieSelectedForEdit.getYear()+"");
                et_imdb.setText(movieSelectedForEdit.getImdb());
                spinner.setSelection(movieSelectedForEdit.getGenre());
                ratingValue.setText(movieSelectedForEdit.getRating()+"");
                seekBar.setProgress(movieSelectedForEdit.rating);
            }
        }

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 if(et_name != null && !et_name.getText().toString().equals("")) {
                    name = et_name.getText().toString();
                } else {
                    Toast.makeText(AddActivity.this, "Please enter a movie name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_desc != null && !et_desc.getText().toString().equals("")) {
                    description = et_desc.getText().toString();
                } else {
                    Toast.makeText(AddActivity.this, "Please enter a description", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(spinner != null && !spinner.getSelectedItem().equals("") && !spinner.getSelectedItem().equals("Select")) {
                    genre = spinner.getSelectedItemPosition();
                    genreValue = spinner.getSelectedItem().toString();
                } else {
                    Toast.makeText(AddActivity.this, "Please select a genre", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(ratingValue != null && !ratingValue.getText().toString().equals("")){
                    rating = Integer.parseInt(ratingValue.getText().toString());
                } else {
                    Toast.makeText(AddActivity.this, "Please rate this movie", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_year != null && !et_year.getText().toString().equals("")) {
                    year = Integer.parseInt(et_year.getText().toString());
                    if (year>2019 || year<1900) {
                        Toast.makeText(AddActivity.this, "Year out of range! Please enter between 1900 to 2019", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(AddActivity.this, "Please enter a year", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_imdb != null && !et_imdb.getText().toString().equals("")) {
                    imdb = et_imdb.getText().toString();
                } else {
                    Toast.makeText(AddActivity.this, "Please enter IMDB url", Toast.LENGTH_SHORT).show();
                    return;
                }


                Intent intent = new Intent();
                Movie movie = new Movie(name,description,imdb,genreValue,year,rating,genre);
                intent.putExtra(MainActivity.RESULT_KEY,movie);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }
}
