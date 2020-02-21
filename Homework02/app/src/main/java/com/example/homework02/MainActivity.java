package com.example.homework02;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * a. Homework02
 * b. File Name.: Homework02
 * c. Full name of students of Group 20.: SANU DAS, AKHIL CHUNDARATHIL, RAVI THEJA GOALLA
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    AlertDialog.Builder builder;
    AlertDialog dialog;
    public Button addtopping, clearpizza, checkout;
    String[] toppings = {"Bacon", "Cheese", "Garlic", "Green Pepper", "Mushroom", "Olives", "Onions", "Red Pepper"};
    ArrayList<Integer> drawablelist = new ArrayList<Integer>(Arrays.asList(R.drawable.bacon,R.drawable.cheese,R.drawable.garlic,R.drawable.green_pepper,R.drawable.mashroom,R.drawable.olive,R.drawable.onion,R.drawable.red_pepper));
    List<String> toppingsorderlist = new ArrayList<String>();
    LinearLayout toppingsrow1, toppingsrow2;
    LinearLayout.LayoutParams params;
    int numberofToppings = 0;
    ProgressBar pb;
    public CheckBox delivery;
    static public String TOPPINGS = "toppings";
    static public String CHECKBOX = "checked";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toppingsrow1 = findViewById(R.id.toppingsrow1);
        toppingsrow2 = findViewById(R.id.toppingsrow2);
        clearpizza = findViewById(R.id.clearpizza);
        pb = findViewById(R.id.progressBar);
        checkout = findViewById(R.id.checkout);
        delivery = findViewById(R.id.delivery);

        final float scale = getResources().getDisplayMetrics().density;
        params = new LinearLayout.LayoutParams((int)(60*scale), (int)(60*scale));

        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose item");

        builder.setItems(toppings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ImageView currenttopping = new ImageView(MainActivity.this);
                params.weight = 0.0f;
                pb.setProgress(++numberofToppings,true);
                currenttopping.setLayoutParams(params);
                currenttopping.setImageDrawable(getDrawable(drawablelist.get(which)));
                toppingsorderlist.add(toppings[which]);
                currenttopping.setTag(toppings[which]);
                currenttopping.setOnClickListener(MainActivity.this);
                if(numberofToppings <= 5) {
                    toppingsrow1.addView(currenttopping);
                }
                else if(numberofToppings <= 10){
                    toppingsrow2.addView(currenttopping);
                }

                if(numberofToppings >= 10) {
                    Toast toast = Toast.makeText(MainActivity.this, "Maximum Capacity!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        clearpizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberofToppings = 0;
                toppingsrow1.removeAllViews();
                toppingsrow2.removeAllViews();
                delivery.setChecked(false);
                pb.setProgress(0);
                toppingsorderlist.clear();
            }
        });

        dialog = builder.create();

        addtopping = findViewById(R.id.addtopping);

        addtopping.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if(numberofToppings >= 10) {
                    Toast toast = Toast.makeText(MainActivity.this, "Maximum Topping capacity reached!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    dialog.show();
                }
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toppingsorderlist.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please add atleast one topping to checkout",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                intent.putStringArrayListExtra(TOPPINGS, (ArrayList<String>) toppingsorderlist);
                intent.putExtra(CHECKBOX, delivery.isChecked());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view) {
        LinearLayout currentSelectedLayout = findViewById(((View) view.getParent()).getId());
        currentSelectedLayout.removeView(view);
        numberofToppings = numberofToppings >= 10 ? 9 : numberofToppings-1;
        pb.setProgress(numberofToppings);
        toppingsorderlist.remove(view.getTag());
        System.out.println(toppingsorderlist);


        if(currentSelectedLayout == toppingsrow1 && toppingsrow2.getChildCount() != 0) {
            ImageView firstview = (ImageView) toppingsrow2.getChildAt(0);
            toppingsrow2.removeView(firstview);
            toppingsrow1.addView(firstview);
        }

    }
}
