package com.example.hw06_group1_42;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * a. Homework 6
 * b. File Name.: HW06_Group1_42
 * c. Full name of students of Group1 42.: SANU DAS
 */
public class MainActivity extends AppCompatActivity implements MyProfile.OnFragmentInteractionListener,
        SelectAvatar.OnFragmentInteractionListener, DisplayMyProfile.OnFragmentInteractionListener {

    public static final String SHAREDPREFS = "shared_preferences";
    public static final String FNAME = "fname", LNAME = "lname", STUDID = "StudId", SELECTION = "selection";
    public static Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
        if(sharedPreferences.contains(FNAME)){
            this.getSupportFragmentManager().beginTransaction().add(R.id.container, new DisplayMyProfile(), "DisplayMyProfile_F").commit();
        } else {
            this.getSupportFragmentManager().beginTransaction().add(R.id.container, new MyProfile(), "MyProfile_F").commit();
        }
    }

    @Override
    public void gotoSelectAvatar() {
        this.getSupportFragmentManager().beginTransaction().replace(R.id.container, new SelectAvatar(), "SelectAvatar_F").addToBackStack(null).commit();
    }

    @Override
    public void saveData(Student student) {
        if(getFragmentManager().getBackStackEntryCount()> 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            this.getSupportFragmentManager().beginTransaction().replace(R.id.container, new DisplayMyProfile(), "DisplayMyProfile_F").addToBackStack(null).commit();
        }
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FNAME, student.firstName);
        editor.putString(LNAME, student.lastName);
        editor.putString(STUDID, student.studentId);
        editor.putString(SELECTION, student.department);

        editor.putInt("d", student.image);
        //Log.d("Demo", profile.selectedImage+"");
        editor.commit();
    }

    public void loadData(Student student) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
        this.student = student;
       // Student student = new Student();
        MyProfile profile = (MyProfile) this.getSupportFragmentManager().findFragmentByTag("MyProfile_F");
        student.firstName = sharedPreferences.getString(FNAME,"");
        student.lastName = sharedPreferences.getString(LNAME,"");
        student.studentId = sharedPreferences.getString(STUDID,"");
        student.department = sharedPreferences.getString(SELECTION,"");
        student.image = sharedPreferences.getInt("d", 0);
       // Log.d("Demo", student.image+"lo");
    }

    @Override
    public void applySelectedAvatar(int drawable, int num) {
        MyProfile profile = (MyProfile) this.getSupportFragmentManager().findFragmentByTag("MyProfile_F");
        if(profile != null) {
            profile.drawable = drawable;
            profile.itemNum = num;
            Log.d("Demo", "num"+profile.itemNum);
            getSupportFragmentManager().popBackStack();
        }
    }
}
