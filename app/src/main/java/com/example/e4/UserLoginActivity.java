package com.example.e4;

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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UserLoginActivity extends AppCompatActivity {

    EditText Email, Password;
    Button LogIn;
    String PasswordHolder, EmailHolder;
    String finalResult;
    String HttpURL = "http://3.222.104.176/index.php/userlogin";
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    com.example.e4.HttpParse httpParse = new com.example.e4.HttpParse();
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
        Intent intent = new Intent(com.example.e4.UserLoginActivity.this, com.example.e4.MainActivity.class);
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

                progressDialog = ProgressDialog.show(com.example.e4.UserLoginActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                //Toast.makeText(com.example.e4.UserLoginActivity.this,httpResponseMsg, Toast.LENGTH_LONG).show();

                try {
                    JSONObject jsonObject = new JSONObject(httpResponseMsg);
                    String error = jsonObject.getString("status");

                    if(error.equals("error")) {

                        progressDialog.dismiss();

                        new MaterialAlertDialogBuilder(com.example.e4.UserLoginActivity.this).setTitle(jsonObject.getString("statusMessage")).setPositiveButton("Ok", (dialogInterface, i) -> {

                        }).show();

                    }else if(error.equals("success")) {

                        finish();

                        String user_data = jsonObject.getString("data");

                        JSONObject user_jsonObject = new JSONObject(user_data);

                        //Toast.makeText(com.example.e4.UserLoginActivity.this,user_jsonObject.getString("uname"), Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = sp_login.edit();

                        editor.putBoolean("logged", true);
                        editor.putString("uname", user_jsonObject.getString("uname"));
                        //editor.putString("Directorate", response[1]);
                        //editor.putString("User", response[2]);
                        editor.apply();

                        goToMainActivity();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
