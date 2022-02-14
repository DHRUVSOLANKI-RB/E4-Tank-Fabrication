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


public class PlanningFragment extends Fragment {

    TextView team_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_planning, container, false);

        team_date = root.findViewById(R.id.team_date);
        Button select_date = root.findViewById(R.id.select_date);
        Button next_proc = root.findViewById(R.id.next_proc);
        Button prev_proc = root.findViewById(R.id.prev_proc);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT DATE");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        select_date.setOnClickListener(v -> {
            materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        materialDatePicker.addOnPositiveButtonClickListener(
                selection -> team_date.setText(materialDatePicker.getHeaderText()));

        prev_proc.setOnClickListener(view -> {

            //Toast.makeText(getActivity(), "Please fill all form fields.", Toast.LENGTH_LONG).show();
            Navigation.findNavController(view).navigate(R.id.nav_home);
        });

        next_proc.setOnClickListener(view -> {

            //Toast.makeText(getActivity(), "Please fill all form fields.", Toast.LENGTH_LONG).show();
            Navigation.findNavController(view).navigate(R.id.nav_tankfab);
        });

        return root;
    }
}