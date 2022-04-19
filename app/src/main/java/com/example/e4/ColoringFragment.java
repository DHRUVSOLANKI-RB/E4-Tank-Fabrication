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
    String HttpURLGetColoring = "http://3.222.104.176/index.php/getcoloringdata";
    HashMap<String, String> hashMap = new HashMap<>();
    com.example.e4.HttpParse httpParse = new com.example.e4.HttpParse();
    ProgressDialog progressDialog;
    String finalResult;
    String finalResult_GetColoring;

    EditText coloring_team;
    CheckBox grinding_check,primer,putty,rubbing_check,base_color,branding_color,cabin_color;
    String txt_coloring_team = "",txt_grinding_check = "",txt_primer = "",txt_putty = "",txt_rubbing_check = "",txt_base_color = "",txt_branding_color = "",
            txt_cabin_color = "",vehicle_unid = "",serial_no = "",txt_sno = "";

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

        SharedPreferences sharedpreferences_2 = getActivity().getSharedPreferences(VehicleStepsFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        vehicle_unid = sharedpreferences_2.getString("vehicle_unid","");

        SharedPreferences sharedpreferences_1 = getActivity().getSharedPreferences(DashboardFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        serial_no = sharedpreferences_1.getString("serial_no","");

        if(!serial_no.equals("")){
            //Toast.makeText(getActivity(),vehicle_unid, Toast.LENGTH_SHORT).show();
            GetColoringData();
        }else{
            //Toast.makeText(getActivity(),"empty", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(HomeFragment.MyPREFERENCES, Context.MODE_PRIVATE);
            vehicle_unid = sharedpreferences.getString("unid","");
        }

        next_coloring.setOnClickListener(view -> {

            txt_coloring_team = coloring_team.getText().toString();
            if(grinding_check.isChecked())
                txt_grinding_check = grinding_check.getText().toString();
            if(primer.isChecked())
                txt_primer = primer.getText().toString();
            if(putty.isChecked())
                txt_putty = putty.getText().toString();
            if(rubbing_check.isChecked())
                txt_rubbing_check = rubbing_check.getText().toString();
            if(base_color.isChecked())
                txt_base_color = base_color.getText().toString();
            if(branding_color.isChecked())
                txt_branding_color = branding_color.getText().toString();
            if(cabin_color.isChecked())
                txt_cabin_color = cabin_color.getText().toString();

            //Navigation.findNavController(view).navigate(R.id.nav_touchup);

            UserLoginFunction(view);
        });

        return root;
    }

    public void GetColoringData() {

        class GetColoringDataClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(getActivity(), "Loading", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                //progressDialog.dismiss();

                //Toast.makeText(getActivity(),httpResponseMsg, Toast.LENGTH_LONG).show();

                try {
                    JSONObject jsonObject = new JSONObject(httpResponseMsg);

                    if (jsonObject.getString("status").equals("success")){

                        JSONObject jsonObject_data = new JSONObject(jsonObject.getString("data"));

                        System.out.println(jsonObject_data);

                        coloring_team.setText(jsonObject_data.getString("coloring_team"));
                        if(jsonObject_data.getString("grinding_check").equals("1"))
                            grinding_check.setChecked(true);
                        if(jsonObject_data.getString("primer").equals("1"))
                            primer.setChecked(true);
                        if(jsonObject_data.getString("putty").equals("1"))
                            putty.setChecked(true);
                        if(jsonObject_data.getString("rubbing_check").equals("1"))
                            rubbing_check.setChecked(true);
                        if(jsonObject_data.getString("base_color").equals("1"))
                            base_color.setChecked(true);
                        if(jsonObject_data.getString("branding_color").equals("1"))
                            branding_color.setChecked(true);
                        if(jsonObject_data.getString("cabin_color").equals("1"))
                            cabin_color.setChecked(true);

                        txt_sno = jsonObject_data.getString("sno");

                        progressDialog.dismiss();

                    }else if (jsonObject.getString("status").equals("error")) {

                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("vehicle_unid", vehicle_unid);

                finalResult_GetColoring = httpParse.postRequest(hashMap, HttpURLGetColoring);

                System.out.println(finalResult_GetColoring);

                return finalResult_GetColoring;
            }
        }

        GetColoringDataClass getColoringDataClass = new GetColoringDataClass();
        getColoringDataClass.execute();
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

                                //Navigation.findNavController(view).navigate(R.id.nav_touchup);
                                if(!serial_no.equals("")){
                                    Navigation.findNavController(view).navigate(R.id.nav_dashboard);
                                }else{
                                    Navigation.findNavController(view).navigate(R.id.nav_touchup);
                                }

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
                hashMap.put("sno", txt_sno);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                Log.d("doInBackground: ", finalResult);
                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute();
    }
}