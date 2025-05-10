package com.example.appdoacoes.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.appdoacoes.R;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private String userType;
    private String userName;

    public ProfileFragment() {
        //
    }

    public static ProfileFragment newInstance(String userType, String userName) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("userType", userType);
        args.putString("userName", userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userType = getArguments().getString("userType");
            userName = getArguments().getString("userName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Log.d(TAG, "Criando ProfileFragment para: " + userType);

        TextView profileName = view.findViewById(R.id.textProfileName);
        TextView profileType = view.findViewById(R.id.textProfileType);

        profileName.setText(userName);
        profileType.setText(userType.equals("doador") ? "Tipo: Doador" : "Tipo: Instituição");

        return view;
    }
}