package com.example.e4;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.e4.ui.upload.HomeFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class FittingFragment extends Fragment {

    TextView invoice_date_text;
    String compdistri = "",txt_received_by = "",txt_assigned_to = "",txt_assigned_date = "",txt_comp11 = "",txt_comp12 = "",txt_comp13 = "",txt_comp14 = "",txt_comp21 = ""
            ,txt_comp22 = "",txt_comp23 = "",txt_comp24 = "",txt_comp31 = "",txt_comp32 = "",txt_comp33 = "",txt_comp34 = "",txt_comp41 = "",txt_comp42 = "",txt_comp43 = ""
            ,txt_comp44 = "",txt_comp51 = "",txt_comp52 = "",txt_comp53 = "",txt_comp54 = "",txt_comp61 = "",txt_comp62 = "", txt_comp63 = "",txt_comp64 = "",
            txt_safety_invoice_no = "",txt_safety_verified_by = "",txt_material_available = "",txt_safety_fittings = "",txt_pipe_line = "",txt_aluminium_material = ""
            ,txt_steel_material = "",txt_extra_accessories = "",txt_cutting_bending = "",txt_leak_test = "",vehicle_unid = "",user_id = "",txt_invoice_date_text = ""
            ,txt_sno = "",comp_sno = "",serial_no = "";
    LinearLayout layout_1,layout_2,layout_3,layout_4,layout_5,layout_6;
    EditText received_by,assigned_to,assigned_date,comp11,comp12,comp13,comp14,comp21,comp22,comp23,comp24,comp31,comp32,comp33,comp34,comp41,comp42,comp43,comp44,comp51,
            comp52,comp53,comp54,comp61,comp62,comp63,comp64,safety_invoice_no,safety_verified_by;
    RadioGroup rg_material_available,rg_safety_fittings,rg_pipe_line,rg_aluminium_material,rg_steel_material,rg_extra_accessories,rg_cutting_bending,rg_leak_test;
    RadioButton material_available,safety_fittings,pipe_line,aluminium_material,steel_material,extra_accessories,cutting_bending,leak_test,material_available_no,
            safety_fittings_no,pipe_line_no,aluminium_material_no,steel_material_no,extra_accessories_no,cutting_bending_no,leak_test_no;

    String HttpURL = "http://3.222.104.176/index.php/fitting";
    String HttpURLGetFitting = "http://3.222.104.176/index.php/getfittingdata";
    HashMap<String, String> hashMap = new HashMap<>();
    com.example.e4.HttpParse httpParse = new com.example.e4.HttpParse();
    ProgressDialog progressDialog;
    String finalResult;
    String finalResult_GetFitting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_fitting, container, false);

        invoice_date_text = root.findViewById(R.id.invoice_date_text);
        Button invoice_date = root.findViewById(R.id.invoice_date);
        Button next_fitting = root.findViewById(R.id.next_fitting);
        Button prev_fitting = root.findViewById(R.id.prev_fitting);
        layout_1 = root.findViewById(R.id.layout_1);
        layout_2 = root.findViewById(R.id.layout_2);
        layout_3 = root.findViewById(R.id.layout_3);
        layout_4 = root.findViewById(R.id.layout_4);
        layout_5 = root.findViewById(R.id.layout_5);
        layout_6 = root.findViewById(R.id.layout_6);

        received_by = root.findViewById(R.id.received_by);
        assigned_to = root.findViewById(R.id.assigned_to);
        assigned_date = root.findViewById(R.id.assigned_date);
        comp11 = root.findViewById(R.id.comp11);
        comp12 = root.findViewById(R.id.comp12);
        comp13 = root.findViewById(R.id.comp13);
        comp14 = root.findViewById(R.id.comp14);
        comp21 = root.findViewById(R.id.comp21);
        comp22 = root.findViewById(R.id.comp22);
        comp23 = root.findViewById(R.id.comp23);
        comp24 = root.findViewById(R.id.comp24);
        comp31 = root.findViewById(R.id.comp31);
        comp32 = root.findViewById(R.id.comp32);
        comp33 = root.findViewById(R.id.comp33);
        comp34 = root.findViewById(R.id.comp34);
        comp41 = root.findViewById(R.id.comp41);
        comp42 = root.findViewById(R.id.comp42);
        comp43 = root.findViewById(R.id.comp43);
        comp44 = root.findViewById(R.id.comp44);
        comp51 = root.findViewById(R.id.comp51);
        comp52 = root.findViewById(R.id.comp52);
        comp53 = root.findViewById(R.id.comp53);
        comp54 = root.findViewById(R.id.comp54);
        comp61 = root.findViewById(R.id.comp61);
        comp62 = root.findViewById(R.id.comp62);
        comp63 = root.findViewById(R.id.comp63);
        comp64 = root.findViewById(R.id.comp64);
        safety_invoice_no = root.findViewById(R.id.safety_invoice_no);
        safety_verified_by = root.findViewById(R.id.safety_verified_by);
        rg_material_available = root.findViewById(R.id.rg_material_available);
        rg_safety_fittings = root.findViewById(R.id.rg_safety_fittings);
        rg_pipe_line = root.findViewById(R.id.rg_pipe_line);
        rg_aluminium_material = root.findViewById(R.id.rg_aluminium_material);
        rg_steel_material = root.findViewById(R.id.rg_steel_material);
        rg_extra_accessories = root.findViewById(R.id.rg_extra_accessories);
        rg_cutting_bending = root.findViewById(R.id.rg_cutting_bending);
        rg_leak_test = root.findViewById(R.id.rg_leak_test);
        material_available_no = root.findViewById(R.id.material_available_no);
        safety_fittings_no = root.findViewById(R.id.safety_fittings_no);
        pipe_line_no = root.findViewById(R.id.pipe_line_no);
        aluminium_material_no = root.findViewById(R.id.aluminium_material_no);
        steel_material_no = root.findViewById(R.id.steel_material_no);
        extra_accessories_no = root.findViewById(R.id.extra_accessories_no);
        cutting_bending_no = root.findViewById(R.id.cutting_bending_no);
        leak_test_no = root.findViewById(R.id.leak_test_no);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate1 = df.format(c);
        invoice_date_text.setText(formattedDate1);

        MaterialDatePicker.Builder materialDateBuilder_mounted = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder_mounted.setTitleText("ASSIGNED DATE");

        final MaterialDatePicker materialDatePicker_mounted = materialDateBuilder_mounted.build();

        invoice_date.setOnClickListener(v -> {
            materialDatePicker_mounted.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        materialDatePicker_mounted.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate  = format.format(calendar.getTime());
            invoice_date_text.setText(formattedDate);
        });

        prev_fitting.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_tankfab);
        });

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(HomeFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        compdistri = sharedpreferences.getString("compdistri","");
        //vehicle_unid = sharedpreferences.getString("unid","");

        SharedPreferences sharedpreferences_1 = getActivity().getSharedPreferences(UserLoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = sharedpreferences_1.getString("user_id","");

        SharedPreferences sharedpreferences_2 = getActivity().getSharedPreferences(VehicleStepsFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        vehicle_unid = sharedpreferences_2.getString("vehicle_unid","");

        SharedPreferences sharedpreferences_3 = getActivity().getSharedPreferences(DashboardFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        serial_no = sharedpreferences_3.getString("serial_no","");

        if(!serial_no.equals("")){
            //Toast.makeText(getActivity(),vehicle_unid, Toast.LENGTH_SHORT).show();
            GetFittingData();
        }else{
            //Toast.makeText(getActivity(),"empty", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedpreferences3 = getActivity().getSharedPreferences(HomeFragment.MyPREFERENCES, Context.MODE_PRIVATE);
            vehicle_unid = sharedpreferences3.getString("unid","");
        }

        //Toast.makeText(getActivity(),compdistri, Toast.LENGTH_LONG).show();

        if(compdistri.equals("1")){

            layout_1.setVisibility(View.VISIBLE);
        }else if(compdistri.equals("2")){

            layout_1.setVisibility(View.VISIBLE);
            layout_2.setVisibility(View.VISIBLE);
        }else if(compdistri.equals("3")){

            layout_1.setVisibility(View.VISIBLE);
            layout_2.setVisibility(View.VISIBLE);
            layout_3.setVisibility(View.VISIBLE);
        }else if(compdistri.equals("4")){

            layout_1.setVisibility(View.VISIBLE);
            layout_2.setVisibility(View.VISIBLE);
            layout_3.setVisibility(View.VISIBLE);
            layout_4.setVisibility(View.VISIBLE);
        }else if(compdistri.equals("5")){

            layout_1.setVisibility(View.VISIBLE);
            layout_2.setVisibility(View.VISIBLE);
            layout_3.setVisibility(View.VISIBLE);
            layout_4.setVisibility(View.VISIBLE);
            layout_5.setVisibility(View.VISIBLE);
        }else if(compdistri.equals("6")){

            layout_1.setVisibility(View.VISIBLE);
            layout_2.setVisibility(View.VISIBLE);
            layout_3.setVisibility(View.VISIBLE);
            layout_4.setVisibility(View.VISIBLE);
            layout_5.setVisibility(View.VISIBLE);
            layout_6.setVisibility(View.VISIBLE);
        }else{
            layout_1.setVisibility(View.GONE);
            layout_2.setVisibility(View.GONE);
            layout_3.setVisibility(View.GONE);
            layout_4.setVisibility(View.GONE);
            layout_5.setVisibility(View.GONE);
            layout_6.setVisibility(View.GONE);
        }

        next_fitting.setOnClickListener(view -> {

            txt_received_by = received_by.getText().toString();
            txt_comp11 = comp11.getText().toString();
            txt_comp12 = comp12.getText().toString();
            txt_comp13 = comp13.getText().toString();
            txt_comp14 = comp14.getText().toString();
            txt_comp21 = comp21.getText().toString();
            txt_comp22 = comp22.getText().toString();
            txt_comp23 = comp23.getText().toString();
            txt_comp24 = comp24.getText().toString();
            txt_comp31 = comp31.getText().toString();
            txt_comp32 = comp32.getText().toString();
            txt_comp33 = comp33.getText().toString();
            txt_comp34 = comp34.getText().toString();
            txt_comp41 = comp41.getText().toString();
            txt_comp42 = comp42.getText().toString();
            txt_comp43 = comp43.getText().toString();
            txt_comp44 = comp44.getText().toString();
            txt_comp51 = comp51.getText().toString();
            txt_comp52 = comp52.getText().toString();
            txt_comp53 = comp53.getText().toString();
            txt_comp54 = comp54.getText().toString();
            txt_comp61 = comp61.getText().toString();
            txt_comp62 = comp62.getText().toString();
            txt_comp63 = comp63.getText().toString();
            txt_comp64 = comp64.getText().toString();
            txt_safety_invoice_no = safety_invoice_no.getText().toString();
            txt_safety_verified_by = safety_verified_by.getText().toString();
            txt_invoice_date_text = invoice_date_text.getText().toString();

            int material_id = rg_material_available.getCheckedRadioButtonId();
            material_available = (RadioButton) root.findViewById(material_id);
            txt_material_available = material_available.getText().toString();

            int safety_id = rg_safety_fittings.getCheckedRadioButtonId();
            safety_fittings = (RadioButton) root.findViewById(safety_id);
            txt_safety_fittings = safety_fittings.getText().toString();

            int pipe_id = rg_pipe_line.getCheckedRadioButtonId();
            pipe_line = (RadioButton) root.findViewById(pipe_id);
            txt_pipe_line = pipe_line.getText().toString();

            int aluminium_id = rg_aluminium_material.getCheckedRadioButtonId();
            aluminium_material = (RadioButton) root.findViewById(aluminium_id);
            txt_aluminium_material = aluminium_material.getText().toString();

            int steel_id = rg_steel_material.getCheckedRadioButtonId();
            steel_material = (RadioButton) root.findViewById(steel_id);
            txt_steel_material = steel_material.getText().toString();

            int extra_id = rg_extra_accessories.getCheckedRadioButtonId();
            extra_accessories = (RadioButton) root.findViewById(extra_id);
            txt_extra_accessories = extra_accessories.getText().toString();

            int cutting_id = rg_cutting_bending.getCheckedRadioButtonId();
            cutting_bending = (RadioButton) root.findViewById(cutting_id);
            txt_cutting_bending = cutting_bending.getText().toString();

            int leak_id = rg_leak_test.getCheckedRadioButtonId();
            leak_test = (RadioButton) root.findViewById(leak_id);
            txt_leak_test = leak_test.getText().toString();

            UserLoginFunction(view);
        });

        return root;
    }

    public void GetFittingData() {

        class GetFittingDataClass extends AsyncTask<String, Void, String> {

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

                        received_by.setText(jsonObject_data.getString("received_by"));

                        if(jsonObject_data.getString("material_available").equals("1"))
                            material_available_no.setChecked(true);
                        if(jsonObject_data.getString("safety_fittings").equals("1"))
                            safety_fittings_no.setChecked(true);
                        if(jsonObject_data.getString("pipe_line").equals("1"))
                            pipe_line_no.setChecked(true);
                        if(jsonObject_data.getString("aluminium_material").equals("1"))
                            aluminium_material_no.setChecked(true);
                        if(jsonObject_data.getString("steel_material").equals("1"))
                            steel_material_no.setChecked(true);
                        if(jsonObject_data.getString("extra_accessories").equals("1"))
                            extra_accessories_no.setChecked(true);
                        if(jsonObject_data.getString("cutting_bending").equals("1"))
                            cutting_bending_no.setChecked(true);
                        if(jsonObject_data.getString("leak_test").equals("1"))
                            leak_test_no.setChecked(true);
                        comp11.setText(jsonObject_data.getString("comp11"));
                        comp12.setText(jsonObject_data.getString("comp12"));
                        comp13.setText(jsonObject_data.getString("comp13"));
                        comp14.setText(jsonObject_data.getString("comp14"));
                        comp21.setText(jsonObject_data.getString("comp21"));
                        comp22.setText(jsonObject_data.getString("comp22"));
                        comp23.setText(jsonObject_data.getString("comp23"));
                        comp24.setText(jsonObject_data.getString("comp24"));
                        comp31.setText(jsonObject_data.getString("comp31"));
                        comp32.setText(jsonObject_data.getString("comp32"));
                        comp33.setText(jsonObject_data.getString("comp33"));
                        comp34.setText(jsonObject_data.getString("comp34"));
                        comp41.setText(jsonObject_data.getString("comp41"));
                        comp42.setText(jsonObject_data.getString("comp42"));
                        comp43.setText(jsonObject_data.getString("comp43"));
                        comp44.setText(jsonObject_data.getString("comp44"));
                        comp51.setText(jsonObject_data.getString("comp51"));
                        comp52.setText(jsonObject_data.getString("comp52"));
                        comp53.setText(jsonObject_data.getString("comp53"));
                        comp54.setText(jsonObject_data.getString("comp54"));
                        comp61.setText(jsonObject_data.getString("comp61"));
                        comp62.setText(jsonObject_data.getString("comp62"));
                        comp63.setText(jsonObject_data.getString("comp63"));
                        comp64.setText(jsonObject_data.getString("comp64"));
                        safety_invoice_no.setText(jsonObject_data.getString("safety_invoice_no"));
                        invoice_date_text.setText(jsonObject_data.getString("invoice_date"));
                        safety_verified_by.setText(jsonObject_data.getString("safety_verified_by"));
                        txt_sno = jsonObject_data.getString("fitting_sno");
                        comp_sno = jsonObject_data.getString("comp_sno");

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

                finalResult_GetFitting = httpParse.postRequest(hashMap, HttpURLGetFitting);

                System.out.println(finalResult_GetFitting);

                return finalResult_GetFitting;
            }
        }

        GetFittingDataClass getFittingDataClass = new GetFittingDataClass();
        getFittingDataClass.execute();
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

                                //Navigation.findNavController(view).navigate(R.id.nav_leaktest);

                                if(!serial_no.equals("")){
                                    Navigation.findNavController(view).navigate(R.id.nav_dashboard);
                                }else{
                                    Navigation.findNavController(view).navigate(R.id.nav_leaktest);
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

                hashMap.put("received_by", txt_received_by);
                hashMap.put("safety_invoice_no", txt_safety_invoice_no);
                hashMap.put("safety_verified_by", txt_safety_verified_by);
                hashMap.put("invoice_date", txt_invoice_date_text);
                hashMap.put("material_available", txt_material_available);
                hashMap.put("safety_fittings", txt_safety_fittings);
                hashMap.put("pipe_line", txt_pipe_line);
                hashMap.put("aluminium_material", txt_aluminium_material);
                hashMap.put("steel_material", txt_steel_material);
                hashMap.put("extra_accessories", txt_extra_accessories);
                hashMap.put("cutting_bending", txt_cutting_bending);
                hashMap.put("leak_test", txt_leak_test);
                hashMap.put("comp11", txt_comp11);
                hashMap.put("comp12", txt_comp12);
                hashMap.put("comp13", txt_comp13);
                hashMap.put("comp14", txt_comp14);
                hashMap.put("comp21", txt_comp21);
                hashMap.put("comp22", txt_comp22);
                hashMap.put("comp23", txt_comp23);
                hashMap.put("comp24", txt_comp24);
                hashMap.put("comp31", txt_comp31);
                hashMap.put("comp32", txt_comp32);
                hashMap.put("comp33", txt_comp33);
                hashMap.put("comp34", txt_comp34);
                hashMap.put("comp41", txt_comp41);
                hashMap.put("comp42", txt_comp42);
                hashMap.put("comp43", txt_comp43);
                hashMap.put("comp44", txt_comp44);
                hashMap.put("comp51", txt_comp51);
                hashMap.put("comp52", txt_comp52);
                hashMap.put("comp53", txt_comp53);
                hashMap.put("comp54", txt_comp54);
                hashMap.put("comp61", txt_comp61);
                hashMap.put("comp62", txt_comp62);
                hashMap.put("comp63", txt_comp63);
                hashMap.put("comp64", txt_comp64);
                hashMap.put("vehicle_unid", vehicle_unid);
                hashMap.put("sno", txt_sno);
                hashMap.put("comp_sno", comp_sno);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                Log.d("doInBackground: ", finalResult);
                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute();
    }
}