package com.example.e4.ui.upload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.volley.RequestQueue;
import com.example.e4.R;
import com.example.e4.UserLoginActivity;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment<array_uri> extends Fragment {

    EditText make;
    EditText unladenwgt;
    EditText model;
    EditText wheelbase;
    EditText ladenwgt;
    TextView indate;
    TextView delivery_date;
    String Directorate;
    AutoCompleteTextView category;
    AutoCompleteTextView bs_type;
    AutoCompleteTextView vehicle_type;
    String User;
    Button select_file;
    String vendor;
    String vendorid;
    String fileno;
    String itemname;
    String rdsospecs;
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    String get_filename = "";
    String all_filename = "";
    Uri uri;
    HashMap<String, String> array_file_uri = new HashMap<>();
    float file_size = (float) 0.00;
    boolean file_alert = false;

    String count_loop = "";

    String displayName = null;
    private ArrayList<HashMap<String, String>> arraylist;
    private RequestQueue rQueue;

    String[] arr_category = {"Bio MRO", "Mobile Bowser", "DD Bowser", "TL Tank", "BL Tank", "Tank Shifting", "Storage Tank"};

    String[] arr_bstype = {"BSIII", "BSIV", "BSVI"};

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_vehicle, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //select_file = root.findViewById(R.id.select_file);
        make = root.findViewById(R.id.make);
        model = root.findViewById(R.id.model);
        wheelbase = root.findViewById(R.id.wheelbase);
        ladenwgt = root.findViewById(R.id.ladenwgt);
        unladenwgt = root.findViewById(R.id.unladenwgt);
        //upload = root.findViewById(R.id.upload);
        indate = root.findViewById(R.id.indate);
        Button select_in_date = root.findViewById(R.id.select_in_date);
        Button select_delivery_date = root.findViewById(R.id.select_delivery_date);
        delivery_date = root.findViewById(R.id.delivery_date);

        final Button upload = (Button) root.findViewById(R.id.upload);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, arr_category);
        category = (AutoCompleteTextView) root.findViewById(R.id.category);
        category.setThreshold(1);
        category.setAdapter(adapter);
        category.setTextColor(Color.BLACK);

        ArrayAdapter<String> adapter_bs_type = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, arr_bstype);
        bs_type = (AutoCompleteTextView) root.findViewById(R.id.bs_type);
        bs_type.setThreshold(1);
        bs_type.setAdapter(adapter_bs_type);
        bs_type.setTextColor(Color.BLACK);

        ArrayAdapter<String> adapter_vehicle_type = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, arr_category);
        vehicle_type = (AutoCompleteTextView) root.findViewById(R.id.vehicle_type);
        vehicle_type.setThreshold(1);
        vehicle_type.setAdapter(adapter_vehicle_type);
        vehicle_type.setTextColor(Color.BLACK);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT IN DATE");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        select_in_date.setOnClickListener((View.OnClickListener) v -> {
            materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        materialDatePicker.addOnPositiveButtonClickListener(
                selection -> indate.setText(materialDatePicker.getHeaderText()));

        /*      Expected Delivery Date    */

        MaterialDatePicker.Builder materialDateBuilder_deldt = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder_deldt.setTitleText("SELECT IN DATE");

        final MaterialDatePicker materialDatePicker_deldt = materialDateBuilder_deldt.build();

        select_delivery_date.setOnClickListener((View.OnClickListener) v -> {
            materialDatePicker_deldt.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        materialDatePicker_deldt.addOnPositiveButtonClickListener(
                selection -> delivery_date.setText(materialDatePicker_deldt.getHeaderText()));


        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(UserLoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);

        upload.setOnClickListener(view -> {

            //Toast.makeText(getActivity(), "Please fill all form fields.", Toast.LENGTH_LONG).show();

            Navigation.findNavController(view).navigate(R.id.nav_planning);

        });

        return root;
    }


    public void CheckEditTextIsEmptyOrNot() {

        vendor = model.getText().toString();
        vendorid = bs_type.getText().toString();
        fileno = wheelbase.getText().toString();
        itemname = ladenwgt.getText().toString();
        rdsospecs = unladenwgt.getText().toString();

        CheckEditText = !TextUtils.isEmpty(vendor) && !TextUtils.isEmpty(vendorid) && !TextUtils.isEmpty(fileno) && !TextUtils.isEmpty(itemname) && !TextUtils.isEmpty(get_filename);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}