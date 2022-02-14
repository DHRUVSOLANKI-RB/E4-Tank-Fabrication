package com.example.e4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class TouchupFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_touchup, container, false);

        Button prev_touchup = root.findViewById(R.id.prev_touchup);
        Button next_touchup = root.findViewById(R.id.next_touchup);

        prev_touchup.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_coloring);
        });

        next_touchup.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_checklistpredelivery);
        });

        return root;
    }
}