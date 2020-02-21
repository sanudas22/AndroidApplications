package com.example.group1_42_ic07;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

public class MusicAdapter extends ArrayAdapter {
    public MusicAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Music music = (Music) getItem(position);
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.music_custom, parent,false);
            viewHolder = new ViewHolder();
            viewHolder.trackname = convertView.findViewById(R.id.trackname);
            viewHolder.artistname = convertView.findViewById(R.id.artistname);
            viewHolder.albumname = convertView.findViewById(R.id.albumname);
            viewHolder.date = convertView.findViewById(R.id.date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.trackname.setText(music.track_name);
        viewHolder.artistname.setText(music.artist_name);
        viewHolder.albumname.setText(music.album_name);
        viewHolder.date.setText(music.updated_time);

        return convertView;
    }

    private static class ViewHolder{
        TextView trackname, artistname, albumname, date;
    }
}
