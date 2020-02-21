package com.example.hw04_group1_42;


import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    String name, description, imdb, genreValue;
    int year, rating, genre;

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public void setGenreValue(String genreValue) {
        this.genreValue = genreValue;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public Movie(String name, String description, String imdb, String genreValue, int year, int rating, int genre) {
        this.name = name;
        this.description = description;
        this.imdb = imdb;
        this.genreValue = genreValue;
        this.year = year;
        this.rating = rating;
        this.genre = genre;
    }

    protected Movie(Parcel in) {
        name = in.readString();
        description = in.readString();
        imdb = in.readString();
        genreValue = in.readString();
        year = in.readInt();
        rating = in.readInt();
        genre = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImdb() {
        return imdb;
    }

    public String getGenreValue() {
        return genreValue;
    }

    public int getYear() {
        return year;
    }

    public int getRating() {
        return rating;
    }

    public int getGenre() {
        return genre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(imdb);
        parcel.writeString(genreValue);
        parcel.writeInt(year);
        parcel.writeInt(rating);
        parcel.writeInt(genre);
    }
}
