package com.example.homework03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * a. Homework 03
 * b. File Name.: Homework03
 * c. Full name of students of Group1 42.: SANU DAS
 */
public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    Button generateButton;
    TextView tv_seekValue;
    int progChange;
    ProgressBar progressBar;
    TextView min, max, av;
    Handler handler;
    public String MIN_VALUE = "minimum";
    public String MAX_VALUE = "maximum";
    public String AV_VALUE = "average";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("InClass4");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setVisibility(View.INVISIBLE);
        min = findViewById(R.id.minValue);
        max = findViewById(R.id.maxValue);
        av = findViewById(R.id.avValue);

        tv_seekValue = findViewById(R.id.tv_seekValue);
        generateButton = findViewById(R.id.generateNumAT);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(10);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv_seekValue.setText(i+" Times");
                progChange = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(progChange == 0){
                    min.setText("");
                    max.setText("");
                    av.setText("");
                    return;
                }

                ExecutorService executorService = Executors.newFixedThreadPool(2);
                executorService.execute(new DoWork());
                progressBar.setVisibility(View.VISIBLE);

                handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {
                        progressBar.setVisibility(View.INVISIBLE);
                        min.setText(message.getData().getString(MIN_VALUE));
                        max.setText(message.getData().getString(MAX_VALUE));
                        av.setText(message.getData().getString(AV_VALUE));
                        return false;
                    }
                });
            }
        });

    }


    class DoWork implements Runnable {
        @Override
        public void run() {
            Double sum = 0.0;
            ArrayList<Double> list = HeavyWork.getArrayNumbers(progChange);
            Collections.sort(list);
            Bundle bundle = new Bundle();
            bundle.putString(MIN_VALUE,list.get(0)+"");
            bundle.putString(MAX_VALUE,(list.get(list.size()-1))+"");
            for (Double d:list) {
                sum+=d;
            }
            bundle.putString(AV_VALUE,(sum/list.size())+"");
            Message message = new Message();
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }
}
