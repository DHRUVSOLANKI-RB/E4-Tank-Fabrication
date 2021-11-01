package com.example.onlinestorage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class UserLoginActivity extends AppCompatActivity {

    EditText Email, Password;
    Button LogIn;
    String PasswordHolder, EmailHolder;
    String finalResult;
    String HttpURL = "http://rdso.rcil.gov.in:8090/OnlineStorage/UserLogin.php";
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    com.example.onlinestorage.HttpParse httpParse = new com.example.onlinestorage.HttpParse();
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sp_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        LogIn = findViewById(R.id.Login);

        sp_login = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (sp_login.getBoolean("logged", false)) {
            goToMainActivity();
        }

        LogIn.setOnClickListener(view -> {

            CheckEditTextIsEmptyOrNot();

            if (CheckEditText) {

                UserLoginFunction(EmailHolder, PasswordHolder);

            } else {

                Toast.makeText(UserLoginActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

            }

        });

        Email.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        Password.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
    }

    public void goToMainActivity() {
        Intent intent = new Intent(com.example.onlinestorage.UserLoginActivity.this, com.example.onlinestorage.MainActivity.class);
        startActivity(intent);
    }

    public void CheckEditTextIsEmptyOrNot() {

        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();

        CheckEditText = !TextUtils.isEmpty(EmailHolder) && !TextUtils.isEmpty(PasswordHolder);
    }

    public void UserLoginFunction(final String email, final String password) {

        class UserLoginClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(com.example.onlinestorage.UserLoginActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                String[] response = httpResponseMsg.split("-");

                //Toast.makeText(com.example.onlinestorage.UserLoginActivity.this,response[0] +" - "+ response[1] +" - "+ response[2], Toast.LENGTH_LONG).show();

                if (response[0].equalsIgnoreCase("Data Matched")) {

                    finish();

                    SharedPreferences.Editor editor = sp_login.edit();

                    editor.putBoolean("logged", true);
                    editor.putString("UserEmail", email);
                    editor.putString("Directorate", response[1]);
                    editor.putString("User", response[2]);
                    editor.apply();

                    goToMainActivity();

                } else {

                    Toast.makeText(com.example.onlinestorage.UserLoginActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email", params[0]);

                hashMap.put("password", params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(email, password);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
