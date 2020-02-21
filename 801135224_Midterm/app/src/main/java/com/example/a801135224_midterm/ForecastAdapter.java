package com.example.a801135224_midterm;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MyViewHolder> {
    ArrayList<Forecast> data;
    public ForecastAdapter(ArrayList<Forecast> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_custom, parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Forecast forecast = data.get(position);
        holder.temp.setText("Temperature : "+forecast.temp);
        holder.humidity.setText("Humidity : "+forecast.humidity);
        holder.status.setText(forecast.skyStatus);
        Log.d("Demo", forecast.skyStatus);
        String url = "http://openweathermap.org/img/wn/"+forecast.icon+"@2x.png";//http://openweathermap.org/img/wn/10d@2x.png
        Picasso.get().load(url).into(holder.imageView);
        //holder.date.setText(music.updated_time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date = format.parse(forecast.time);
            PrettyTime prettyTime = new PrettyTime();
            // Date date = new Date(music.updated_time);
            String dateTime = prettyTime.format(date);
            holder.time.setText("At "+forecast.time);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView time, temp, status, humidity;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            humidity = itemView.findViewById(R.id.humidity);
            status = itemView.findViewById(R.id.sky_status);
            temp = itemView.findViewById(R.id.temp);
            imageView = itemView.findViewById(R.id.imageView2);
        }
    }
}
