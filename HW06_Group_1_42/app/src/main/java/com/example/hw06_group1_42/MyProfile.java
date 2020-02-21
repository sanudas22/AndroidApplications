package com.example.hw06_group1_42;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyProfile extends Fragment {
    private ImageView avatar;
    private EditText fname, lname, studentid;
    private Button save;
    private OnFragmentInteractionListener mListener;
    private RadioGroup rd_selection;
    private RadioButton rb_CS, rb_SIS, rb_BIO, rb_Other;
    public int drawable;
    public int itemNum;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Map<Integer, Integer> imageMap = new HashMap<>();
        imageMap.put(0, R.drawable.select_image);
        imageMap.put(1, R.drawable.avatar_f_1);
        imageMap.put(2, R.drawable.avatar_f_2);
        imageMap.put(3, R.drawable.avatar_f_3);
        imageMap.put(4, R.drawable.avatar_m_1);
        imageMap.put(5, R.drawable.avatar_m_2);
        imageMap.put(6, R.drawable.avatar_m_3);

        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        fname = view.findViewById(R.id.fname);
        lname = view.findViewById(R.id.lname);
        studentid = view.findViewById(R.id.studentid);
        save = view.findViewById(R.id.save);
        avatar = view.findViewById(R.id.avatar);
        rd_selection = view.findViewById(R.id.rg);
        rb_BIO = view.findViewById(R.id.rb_BIO);
        rb_CS = view.findViewById(R.id.rb_CS);
        rb_SIS = view.findViewById(R.id.rb_SIS);
        rb_Other = view.findViewById(R.id.rb_Other);
        final Student student = new Student();
        rd_selection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rb_CS:
                        student.department = "CS";
                        break;
                    case R.id.rb_SIS:
                        student.department = "SIS";
                        break;
                    case R.id.rb_BIO:
                        student.department = "BIO";
                        break;
                    case R.id.rb_Other:
                        student.department = "Other";
                        break;
                    default:
                        break;
                }
            }
        });
        Log.d("Demo", "num@p"+itemNum);
        Log.d("Demo", "drr@p  "+drawable);
        if (drawable > 0 ) {
            avatar.setImageResource(drawable);
        }
        if (itemNum >0) {
            student.image = itemNum;
        }
        Log.d("Demo", "im@p"+student.image);
        if (student.firstName != null){
            fname.setText(student.firstName);
        }
        if (student.lastName != null){
            lname.setText(student.lastName);
        }
        if (student.studentId != null){
            studentid.setText(student.studentId);
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fname.getText().toString().equals("") || lname.getText().toString().equals("") || studentid.getText().equals("") || student.department.equals("")) {
                    Toast.makeText(getContext(), "All fields are mandatory, please enter the missing value!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!studentid.getText().equals("") && studentid.getText().length() != 9) {
                    Toast.makeText(getContext(), "Please enter a valid 9 digit Student Id!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Log.d("Demo", student.image+"imgCount");
                if(student.image == 0) {
                    Toast.makeText(getContext(), "Please select Student Image!", Toast.LENGTH_SHORT).show();
                    return;
                }
                student.firstName = fname.getText().toString();
                student.lastName = lname.getText().toString();
                student.studentId = studentid.getText().toString();
                avatar.setImageResource(drawable);

                for (Map.Entry<Integer, Integer> entry : imageMap.entrySet()) {

                    if (Objects.equals(drawable, entry.getValue())) {
                        student.image = entry.getKey();
                    }
                }

                mListener.saveData(student);
            }
        });
        if(student.image<=0) {
            mListener.loadData(student);
        }
        updateViews(student);
        return view;
    }

    private void updateViews(Student student) {
        fname.setText(student.firstName);
        lname.setText(student.lastName);
        studentid.setText(student.studentId);
        switch (student.department) {
            case "CS":
                rb_CS.setChecked(true);
                break;
            case "SIS":
                rb_SIS.setChecked(true);
                break;
            case "BIO":
                rb_BIO.setChecked(true);
                break;
            case "Other":
                rb_Other.setChecked(true);
                break;
            default:
                break;
        }
        //Log.d("Demo", student.image+"switch");
        switch (student.image) {
            case 0:
                avatar.setImageResource(R.drawable.select_image);
                break;
            case 1:
                avatar.setImageResource(R.drawable.avatar_f_1);
                break;
            case 2:
                avatar.setImageResource(R.drawable.avatar_f_2);
                break;
            case 3:
                avatar.setImageResource(R.drawable.avatar_f_3);
                break;
            case 4:
                avatar.setImageResource(R.drawable.avatar_m_1);
                break;
            case 5:
                avatar.setImageResource(R.drawable.avatar_m_2);
                break;
            case 6:
                avatar.setImageResource(R.drawable.avatar_m_3);
                break;
            default:
                avatar.setImageResource(R.drawable.select_image);
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Log.d("Demo", "Clicked");
                mListener.gotoSelectAvatar();

            }
        });
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void changeImage(Drawable drawable) {
        avatar.setImageDrawable(drawable);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void gotoSelectAvatar();
        void saveData(Student student);
        void loadData(Student student);
    }
}
