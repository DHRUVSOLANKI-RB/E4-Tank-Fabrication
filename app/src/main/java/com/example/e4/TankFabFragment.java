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

public class TankFabFragment extends Fragment {

    TextView mounted_date_text;
    TextView assigned_date_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tank_fab, container, false);

        mounted_date_text = root.findViewById(R.id.mounted_date_text);
        Button mounted_date = root.findViewById(R.id.mounted_date);
        assigned_date_text = root.findViewById(R.id.assigned_date_text);
        Button assigned_date = root.findViewById(R.id.assigned_date);
        Button next_tankfeb = root.findViewById(R.id.next_tankfeb);
        Button prev_tankfeb = root.findViewById(R.id.prev_tankfeb);

        MaterialDatePicker.Builder materialDateBuilder_mounted = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder_mounted.setTitleText("MOUNTED ON VEHICLE DATE");

        final MaterialDatePicker materialDatePicker_mounted = materialDateBuilder_mounted.build();

        mounted_date.setOnClickListener(v -> {
            materialDatePicker_mounted.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        materialDatePicker_mounted.addOnPositiveButtonClickListener(
                selection -> mounted_date_text.setText(materialDatePicker_mounted.getHeaderText()));

        /*     Assigned Date     */

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("ASSIGNED DATE");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        assigned_date.setOnClickListener(v -> {
            materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        materialDatePicker.addOnPositiveButtonClickListener(
                selection -> assigned_date_text.setText(materialDatePicker.getHeaderText()));

        prev_tankfeb.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_planning);
        });

        next_tankfeb.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_fitting);
        });

        return root;
    }
}