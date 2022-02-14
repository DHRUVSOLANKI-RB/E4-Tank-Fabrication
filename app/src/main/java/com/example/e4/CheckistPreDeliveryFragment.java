package com.example.e4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


public class CheckistPreDeliveryFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_checkist_pre_delivery, container, false);

        Button prev_predelivery = root.findViewById(R.id.prev_predelivery);
        Button next_predelivery = root.findViewById(R.id.next_predelivery);

        prev_predelivery.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_touchup);
        });

        next_predelivery.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_checklistpredelivery);
        });

        return root;
    }
}