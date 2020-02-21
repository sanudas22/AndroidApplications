package com.example.homework02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private TextView tv_baseprice, tv_deliverycost, tv_toppings, tv_total, tv_toppList;
    double toppingsCost, totalCost, deliveryCost = 0.0;
    private Button finish;
    List<String> toppList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setTitle("Pizza Order");

        tv_baseprice = findViewById(R.id.tv_basepricevalue);
        tv_deliverycost = findViewById(R.id.tv_deliverycostvalue);
        tv_toppings = findViewById(R.id.tv_toppingsvalue);
        tv_total = findViewById(R.id.tv_totalvalue);
        tv_toppList = findViewById(R.id.tv_toppList);
        finish = findViewById(R.id.finish);

        if(getIntent() != null && getIntent().getExtras()!=null
                && getIntent().getStringArrayListExtra(MainActivity.TOPPINGS) !=null) {
            toppList = getIntent().getStringArrayListExtra(MainActivity.TOPPINGS);
            toppingsCost = toppList.size()*1.50;
            tv_baseprice.setText("6.50$");
            if(getIntent().getExtras().getBoolean(MainActivity.CHECKBOX)) {
                deliveryCost = 4.00;
            }
            tv_deliverycost.setText(deliveryCost+"$");
            tv_toppings.setText(toppingsCost+"$");
            tv_toppList.setText(Arrays.toString(toppList.toArray()).replace("[","").replace("]",""));
            totalCost = 6.50+deliveryCost+toppingsCost;
            tv_total.setText(totalCost+"$");
            tv_total.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        }
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
