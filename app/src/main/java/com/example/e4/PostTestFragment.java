package com.example.e4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


public class PostTestFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_post_test, container, false);

        Button prev_posttest = root.findViewById(R.id.prev_posttest);
        Button next_posttest = root.findViewById(R.id.next_posttest);

        prev_posttest.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_leaktest);
        });

        next_posttest.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_checklista);
        });

        return root;
    }
}