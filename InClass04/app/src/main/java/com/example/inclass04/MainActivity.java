package com.example.inclass04;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;

/**
 * a. InClass Assignment 4
 * b. File Name.: InClass04
 * c. Full name of students of Group1 42.: SANU DAS
 */
public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    Button generateButton;
    TextView tv_seekValue;
    int progChange;
    ProgressBar progressBar;
    TextView min, max, av;
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
                new DoWork().execute(progChange);
            }
        });
    }

    class DoWork extends AsyncTask<Integer, Integer, ArrayList<Double>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<Double> list) {
            Double sum = 0.0;
            super.onPostExecute(list);
            progressBar.setVisibility(View.INVISIBLE);
            Collections.sort(list);
            min.setText(list.get(0)+"");
            max.setText(list.get(list.size()-1)+"");
            for (Double d:list) {
                sum+=d;
            }
            av.setText((sum/list.size())+"");

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Double> doInBackground(Integer... integers) {
            ArrayList<Double> list = HeavyWork.getArrayNumbers(integers[0]);
            return list;
        }
    }
}
