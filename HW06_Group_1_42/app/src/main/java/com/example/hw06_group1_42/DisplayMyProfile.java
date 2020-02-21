package com.example.hw06_group1_42;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class DisplayMyProfile extends Fragment {

    TextView name, id, department;
    Button edit;
    ImageView image;

    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_my_profile, container, false);
        name = view.findViewById(R.id.name);
        id = view.findViewById(R.id.id);
        department = view.findViewById(R.id.dept);
        edit = view.findViewById(R.id.edit);
        image = view.findViewById(R.id.image);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFragmentManager().getBackStackEntryCount()> 0) {
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyProfile(), "MyProfile_F").addToBackStack(null).commit();
                }
            }
        });
        Student student = new Student();
        mListener.loadData(student);
        updateViews(student);
        return view;
    }

    private void updateViews(Student student) {
        name.setText("Name : "+student.firstName+ " "+student.lastName);
        id.setText("Student ID : "+student.studentId);
        department.setText("Department : "+student.department);

        Log.d("Demo", student.image+"switch");
        switch (student.image) {
            case 0:
                //avatar.setBackground(getResources().getDrawable(R.drawable.select_image));
                break;
            case 1:
                image.setImageResource(R.drawable.avatar_f_1);
                break;
            case 2:
                image.setImageResource(R.drawable.avatar_f_2);
                break;
            case 3:
                image.setImageResource(R.drawable.avatar_f_3);
                break;
            case 4:
                image.setImageResource(R.drawable.avatar_m_1);
                break;
            case 5:
                image.setImageResource(R.drawable.avatar_m_2);
                break;
            case 6:
                image.setImageResource(R.drawable.avatar_m_3);
                break;
            default:
                break;
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MyProfile profile = (MyProfile) getActivity().getSupportFragmentManager().findFragmentByTag("MyProfile_F");
        if(profile != null) {
            Student student = MainActivity.student;
            name.setText("Name : "+student.firstName+ " "+student.lastName);
            id.setText("Student ID : "+student.studentId);
            //image.setImageResource(profile.drawable);
            switch (student.image) {
                case 0:
                    //avatar.setBackground(getResources().getDrawable(R.drawable.select_image));
                    break;
                case 1:
                    image.setImageResource(R.drawable.avatar_f_1);
                    break;
                case 2:
                    image.setImageResource(R.drawable.avatar_f_2);
                    break;
                case 3:
                    image.setImageResource(R.drawable.avatar_f_3);
                    break;
                case 4:
                    image.setImageResource(R.drawable.avatar_m_1);
                    break;
                case 5:
                    image.setImageResource(R.drawable.avatar_m_2);
                    break;
                case 6:
                    image.setImageResource(R.drawable.avatar_m_3);
                    break;
                default:
                    break;
            }
            department.setText("Department : "+student.department);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void loadData(Student student);
    }
}
