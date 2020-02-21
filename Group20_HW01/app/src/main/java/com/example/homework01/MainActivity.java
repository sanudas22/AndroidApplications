
package com.example.homework01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * a. Assignment #3
 * b. File Name.: Group20_HW01
 * c. Full name of students of Group 20.: SANU DAS, AKHIL CHUNDARATHIL, RAVI THEJA GOALLA
 */
public class MainActivity extends AppCompatActivity {

    private TextView tv_resultBMI;
    private TextView tv_resultBMIStatus;
    private EditText et_weight;
    private EditText et_heightInches;
    private EditText et_heightFeet;
    private Button button_calculate;
    double weight;
    float height_inches;
    int height_feet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("BMI Calculator");

        tv_resultBMI = findViewById(R.id.tv_resultBMI);
        tv_resultBMIStatus = findViewById(R.id.tv_resultBMIStatus);
        et_weight = findViewById(R.id.et_weight);
        et_heightFeet = findViewById(R.id.et_heightFeet);
        et_heightInches = findViewById(R.id.et_heightInches);
        button_calculate = findViewById(R.id.button_calculate);

        button_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_resultBMI.setText("");
                tv_resultBMIStatus.setText("");
                et_heightInches.setError(null);

                String tempWeight = et_weight.getText().toString();
                String tempHeightInches = et_heightInches.getText().toString();
                String tempHeightFeet = et_heightFeet.getText().toString();

                if(null!=tempWeight && !tempWeight.equals("")){
                    weight = Float.parseFloat(tempWeight);
                    if(weight == 0) {
                        errorToast();
                        et_weight.setError("Please enter a valid weight");
                        return;
                    }
                } else {
                    errorToast();
                    et_weight.setError("Please enter a valid weight");
                    return;
                }
                if(null!=tempHeightFeet && !tempHeightFeet.equals("")){
                    height_feet = Integer.parseInt(tempHeightFeet);
                } else {
                    errorToast();
                    et_heightFeet.setError("Please enter a valid value for feet");
                    return;
                }
                if(null!=tempHeightInches && !tempHeightInches.equals("")){
                    height_inches = Float.parseFloat(tempHeightInches);
                } else {
                    errorToast();
                    et_heightInches.setError("Please enter a valid value for inches");
                    return;
                }

                if ((height_feet == 0 && height_inches == 0) || height_inches > 11) {
                    errorToast();
                    et_heightInches.setError("Please enter a valid value for inches");
                    return;
                }

                Toast toast = Toast.makeText(getApplicationContext(),"BMI Calculated", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();

                float totalHeightInInches = (height_feet*12) + height_inches;
                double result = weight*703/(totalHeightInInches*totalHeightInInches);
                tv_resultBMI.setText(String.format("Your BMI: %.2f",result));
                if(result<18.5) {
                    tv_resultBMIStatus.setText("You are Underweight");
                } else if((18.5<=result) && (result<= 24.9)) {
                    tv_resultBMIStatus.setText("You are Normal weight");
                } else if((25<=result) && (result<= 29.9)) {
                    tv_resultBMIStatus.setText("You are Overweight");
                } else if (30<=result ){
                    tv_resultBMIStatus.setText("You are Obese");
                }

            }
        });
    }

    public void errorToast() {
        Toast toast = Toast.makeText(getApplicationContext(),"Invalid Input", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
