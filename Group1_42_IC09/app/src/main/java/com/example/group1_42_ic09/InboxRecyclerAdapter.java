package com.example.group1_42_ic09;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class InboxRecyclerAdapter extends RecyclerView.Adapter<InboxRecyclerAdapter.MyViewHolder> {
    JSONArray messages;
    String subject, message, fname, lname, created_at;
    private Context context;
    public InboxRecyclerAdapter(Context context, JSONArray messages) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_inbox, parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            JSONObject obj = messages.getJSONObject(position);
            subject = obj.getString("subject");
            message = obj.getString("message");
            fname = obj.getString("sender_fname");
            lname = obj.getString("sender_lname");
            created_at = obj.getString("created_at");

            Email email = new Email();
            email.subjectbody = subject;
            email.message = message;
            email.createdat = created_at;
            email.senderName = fname+" "+lname;
            holder.subject.setText(subject);
            holder.con = context;
            holder.email = email;

            /*if(updated_time != null || !updated_time.equals("")) {

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date date = format.parse(updated_time);
                PrettyTime prettyTime = new PrettyTime();
                String dateTime = prettyTime.format(date);
                holder.date.setText(dateTime);
            }*/
            holder.jsonObject = obj;

        } catch (JSONException e) {
            e.printStackTrace();
        }/* catch (ParseException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public int getItemCount() {
        return messages.length();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        JSONObject jsonObject;
        TextView subject, date;
        ImageView delete;
        Email email;
        Context con;
        public static  String EMAIL = "email";
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.subject);
            date = itemView.findViewById(R.id.date);
            delete = itemView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Request request = new Request.Builder()
                            .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox/delete/2")
                            .header("Authorization", "BEARER "+ InboxActivity.token)
                            .build();

                    InboxActivity.client.newCall(request).enqueue(new Callback() {
                        @Override public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override public void onResponse(Call call, Response response) throws IOException {
                            try (ResponseBody responseBody = response.body()) {
                                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                                Headers responseHeaders = response.headers();
                                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                                }
                                System.out.println(responseBody.string());
                            }
                        }
                    });
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(con, DisplayMail.class);
                    intent.putExtra(EMAIL, email);
                    con.startActivity(intent);
                }
            });
        }
    }
}
