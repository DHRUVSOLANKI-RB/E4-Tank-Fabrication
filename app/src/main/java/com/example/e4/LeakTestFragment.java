package com.example.e4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.datepicker.MaterialDatePicker;

public class LeakTestFragment extends Fragment {

    TextView leak_date_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_leak_test, container, false);

        leak_date_text = root.findViewById(R.id.leak_date_text);
        Button leak_date = root.findViewById(R.id.leak_date);
        Button next_leaktest = root.findViewById(R.id.next_leaktest);
        Button prev_leaktest = root.findViewById(R.id.prev_leaktest);

        MaterialDatePicker.Builder materialDateBuilder_mounted = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder_mounted.setTitleText("ASSIGNED DATE");

        final MaterialDatePicker materialDatePicker_mounted = materialDateBuilder_mounted.build();

        leak_date.setOnClickListener(v -> {
            materialDatePicker_mounted.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        materialDatePicker_mounted.addOnPositiveButtonClickListener(
                selection -> leak_date_text.setText(materialDatePicker_mounted.getHeaderText()));


        prev_leaktest.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_fitting);
        });

        next_leaktest.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_leaktest);
        });

        return root;
    }
}