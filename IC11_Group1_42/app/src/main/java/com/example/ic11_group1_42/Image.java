package com.example.ic11_group1_42;

import com.google.firebase.storage.StorageReference;

public class Image {
    String url;
    StorageReference pathReference;

    public Image(StorageReference pathReference, String url) {
        this.pathReference = pathReference;
        this.url = url;
    }

    @Override
    public String toString() {
        return "pathReference='" + pathReference  +
                ", url='" + url;
    }
}
