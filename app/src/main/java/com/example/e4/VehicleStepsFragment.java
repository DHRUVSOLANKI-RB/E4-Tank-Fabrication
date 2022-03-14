package com.example.e4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VehicleStepsFragment extends Fragment {

    String serial_no = "";
    TextView txt_serial_no;
    String HttpURLGet = "http://3.222.104.176/index.php/getvehiclesteps";
    HashMap<String, String> hashMap = new HashMap<>();
    com.example.e4.HttpParse httpParse = new com.example.e4.HttpParse();
    ProgressDialog progressDialog;
    String user_id = "";
    String finalResult;
    Button btn_vehicle_in,btn_planning,btn_tank_fabrication,btn_fittings_pre_test,btn_leak_test,btn_post_test_fittings,btn_checklist_a,btn_coloring,btn_touch_up_stage,btn_checklist_pre_delivery;
    LinearLayout layout_planning,layout_tank_fabrication,layout_fittings_pre_test,layout_leak_test,layout_post_test_fittings,layout_checklist_a,layout_coloring,layout_touch_up_stage,layout_checklist_pre_delivery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_vehicle_steps, container, false);

        txt_serial_no = root.findViewById(R.id.serial_no);
        btn_vehicle_in = root.findViewById(R.id.btn_vehicle_in);
        btn_planning = root.findViewById(R.id.btn_planning);
        btn_tank_fabrication = root.findViewById(R.id.btn_tank_fabrication);
        btn_fittings_pre_test = root.findViewById(R.id.btn_fittings_pre_test);
        btn_leak_test = root.findViewById(R.id.btn_leak_test);
        btn_post_test_fittings = root.findViewById(R.id.btn_post_test_fittings);
        btn_checklist_a = root.findViewById(R.id.btn_checklist_a);
        btn_coloring = root.findViewById(R.id.btn_coloring);
        btn_touch_up_stage = root.findViewById(R.id.btn_touch_up_stage);
        btn_checklist_pre_delivery = root.findViewById(R.id.btn_checklist_pre_delivery);

        layout_planning = root.findViewById(R.id.layout_planning);
        layout_tank_fabrication = root.findViewById(R.id.layout_tank_fabrication);
        layout_fittings_pre_test = root.findViewById(R.id.layout_fittings_pre_test);
        layout_leak_test = root.findViewById(R.id.layout_leak_test);
        layout_post_test_fittings = root.findViewById(R.id.layout_post_test_fittings);
        layout_checklist_a = root.findViewById(R.id.layout_checklist_a);
        layout_coloring = root.findViewById(R.id.layout_coloring);
        layout_touch_up_stage = root.findViewById(R.id.layout_touch_up_stage);
        layout_checklist_pre_delivery = root.findViewById(R.id.layout_checklist_pre_delivery);

        GetSerialNo();

        SharedPreferences sharedpreferences_1 = getActivity().getSharedPreferences(DashboardFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        serial_no = sharedpreferences_1.getString("serial_no","");

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(UserLoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = sharedpreferences.getString("user_id","");

        txt_serial_no.setText(serial_no);
        //Toast.makeText(getActivity(),serial_no, Toast.LENGTH_SHORT).show();

        return root;
    }

    public void GetSerialNo() {

        class GetSerialNoClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(getActivity(), "Loading", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                //Toast.makeText(getActivity(),httpResponseMsg, Toast.LENGTH_LONG).show();

                try {
                    JSONObject jsonObject = new JSONObject(httpResponseMsg);

                    if (jsonObject.getString("status").equals("success")){

                        JSONArray json_data = new JSONArray(jsonObject.getString("data").split(","));

                        System.out.println(json_data);

                        for(int i = 0; i<json_data.length(); i++){
                            if (json_data.getString(i).equals("planning")){
                                //System.out.println("planning");
                                layout_planning.setVisibility(View.VISIBLE);
                            }else if (json_data.getString(i).equals("tankfab")){
                                //System.out.println("tankfab");
                                layout_tank_fabrication.setVisibility(View.VISIBLE);
                            }else if (json_data.getString(i).equals("fitting")){
                                //System.out.println("fitting");
                                layout_fittings_pre_test.setVisibility(View.VISIBLE);
                            }else if (json_data.getString(i).equals("leak_test")){
                                //System.out.println("leak_test");
                                layout_leak_test.setVisibility(View.VISIBLE);
                            }else if (json_data.getString(i).equals("post_test")){
                                //System.out.println("post_test");
                                layout_post_test_fittings.setVisibility(View.VISIBLE);
                            }else if (json_data.getString(i).equals("checklist_a")){
                                //System.out.println("checklist_a");
                                layout_checklist_a.setVisibility(View.VISIBLE);
                            }else if (json_data.getString(i).equals("coloring")){
                                //System.out.println("coloring");
                                layout_coloring.setVisibility(View.VISIBLE);
                            }else if (json_data.getString(i).equals("touchup")){
                                //System.out.println("touchup");
                                layout_touch_up_stage.setVisibility(View.VISIBLE);
                            }else if (json_data.getString(i).equals("checkpredelivery")){
                                //System.out.println("checkpredelivery");
                                layout_checklist_pre_delivery.setVisibility(View.VISIBLE);
                            }else if (json_data.getString(i).equals("")){
                                //System.out.println("checkpredelivery");
                                layout_planning.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("user_id", user_id);
                hashMap.put("serial_no", serial_no);

                finalResult = httpParse.postRequest(hashMap, HttpURLGet);

                System.out.println(finalResult);

                return finalResult;
            }
        }

        GetSerialNoClass getSerialNoClass = new GetSerialNoClass();
        getSerialNoClass.execute();
    }
}