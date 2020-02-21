package com.example.ic11_group1_42;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class ImageRecyclerView extends RecyclerView.Adapter <ImageRecyclerView.MyViewHolder> {

    ArrayList<Image> data;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();

    public ImageRecyclerView(ArrayList<Image> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_custom, parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.imageURLList.addAll(this.data);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Image imageObject = data.get(position);
        Log.d("Demo", data.size()+"");
        Picasso.get().load(imageObject.url).into(holder.image);
        holder.imageObject = imageObject;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        Image imageObject;
        ArrayList<Image>  imageURLList = new ArrayList<>();
        private ImageView image;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {

                   // StorageReference desertRef = storageReference.child(imageObject.pathReference.toString());

// Delete the file
                    imageObject.pathReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            removeAt(getPosition());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                        }
                    });


                    return false;
                }
            });

        }
        public void removeAt(int position){

            data.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,data.size());
        }
    }
}
