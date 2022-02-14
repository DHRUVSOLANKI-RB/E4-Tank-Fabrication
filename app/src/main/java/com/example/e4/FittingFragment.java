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

public class FittingFragment extends Fragment {

    TextView assigned_date_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_fitting, container, false);

        assigned_date_text = root.findViewById(R.id.assigned_date_text);
        Button assigned_date = root.findViewById(R.id.assigned_date);
        Button next_fitting = root.findViewById(R.id.next_fitting);
        Button prev_fitting = root.findViewById(R.id.prev_fitting);

        MaterialDatePicker.Builder materialDateBuilder_mounted = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder_mounted.setTitleText("ASSIGNED DATE");

        final MaterialDatePicker materialDatePicker_mounted = materialDateBuilder_mounted.build();

        assigned_date.setOnClickListener(v -> {
            materialDatePicker_mounted.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        materialDatePicker_mounted.addOnPositiveButtonClickListener(
                selection -> assigned_date_text.setText(materialDatePicker_mounted.getHeaderText()));

        prev_fitting.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_tankfab);
        });

        next_fitting.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_leaktest);
        });

        return root;
    }
}