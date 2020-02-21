package com.example.ic10_group1_42;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;
/**
 * a. Inclass10
 * b. File Name.: IC10_group1_42
 * c. Full name of students of Group1 42.: SANU DAS
 */
public class MainActivity extends AppCompatActivity {

        private Button add, edit, delete, byYear, byRating;
        public final ArrayList<Movie> moviesList = new ArrayList();
        final ArrayList moviesNames = new ArrayList();
        final static int REQ_CODE = 1990;
        final static String RESULT_KEY = "result";
        String selectedMovieName;
        static String MOVIE_SELECTED;
        static String MOVIE_LIST;
        boolean movieSelected = false;
        int selectedIndex = 0;
        FirebaseFirestore db;
        Movie selectedMovie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

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
        getMovieName();


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("Demo1", moviesNames.toString());
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
                            Log.d("Demo", movieNameStrings[which]);
                            if(m.getName().equals(movieNameStrings[which])){
                                selectedMovie = m;
                                Log.d("Demo", selectedMovie.toString());
                            }
                        }
                        movieSelected = true;
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
                        selectedMovieName = movieNameStrings[which];
                        db.collection("movies").document(selectedMovieName).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MainActivity.this, "Deleted Succesfully: "+selectedMovieName, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        byYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("movies").orderBy("Year", Query.Direction.ASCENDING).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    ArrayList movies = new ArrayList();
                                    ArrayList names = new ArrayList();
                                    for (QueryDocumentSnapshot q : task.getResult()){
                                        Movie movie = new Movie(q.getData());
                                        movies.add(movie);
                                        names.add(movie.getName());
                                    }
                                    if(names.isEmpty() || names == null) {
                                        Toast.makeText(MainActivity.this, "No movie to show", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Intent intent = new Intent("com.example.ic10_group1_42.intent.action.VIEWMOVIEBYYEAR");
                                    intent.putParcelableArrayListExtra(MOVIE_LIST,movies);
                                    startActivity(intent);
                                }
                            }
                        });


            }
        });

        byRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("movies").orderBy("Rating", Query.Direction.DESCENDING).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    ArrayList movies = new ArrayList();
                                    ArrayList names = new ArrayList();
                                    for (QueryDocumentSnapshot q : task.getResult()){
                                        Movie movie = new Movie(q.getData());
                                        movies.add(movie);
                                        names.add(movie.getName());
                                    }
                                    if(names.isEmpty() || names == null) {
                                    Toast.makeText(MainActivity.this, "No movie to show", Toast.LENGTH_SHORT).show();
                                    return;
                                    }
                                Intent intent = new Intent("com.example.ic10_group1_42.intent.action.VIEWMOVIEBYRATING");
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                 intent.putParcelableArrayListExtra(MOVIE_LIST,movies);
                                 startActivity(intent);
                                }
                            }
                        });
            }
        });
    }

    public void getMovieName(){

        db.collection("movies").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                moviesNames.clear();
                moviesList.clear();
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        Log.d("Demo", documentSnapshot.toString());
                        Movie movie = new Movie(documentSnapshot.getData());
                        moviesList.add(movie);
                        moviesNames.add(movie.getName());
                }
                Log.d("Demo", moviesNames.toString());
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode,resultCode,intent);
        if(requestCode == REQ_CODE) {
            if(resultCode==RESULT_OK && intent.getExtras().containsKey(RESULT_KEY)) {

                if(movieSelected) {
                    db.collection("movies").document(selectedMovie.getName()).delete();
                }
                Movie movie = (Movie) intent.getExtras().getParcelable(RESULT_KEY);
                Map<String, Object> movieMap = movie.toHashMap();
                db.collection("movies").document(movie.getName())
                        .set(movieMap);

            }
        }

    }
}
