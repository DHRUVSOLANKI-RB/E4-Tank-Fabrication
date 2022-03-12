package com.example.e4.ui.report;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.e4.R;
import com.example.e4.UserLoginActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddDrawingFragment extends Fragment {

    AutoCompleteTextView loading;
    EditText drawing_no,compartment,letter_number,approving_capacity;
    Button save;
    String txt_drawing_no = "",txt_compartment = "",txt_letter_number = "",txt_approving_capacity = "",txt_loading = "",user_id = "";
    String[] arr_loading = {"Top", "Bottom"};

    String HttpURL = "http://3.222.104.176/index.php/drawing";
    HashMap<String, String> hashMap = new HashMap<>();
    com.example.e4.HttpParse httpParse = new com.example.e4.HttpParse();
    ProgressDialog progressDialog;
    String finalResult;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_drawing, container, false);

        save = root.findViewById(R.id.save);
        drawing_no = root.findViewById(R.id.drawing_no);
        compartment = root.findViewById(R.id.compartment);
        letter_number = root.findViewById(R.id.letter_number);
        approving_capacity = root.findViewById(R.id.approving_capacity);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, arr_loading);
        loading = (AutoCompleteTextView) root.findViewById(R.id.loading);
        loading.setThreshold(1);
        loading.setAdapter(adapter);
        loading.setInputType(InputType.TYPE_NULL);
        loading.setTextColor(Color.BLACK);

        SharedPreferences sharedpreferences_1 = getActivity().getSharedPreferences(UserLoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = sharedpreferences_1.getString("user_id","");

        //Toast.makeText(getContext().getApplicationContext(), user_id, Toast.LENGTH_SHORT).show();

        save.setOnClickListener(view -> {

            txt_drawing_no = drawing_no.getText().toString();
            txt_compartment = compartment.getText().toString();
            txt_letter_number = letter_number.getText().toString();
            txt_approving_capacity = approving_capacity.getText().toString();
            txt_loading = loading.getText().toString();

            UserLoginFunction(view);
        });

        return root;
    }

    public void UserLoginFunction(View view) {

        class UserLoginClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(getActivity(), "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                //Toast.makeText(com.example.e4.UserLoginActivity.this,httpResponseMsg, Toast.LENGTH_LONG).show();

                try {
                    JSONObject jsonObject = new JSONObject(httpResponseMsg);

                    new MaterialAlertDialogBuilder(getContext()).setTitle(jsonObject.getString("statusMessage")).setPositiveButton("Ok", (dialogInterface, i) -> {

                        String error = null;
                        try {
                            error = jsonObject.getString("status");

                            if (error.equals("success")) {

                                progressDialog.dismiss();

                                Navigation.findNavController(view).navigate(R.id.nav_drawing);

//                                    filename.setText("");
//                                    txt_rdsospecs.getText().clear();
//                                    txt_vendor.getText().clear();
//                                    txt_vendorid.getText().clear();
//                                    txt_fileno.getText().clear();
//                                    txt_itemname.getText().clear();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("drawing_no", txt_drawing_no);
                hashMap.put("compartment", txt_compartment);
                hashMap.put("letter_number", txt_letter_number);
                hashMap.put("loading", txt_loading);
                hashMap.put("approving_capacity", txt_approving_capacity);
                hashMap.put("user_id", user_id);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                Log.d("doInBackground: ", finalResult);
                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute();
    }
}