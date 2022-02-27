package com.example.e4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.e4.ui.upload.HomeFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ColoringFragment extends Fragment {

    String HttpURL = "http://3.222.104.176/index.php/coloring";
    HashMap<String, String> hashMap = new HashMap<>();
    com.example.e4.HttpParse httpParse = new com.example.e4.HttpParse();
    ProgressDialog progressDialog;
    String finalResult;

    EditText coloring_team;
    CheckBox grinding_check,primer,putty,rubbing_check,base_color,branding_color,cabin_color;
    String txt_coloring_team = "",txt_grinding_check = "",txt_primer = "",txt_putty = "",txt_rubbing_check = "",txt_base_color = "",txt_branding_color = "",
            txt_cabin_color = "",vehicle_unid = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_coloring, container, false);

        Button prev_coloring = root.findViewById(R.id.prev_coloring);
        Button next_coloring = root.findViewById(R.id.next_coloring);

        coloring_team = root.findViewById(R.id.coloring_team);
        grinding_check = root.findViewById(R.id.grinding_check);
        primer = root.findViewById(R.id.primer);
        putty = root.findViewById(R.id.putty);
        rubbing_check = root.findViewById(R.id.rubbing_check);
        base_color = root.findViewById(R.id.base_color);
        branding_color = root.findViewById(R.id.branding_color);
        cabin_color = root.findViewById(R.id.cabin_color);

        prev_coloring.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_checklista);
        });

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(HomeFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        vehicle_unid = sharedpreferences.getString("unid","");

        next_coloring.setOnClickListener(view -> {

            txt_coloring_team = coloring_team.getText().toString();
            txt_grinding_check = grinding_check.getText().toString();
            txt_primer = primer.getText().toString();
            txt_putty = putty.getText().toString();
            txt_rubbing_check = rubbing_check.getText().toString();
            txt_base_color = base_color.getText().toString();
            txt_branding_color = branding_color.getText().toString();
            txt_cabin_color = cabin_color.getText().toString();

            //Navigation.findNavController(view).navigate(R.id.nav_touchup);

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

                                Navigation.findNavController(view).navigate(R.id.nav_touchup);

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

                hashMap.put("coloring_team", txt_coloring_team);
                hashMap.put("grinding_check", txt_grinding_check);
                hashMap.put("primer", txt_primer);
                hashMap.put("putty", txt_putty);
                hashMap.put("rubbing_check", txt_rubbing_check);
                hashMap.put("base_color", txt_base_color);
                hashMap.put("branding_color", txt_branding_color);
                hashMap.put("cabin_color", txt_cabin_color);
                hashMap.put("vehicle_unid", vehicle_unid);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                Log.d("doInBackground: ", finalResult);
                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute();
    }
}