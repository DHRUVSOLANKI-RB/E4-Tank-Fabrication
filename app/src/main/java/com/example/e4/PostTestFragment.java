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

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.e4.ui.upload.HomeFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PostTestFragment extends Fragment {

    String HttpURL = "http://3.222.104.176/index.php/post_test";
    String HttpURLGetPost = "http://3.222.104.176/index.php/getposttestdata";
    HashMap<String, String> hashMap = new HashMap<>();
    com.example.e4.HttpParse httpParse = new com.example.e4.HttpParse();
    ProgressDialog progressDialog;
    String finalResult;
    String finalResult_GetPost;
    CheckBox outer_assembly,lever_arrangement,locking,tank_top,alluminium_work,stainless_steel_work,dip_plate,cleaning;
    String txt_outer_assembly = "",txt_lever_arrangement = "",txt_locking = "",txt_tank_top = "",txt_alluminium_work = "",txt_stainless_steel_work = "",txt_dip_plate = ""
            ,txt_cleaning = "",vehicle_unid = "",serial_no = "",txt_sno = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_post_test, container, false);

        Button prev_posttest = root.findViewById(R.id.prev_posttest);
        Button next_posttest = root.findViewById(R.id.next_posttest);
        outer_assembly = root.findViewById(R.id.outer_assembly);
        lever_arrangement = root.findViewById(R.id.lever_arrangement);
        locking = root.findViewById(R.id.locking);
        tank_top = root.findViewById(R.id.tank_top);
        alluminium_work = root.findViewById(R.id.alluminium_work);
        stainless_steel_work = root.findViewById(R.id.stainless_steel_work);
        dip_plate = root.findViewById(R.id.dip_plate);
        cleaning = root.findViewById(R.id.cleaning);

        prev_posttest.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_leaktest);
        });

        SharedPreferences sharedpreferences_2 = getActivity().getSharedPreferences(VehicleStepsFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        vehicle_unid = sharedpreferences_2.getString("vehicle_unid","");

        SharedPreferences sharedpreferences_1 = getActivity().getSharedPreferences(DashboardFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        serial_no = sharedpreferences_1.getString("serial_no","");

        if(!serial_no.equals("")){
            //Toast.makeText(getActivity(),vehicle_unid, Toast.LENGTH_SHORT).show();
            GetPostData();
        }else{
            //Toast.makeText(getActivity(),"empty", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(HomeFragment.MyPREFERENCES, Context.MODE_PRIVATE);
            vehicle_unid = sharedpreferences.getString("unid","");
        }

        next_posttest.setOnClickListener(view -> {

            if(outer_assembly.isChecked())
                txt_outer_assembly = outer_assembly.getText().toString();
            if(lever_arrangement.isChecked())
                txt_lever_arrangement = lever_arrangement.getText().toString();
            if(locking.isChecked())
                txt_locking = locking.getText().toString();
            if(tank_top.isChecked())
                txt_tank_top = tank_top.getText().toString();
            if(alluminium_work.isChecked())
                txt_alluminium_work = alluminium_work.getText().toString();
            if(stainless_steel_work.isChecked())
                txt_stainless_steel_work = stainless_steel_work.getText().toString();
            if(dip_plate.isChecked())
                txt_dip_plate = dip_plate.getText().toString();
            if(cleaning.isChecked())
                txt_cleaning = cleaning.getText().toString();

            UserLoginFunction(view);

            //Navigation.findNavController(view).navigate(R.id.nav_checklista);
        });

        return root;
    }

    public void GetPostData() {

        class GetPostDataClass extends AsyncTask<String, Void, String> {

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

                        if(jsonObject_data.getString("outer_assembly").equals("1"))
                            outer_assembly.setChecked(true);
                        if(jsonObject_data.getString("lever_arrangement").equals("1"))
                            lever_arrangement.setChecked(true);
                        if(jsonObject_data.getString("locking").equals("1"))
                            locking.setChecked(true);
                        if(jsonObject_data.getString("tank_top").equals("1"))
                            tank_top.setChecked(true);
                        if(jsonObject_data.getString("alluminium_work").equals("1"))
                            alluminium_work.setChecked(true);
                        if(jsonObject_data.getString("stainless_steel_work").equals("1"))
                            stainless_steel_work.setChecked(true);
                        if(jsonObject_data.getString("dip_plate").equals("1"))
                            dip_plate.setChecked(true);
                        if(jsonObject_data.getString("cleaning").equals("1"))
                            cleaning.setChecked(true);

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

                finalResult_GetPost = httpParse.postRequest(hashMap, HttpURLGetPost);

                System.out.println(finalResult_GetPost);

                return finalResult_GetPost;
            }
        }

        GetPostDataClass getPostDataClass = new GetPostDataClass();
        getPostDataClass.execute();
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

                                if(!serial_no.equals("")){
                                    Navigation.findNavController(view).navigate(R.id.nav_dashboard);
                                }else{
                                    Navigation.findNavController(view).navigate(R.id.nav_checklista);
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

                hashMap.put("outer_assembly", txt_outer_assembly);
                hashMap.put("lever_arrangement", txt_lever_arrangement);
                hashMap.put("locking", txt_locking);
                hashMap.put("tank_top", txt_tank_top);
                hashMap.put("alluminium_work", txt_alluminium_work);
                hashMap.put("stainless_steel_work", txt_stainless_steel_work);
                hashMap.put("dip_plate", txt_dip_plate);
                hashMap.put("cleaning", txt_cleaning);
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