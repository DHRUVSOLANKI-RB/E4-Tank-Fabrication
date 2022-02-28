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


public class CheckistPreDeliveryFragment extends Fragment {

    String HttpURL = "http://3.222.104.176/index.php/checklistpredelivery";
    HashMap<String, String> hashMap = new HashMap<>();
    com.example.e4.HttpParse httpParse = new com.example.e4.HttpParse();
    ProgressDialog progressDialog;
    String finalResult;
    
    CheckBox tank_mounting,safety_fittings,valve_lever,locking,color,sticker,number_plates,accessories;
    String txt_tank_mounting = "",txt_safety_fittings = "",txt_valve_lever = "",txt_locking = "",txt_color = "",txt_sticker = "",txt_number_plates = "",
            txt_accessories = "",vehicle_unid = "";
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_checkist_pre_delivery, container, false);

        Button prev_predelivery = root.findViewById(R.id.prev_predelivery);
        Button next_predelivery = root.findViewById(R.id.next_predelivery);

        tank_mounting = root.findViewById(R.id.tank_mounting);
        safety_fittings = root.findViewById(R.id.safety_fittings);
        valve_lever = root.findViewById(R.id.valve_lever);
        locking = root.findViewById(R.id.locking);
        color = root.findViewById(R.id.color);
        sticker = root.findViewById(R.id.sticker);
        number_plates = root.findViewById(R.id.number_plates);
        accessories = root.findViewById(R.id.accessories);
        
        prev_predelivery.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_touchup);
        });

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(HomeFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        vehicle_unid = sharedpreferences.getString("unid","");

        next_predelivery.setOnClickListener(view -> {

            if(tank_mounting.isChecked())
                txt_tank_mounting = tank_mounting.getText().toString();
            if(safety_fittings.isChecked())
                txt_safety_fittings = safety_fittings.getText().toString();
            if(valve_lever.isChecked())
                txt_valve_lever = valve_lever.getText().toString();
            if(locking.isChecked())
                txt_locking = locking.getText().toString();
            if(color.isChecked())
                txt_color = color.getText().toString();
            if(accessories.isChecked())
                txt_accessories = accessories.getText().toString();
            if(number_plates.isChecked())
                txt_number_plates = number_plates.getText().toString();
            if(sticker.isChecked())
                txt_sticker = sticker.getText().toString();

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

                                Navigation.findNavController(view).navigate(R.id.nav_checklistpredelivery);

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

                hashMap.put("tank_mounting", txt_tank_mounting);
                hashMap.put("safety_fittings", txt_safety_fittings);
                hashMap.put("valve_lever", txt_valve_lever);
                hashMap.put("locking", txt_locking);
                hashMap.put("color", txt_color);
                hashMap.put("sticker", txt_sticker);
                hashMap.put("number_plates", txt_number_plates);
                hashMap.put("accessories", txt_accessories);
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