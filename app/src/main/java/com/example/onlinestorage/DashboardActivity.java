package com.example.onlinestorage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {

    Button LogOut;
    EditText txt_directorate;
    EditText txt_rdsospecs;
    EditText txt_vendor;
    EditText txt_vendorid;
    EditText txt_fileno;
    EditText txt_itemname;
    String EmailHolder;
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
    String HttpURL = "http://192.168.1.100:8585/OnlineStorage/UserRegistration.php";
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    ProgressDialog progressDialog;
    String finalResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        select_file = findViewById(R.id.select_file);
        LogOut = findViewById(R.id.button);
        txt_directorate = findViewById(R.id.directorate);
        txt_vendor = findViewById(R.id.vendor);
        txt_vendorid = findViewById(R.id.vendorid);
        txt_fileno = findViewById(R.id.fileno);
        txt_itemname = findViewById(R.id.itemname);
        txt_rdsospecs = findViewById(R.id.rdsospecs);
        upload = findViewById(R.id.upload);

        upload.setOnClickListener(view -> {

            // Checking whether EditText is Empty or Not
            CheckEditTextIsEmptyOrNot();

            if (CheckEditText) {

                // If EditText is not empty and CheckEditText = True then this block will execute.

                UserRegisterFunction(vendor, vendorid, fileno, itemname, rdsospecs, Directorate, User);

            } else {

                // If EditText is empty then this block will execute .
                Toast.makeText(DashboardActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

            }


        });

        select_file.setOnClickListener(v -> imageChooser());


        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra("UserEmail");

        Directorate = intent.getStringExtra("Directorate");
        txt_directorate.setText(Directorate);

        User = intent.getStringExtra("User");

        //Toast.makeText(DashboardActivity.this, Directorate+" - "+User, Toast.LENGTH_LONG).show();

        LogOut.setOnClickListener(view -> {

            finish();

            Intent intent1 = new Intent(DashboardActivity.this, UserLoginActivity.class);

            startActivity(intent1);

            Toast.makeText(DashboardActivity.this, "Log Out Successfully", Toast.LENGTH_LONG).show();


        });

//        txt_directorate.setOnFocusChangeListener((v, hasFocus) -> {
//            if (!hasFocus) {
//                hideKeyboard(v);
//            }
//        });

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

    }

    public void CheckEditTextIsEmptyOrNot() {

        vendor = txt_vendor.getText().toString();
        vendorid = txt_vendorid.getText().toString();
        fileno = txt_fileno.getText().toString();
        itemname = txt_itemname.getText().toString();
        rdsospecs = txt_rdsospecs.getText().toString();


        CheckEditText = !TextUtils.isEmpty(vendor) && !TextUtils.isEmpty(vendorid) && !TextUtils.isEmpty(fileno) && !TextUtils.isEmpty(itemname);

    }

    public void UserRegisterFunction(final String vendor, final String vendorid, final String fileno, final String itemname, final String rdsospecs, final String directorate, final String user) {

        class UserRegisterFunctionClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(DashboardActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(DashboardActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("vendor", params[0]);

                hashMap.put("vendorid", params[1]);

                hashMap.put("fileno", params[2]);

                hashMap.put("itemname", params[3]);

                hashMap.put("rdsospecs", params[4]);

                hashMap.put("directorate", params[5]);

                hashMap.put("user", params[6]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();

        userRegisterFunctionClass.execute(vendor, vendorid, fileno, itemname, rdsospecs, directorate, user);
    }

    void imageChooser() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivity(Intent.createChooser(i, "Select File"));
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
