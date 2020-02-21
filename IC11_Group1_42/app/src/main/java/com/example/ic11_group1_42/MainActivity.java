package com.example.ic11_group1_42;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * a. Inclass11
 * b. File Name.: IC11_group1_42
 * c. Full name of students of Group1 42.: SANU DAS
 */
public class MainActivity extends AppCompatActivity {

    private Button photoButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int count = 0;
    ArrayList imageURLList = new ArrayList();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    Map<String, Object> imagesStore = new HashMap<>();
    private ProgressBar progressBar;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("My Album");
        //SharedPreferences sharedPreferences = new SharedPreferences(MODE_PRIVATE, )

        //loadImagesFromDB();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ImageRecyclerView(imageURLList);
        recyclerView.setAdapter(mAdapter);

        photoButton = findViewById(R.id.takephoto);
        progressBar = findViewById(R.id.progressBar);
        loadImagesFromDB();
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            StorageReference storageReference = firebaseStorage.getReference();
            final String path = "images/camera"+ UUID.randomUUID() +".png";
            final StorageReference image = storageReference.child(path);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
            byte[] dataBytes = byteArrayOutputStream.toByteArray();
            UploadTask uploadTask = image.putBytes(dataBytes);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    return image.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        String imageURL = task.getResult().toString();
                        Image imageObject = new Image(image, imageURL);
                        imageURLList.add(imageObject);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressBar.setProgress((int) progress);
                    System.out.println("Upload is " + progress + "% done");
                }
            });
        }
    }

    private void loadImagesFromDB() {
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference listRef = storageReference.child("images");

        listRef.listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                for(final StorageReference reference : task.getResult().getItems()) {
                    reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()) {
                                String url = task.getResult().toString();
                                imageURLList.add(new Image(reference,url));
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
