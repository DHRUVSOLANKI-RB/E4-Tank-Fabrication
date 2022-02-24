package com.example.e4;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FittingFragment extends Fragment {

    TextView invoice_date_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_fitting, container, false);

        invoice_date_text = root.findViewById(R.id.invoice_date_text);
        Button invoice_date = root.findViewById(R.id.invoice_date);
        Button next_fitting = root.findViewById(R.id.next_fitting);
        Button prev_fitting = root.findViewById(R.id.prev_fitting);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate1 = df.format(c);
        invoice_date_text.setText(formattedDate1);

        MaterialDatePicker.Builder materialDateBuilder_mounted = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder_mounted.setTitleText("ASSIGNED DATE");

        final MaterialDatePicker materialDatePicker_mounted = materialDateBuilder_mounted.build();

        invoice_date.setOnClickListener(v -> {
            materialDatePicker_mounted.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        materialDatePicker_mounted.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate  = format.format(calendar.getTime());
            invoice_date_text.setText(formattedDate);
        });

        prev_fitting.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_tankfab);
        });

        next_fitting.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_leaktest);
        });

        return root;
    }
}