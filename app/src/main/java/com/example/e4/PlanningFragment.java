package com.example.e4;

import android.annotation.SuppressLint;
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
import android.widget.CheckBox;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class PlanningFragment extends Fragment {

    TextView team_date;
    AutoCompleteTextView drawing_no;
    EditText payment_confirmation,cutting_remarks,team_name,remarks;
    RadioGroup rg_die_availability,rg_cutting_bending;
    RadioButton die_availability,cutting_bending;
    CheckBox tank_fabrication,fittings,other_accessories,need_to_apply;
    String txt_drawing_no = "",txt_need_to_apply = "",txt_payment_confirmation = "",txt_cutting_remarks = "", txt_team_name = "",txt_remarks = "",txt_die_availability = "",
            txt_cutting_bending = "",txt_tank_fabrication = "",txt_fittings = "",txt_other_accessories = "",vehicle_unid = "",txt_team_date = "",user_id = "";
    String HttpURL = "http://3.222.104.176/index.php/planning";
    String HttpURLGet = "http://3.222.104.176/index.php/getdrawing";
    HashMap<String, String> hashMap = new HashMap<>();
    com.example.e4.HttpParse httpParse = new com.example.e4.HttpParse();
    ProgressDialog progressDialog;
    String finalResult;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_planning, container, false);

        team_date = root.findViewById(R.id.team_date);
        Button select_date = root.findViewById(R.id.select_date);
        Button next_proc = root.findViewById(R.id.next_proc);
        Button prev_proc = root.findViewById(R.id.prev_proc);
        drawing_no = root.findViewById(R.id.drawing_no);
        need_to_apply = root.findViewById(R.id.need_to_apply);
        payment_confirmation = root.findViewById(R.id.payment_confirmation);
        cutting_remarks = root.findViewById(R.id.cutting_remarks);
        team_name = root.findViewById(R.id.team_name);
        remarks = root.findViewById(R.id.remarks);
        rg_die_availability = root.findViewById(R.id.rg_die_availability);
        rg_cutting_bending = root.findViewById(R.id.rg_cutting_bending);
        tank_fabrication = root.findViewById(R.id.tank_fabrication);
        fittings = root.findViewById(R.id.fittings);
        other_accessories = root.findViewById(R.id.other_accessories);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate1 = df.format(c);
        team_date.setText(formattedDate1);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT DATE");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        select_date.setOnClickListener(v -> {
            materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        materialDatePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate  = format.format(calendar.getTime());
            team_date.setText(formattedDate);
        });

        prev_proc.setOnClickListener(view -> {

            //Toast.makeText(getActivity(), "Please fill all form fields.", Toast.LENGTH_LONG).show();
            Navigation.findNavController(view).navigate(R.id.nav_vehiclein);
        });

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(HomeFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        vehicle_unid = sharedpreferences.getString("unid","");

        SharedPreferences sharedpreferences_1 = getActivity().getSharedPreferences(UserLoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = sharedpreferences_1.getString("user_id","");

        GetDrawing();

        //Toast.makeText(getContext().getApplicationContext(), vehicle_unid, Toast.LENGTH_SHORT).show();

        next_proc.setOnClickListener(view -> {

            txt_drawing_no = drawing_no.getText().toString();
            txt_payment_confirmation = payment_confirmation.getText().toString();
            txt_cutting_remarks = cutting_remarks.getText().toString();
            txt_team_name = team_name.getText().toString();
            txt_remarks = remarks.getText().toString();
            txt_team_date = team_date.getText().toString();

            if(tank_fabrication.isChecked())
                txt_tank_fabrication = tank_fabrication.getText().toString();
            if(fittings.isChecked())
                txt_fittings = fittings.getText().toString();
            if(other_accessories.isChecked())
                txt_other_accessories = other_accessories.getText().toString();
            if(need_to_apply.isChecked())
                txt_need_to_apply = need_to_apply.getText().toString();

            int die_id = rg_die_availability.getCheckedRadioButtonId();
            die_availability = (RadioButton) root.findViewById(die_id);
            txt_die_availability = die_availability.getText().toString();

            int cut_id = rg_cutting_bending.getCheckedRadioButtonId();
            cutting_bending = (RadioButton) root.findViewById(cut_id);
            txt_cutting_bending = cutting_bending.getText().toString();

            //progressDialog = ProgressDialog.show(getContext(), "Loading Data", null, true, true);

            UserLoginFunction(view);

        });

        return root;
    }

    public void GetDrawing() {

        class GetDrawingClass extends AsyncTask<String, Void, String> {

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

                        List<String> drawing_list = new ArrayList<String>();

                        JSONArray json_data = new JSONArray(jsonObject.getString("data"));

                        for(int i = 0; i<json_data.length(); i++){
                            JSONObject obj_drawing = new JSONObject(json_data.getString(i));
                            System.out.println(obj_drawing.getString("drawing_no"));
                            drawing_list.add(obj_drawing.getString("drawing_no"));
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, drawing_list);
                        drawing_no.setThreshold(1);
                        drawing_no.setAdapter(adapter);
                        //drawing_no.setInputType(InputType.TYPE_NULL);
                        drawing_no.setTextColor(Color.BLACK);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("user_id", user_id);

                finalResult = httpParse.postRequest(hashMap, HttpURLGet);

                System.out.println(finalResult);

                return finalResult;
            }
        }

        GetDrawingClass getDrawingClass = new GetDrawingClass();
        getDrawingClass.execute();
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

                                Navigation.findNavController(view).navigate(R.id.nav_tankfab);


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
                hashMap.put("need_to_apply", txt_need_to_apply);
                hashMap.put("die_availability", txt_die_availability);
                hashMap.put("payment_confirmation", txt_payment_confirmation);
                hashMap.put("tank_fabrication", txt_tank_fabrication);
                hashMap.put("fittings", txt_fittings);
                hashMap.put("other_accessories", txt_other_accessories);
                hashMap.put("cutting_bending", txt_cutting_bending);
                hashMap.put("cutting_remarks", txt_cutting_remarks);
                hashMap.put("team_name", txt_team_name);
                hashMap.put("team_date", txt_team_date);
                hashMap.put("remarks", txt_remarks);
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