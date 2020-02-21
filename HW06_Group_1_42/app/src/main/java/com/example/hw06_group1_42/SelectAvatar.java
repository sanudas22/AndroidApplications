package com.example.hw06_group1_42;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class SelectAvatar extends Fragment {

    ImageView f1, f2, f3, m1, m2, m3;
    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Demo", "SelectAvatar oncreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_avatar, container, false);
        f1 = view.findViewById(R.id.f1);
        f2 = view.findViewById(R.id.f2);
        f3 = view.findViewById(R.id.f3);
        m1 = view.findViewById(R.id.m1);
        m2 = view.findViewById(R.id.m2);
        m3 = view.findViewById(R.id.m3);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.applySelectedAvatar(R.drawable.avatar_f_1, 1);
            }
        });
        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.applySelectedAvatar(R.drawable.avatar_f_2, 2);
            }
        });
        f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.applySelectedAvatar(R.drawable.avatar_f_3, 3);
            }
        });
        m1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.applySelectedAvatar(R.drawable.avatar_m_1, 4);
            }
        });
        m2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.applySelectedAvatar(R.drawable.avatar_m_2, 5);
            }
        });
        m3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.applySelectedAvatar(R.drawable.avatar_m_3, 6);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void applySelectedAvatar(int id, int num);
    }
}
