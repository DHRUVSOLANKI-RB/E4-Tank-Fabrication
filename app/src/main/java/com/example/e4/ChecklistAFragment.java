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

public class ChecklistAFragment extends Fragment {

    CheckBox line_box,lever_box,fire_screen,u_bolts,mud_guards,hose_pipe_stand,fire_ext_stand,dip_rod_stand,earthing,locking,dip_plate,tank_cleaning,shutoff_valve,
            safety_fitting,top_cover,gasket,grinding_release;

    String txt_line_box = "",txt_lever_box = "",txt_fire_screen = "",txt_u_bolts = "",txt_mud_guards = "",txt_hose_pipe_stand = "",txt_fire_ext_stand = "",
            txt_dip_rod_stand = "",txt_earthing = "",txt_locking = "",txt_dip_plate = "",txt_tank_cleaning = "",txt_shutoff_valve = "",txt_safety_fitting = "",
            txt_top_cover = "",txt_gasket = "",txt_grinding_release = "",vehicle_unid = "",serial_no = "",txt_sno = "";

    String HttpURL = "http://3.222.104.176/index.php/checklista";
    String HttpURLGetChecklistA = "http://3.222.104.176/index.php/getchecklistadata";
    HashMap<String, String> hashMap = new HashMap<>();
    com.example.e4.HttpParse httpParse = new com.example.e4.HttpParse();
    ProgressDialog progressDialog;
    String finalResult;
    String finalResult_GetChecklistA;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_checklist_a, container, false);

        Button prev_posttest = root.findViewById(R.id.prev_posttest);
        Button next_posttest = root.findViewById(R.id.next_posttest);

        line_box = root.findViewById(R.id.line_box);
        lever_box = root.findViewById(R.id.lever_box);
        fire_screen = root.findViewById(R.id.fire_screen);
        u_bolts = root.findViewById(R.id.u_bolts);
        mud_guards = root.findViewById(R.id.mud_guards);
        hose_pipe_stand = root.findViewById(R.id.hose_pipe_stand);
        fire_ext_stand = root.findViewById(R.id.fire_ext_stand);
        dip_rod_stand = root.findViewById(R.id.dip_rod_stand);
        earthing = root.findViewById(R.id.earthing);
        locking = root.findViewById(R.id.locking);
        dip_plate = root.findViewById(R.id.dip_plate);
        tank_cleaning = root.findViewById(R.id.tank_cleaning);
        shutoff_valve = root.findViewById(R.id.shutoff_valve);
        safety_fitting = root.findViewById(R.id.safety_fitting);
        top_cover = root.findViewById(R.id.top_cover);
        gasket = root.findViewById(R.id.gasket);
        grinding_release = root.findViewById(R.id.grinding_release);

        prev_posttest.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_post_test);
        });

        SharedPreferences sharedpreferences_2 = getActivity().getSharedPreferences(VehicleStepsFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        vehicle_unid = sharedpreferences_2.getString("vehicle_unid","");

        SharedPreferences sharedpreferences_1 = getActivity().getSharedPreferences(DashboardFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        serial_no = sharedpreferences_1.getString("serial_no","");

        if(!serial_no.equals("")){
            //Toast.makeText(getActivity(),vehicle_unid, Toast.LENGTH_SHORT).show();
            GetChecklistAData();
        }else{
            //Toast.makeText(getActivity(),"empty", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(HomeFragment.MyPREFERENCES, Context.MODE_PRIVATE);
            vehicle_unid = sharedpreferences.getString("unid","");
        }

        next_posttest.setOnClickListener(view -> {

            if(line_box.isChecked())
                txt_line_box = line_box.getText().toString();
            if(lever_box.isChecked())
                txt_lever_box = lever_box.getText().toString();
            if(fire_screen.isChecked())
                txt_fire_screen = fire_screen.getText().toString();
            if(u_bolts.isChecked())
                txt_u_bolts = u_bolts.getText().toString();
            if(mud_guards.isChecked())
                txt_mud_guards = mud_guards.getText().toString();
            if(hose_pipe_stand.isChecked())
                txt_hose_pipe_stand = hose_pipe_stand.getText().toString();
            if(fire_ext_stand.isChecked())
                txt_fire_ext_stand = fire_ext_stand.getText().toString();
            if(dip_rod_stand.isChecked())
                txt_dip_rod_stand = dip_rod_stand.getText().toString();
            if(earthing.isChecked())
                txt_earthing = earthing.getText().toString();
            if(locking.isChecked())
                txt_locking = locking.getText().toString();
            if(dip_plate.isChecked())
                txt_dip_plate = dip_plate.getText().toString();
            if(tank_cleaning.isChecked())
                txt_tank_cleaning = tank_cleaning.getText().toString();
            if(shutoff_valve.isChecked())
                txt_shutoff_valve = shutoff_valve.getText().toString();
            if(safety_fitting.isChecked())
                txt_safety_fitting = safety_fitting.getText().toString();
            if(top_cover.isChecked())
                txt_top_cover = top_cover.getText().toString();
            if(gasket.isChecked())
                txt_gasket = gasket.getText().toString();
            if(grinding_release.isChecked())
                txt_grinding_release = grinding_release.getText().toString();

            UserLoginFunction(view);
            //Navigation.findNavController(view).navigate(R.id.nav_coloring);
        });

        return root;
    }

    public void GetChecklistAData() {

        class GetChecklistADataClass extends AsyncTask<String, Void, String> {

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

                        if(jsonObject_data.getString("line_box").equals("1"))
                            line_box.setChecked(true);
                        if(jsonObject_data.getString("lever_box").equals("1"))
                            lever_box.setChecked(true);
                        if(jsonObject_data.getString("fire_screen").equals("1"))
                            fire_screen.setChecked(true);
                        if(jsonObject_data.getString("u_bolts").equals("1"))
                            u_bolts.setChecked(true);
                        if(jsonObject_data.getString("mud_guards").equals("1"))
                            mud_guards.setChecked(true);
                        if(jsonObject_data.getString("hose_pipe_stand").equals("1"))
                            hose_pipe_stand.setChecked(true);
                        if(jsonObject_data.getString("fire_ext_stand").equals("1"))
                            fire_ext_stand.setChecked(true);
                        if(jsonObject_data.getString("dip_rod_stand").equals("1"))
                            dip_rod_stand.setChecked(true);
                        if(jsonObject_data.getString("earthing").equals("1"))
                            earthing.setChecked(true);
                        if(jsonObject_data.getString("locking").equals("1"))
                            locking.setChecked(true);
                        if(jsonObject_data.getString("dip_plate").equals("1"))
                            dip_plate.setChecked(true);
                        if(jsonObject_data.getString("tank_cleaning").equals("1"))
                            tank_cleaning.setChecked(true);
                        if(jsonObject_data.getString("shutoff_valve").equals("1"))
                            shutoff_valve.setChecked(true);
                        if(jsonObject_data.getString("safety_fitting").equals("1"))
                            safety_fitting.setChecked(true);
                        if(jsonObject_data.getString("top_cover").equals("1"))
                            top_cover.setChecked(true);
                        if(jsonObject_data.getString("gasket").equals("1"))
                            gasket.setChecked(true);
                        if(jsonObject_data.getString("grinding_release").equals("1"))
                            grinding_release.setChecked(true);

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

                finalResult_GetChecklistA = httpParse.postRequest(hashMap, HttpURLGetChecklistA);

                System.out.println(finalResult_GetChecklistA);

                return finalResult_GetChecklistA;
            }
        }

        GetChecklistADataClass getChecklistADataClass = new GetChecklistADataClass();
        getChecklistADataClass.execute();
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
                                    Navigation.findNavController(view).navigate(R.id.nav_coloring);
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

                hashMap.put("line_box", txt_line_box);
                hashMap.put("lever_box", txt_lever_box);
                hashMap.put("fire_screen", txt_fire_screen);
                hashMap.put("u_bolts", txt_u_bolts);
                hashMap.put("mud_guards", txt_mud_guards);
                hashMap.put("hose_pipe_stand", txt_hose_pipe_stand);
                hashMap.put("fire_ext_stand", txt_fire_ext_stand);
                hashMap.put("dip_rod_stand", txt_dip_rod_stand);
                hashMap.put("earthing", txt_earthing);
                hashMap.put("locking", txt_locking);
                hashMap.put("dip_plate", txt_dip_plate);
                hashMap.put("tank_cleaning", txt_tank_cleaning);
                hashMap.put("shutoff_valve", txt_shutoff_valve);
                hashMap.put("safety_fitting", txt_safety_fitting);
                hashMap.put("top_cover", txt_top_cover);
                hashMap.put("gasket", txt_gasket);
                hashMap.put("grinding_release", txt_grinding_release);
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