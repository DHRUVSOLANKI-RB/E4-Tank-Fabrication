package com.example.e4.ui.upload;

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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.example.e4.R;
import com.example.e4.UserLoginActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment<array_uri> extends Fragment {

    EditText txt_directorate;
    EditText txt_rdsospecs;
    EditText txt_vendor;
    EditText txt_vendorid;
    EditText txt_fileno;
    EditText txt_itemname;
    TextView filename;
    String Directorate;
    String User;
    Button select_file;
    Button upload;
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
    Spinner AMC_section;

    String[] fruits = {"Apple", "Banana", "Cherry", "Date", "Grape", "Kiwi", "Mango", "Pear"};

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_upload, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //select_file = root.findViewById(R.id.select_file);
        txt_directorate = root.findViewById(R.id.make);
        txt_vendor = root.findViewById(R.id.model);
        txt_vendorid = root.findViewById(R.id.bs_type);
        txt_fileno = root.findViewById(R.id.wheelbase);
        txt_itemname = root.findViewById(R.id.ladenwgt);
        txt_rdsospecs = root.findViewById(R.id.unladenwgt);
        upload = root.findViewById(R.id.upload);

        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, fruits);
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView actv = (AutoCompleteTextView) root.findViewById(R.id.category);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.RED);


//
//        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tss_list);
//        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        AMC_section.setAdapter(dataAdapter1);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(UserLoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);

        //User = sharedpreferences.getString("User", "");
        //Directorate = sharedpreferences.getString("Directorate", "");

        txt_directorate.setText(Directorate);

        txt_vendor.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        txt_vendorid.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        txt_fileno.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        txt_itemname.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        txt_rdsospecs.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        upload.setOnClickListener(view -> {



        });

        return root;
    }


    public void CheckEditTextIsEmptyOrNot() {

        vendor = txt_vendor.getText().toString();
        vendorid = txt_vendorid.getText().toString();
        fileno = txt_fileno.getText().toString();
        itemname = txt_itemname.getText().toString();
        rdsospecs = txt_rdsospecs.getText().toString();

        CheckEditText = !TextUtils.isEmpty(vendor) && !TextUtils.isEmpty(vendorid) && !TextUtils.isEmpty(fileno) && !TextUtils.isEmpty(itemname) && !TextUtils.isEmpty(get_filename);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}