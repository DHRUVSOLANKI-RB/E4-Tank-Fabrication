package com.example.e4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


public class ColoringFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_coloring, container, false);

        Button prev_coloring = root.findViewById(R.id.prev_coloring);
        Button next_coloring = root.findViewById(R.id.next_coloring);

        prev_coloring.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_checklista);
        });

        next_coloring.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_touchup);
        });

        return root;
    }
}