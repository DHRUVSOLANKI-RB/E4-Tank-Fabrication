package com.example.e4;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.itextpdf.text.Document;
import com.itextpdf.text.Header;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class TankFabFragment extends Fragment {

    TextView mounted_date_text,assigned_date_text;
    EditText from,to,authorised_by,assigned_to,received_by;
    RadioGroup rg_die_availability,rg_drawing_clear,rg_material_received,rg_cardel_support;
    RadioButton die_availability,drawing_clear,material_received,cardel_support,die_availability_yes,drawing_clear_yes,material_received_yes,cardel_support_yes;
    String txt_die_availability = "",txt_drawing_clear = "",txt_material_received = "",txt_cardel_support = "", txt_mounted_date = "",txt_assigned_date = "",
            txt_from = "", txt_to = "",txt_authorised_by = "",txt_assigned_to = "",txt_received_by = "",user_id = "",vehicle_unid = "",txt_sno = "",serial_no = "";
    String HttpURL = "http://3.222.104.176/index.php/tankfab";
    String HttpURLGetPlanning = "http://3.222.104.176/index.php/gettankfabdata";
    HashMap<String, String> hashMap = new HashMap<>();
    com.example.e4.HttpParse httpParse = new com.example.e4.HttpParse();
    ProgressDialog progressDialog;
    String finalResult;
    String finalResult_GetPlanning;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tank_fab, container, false);

        mounted_date_text = root.findViewById(R.id.mounted_date_text);
        Button mounted_date = root.findViewById(R.id.mounted_date);
        assigned_date_text = root.findViewById(R.id.assigned_date_text);
        Button assigned_date = root.findViewById(R.id.assigned_date);
        Button next_tankfeb = root.findViewById(R.id.next_tankfeb);
        Button prev_tankfeb = root.findViewById(R.id.prev_tankfeb);

        rg_die_availability = root.findViewById(R.id.rg_die_availability);
        rg_drawing_clear = root.findViewById(R.id.rg_drawing_clear);
        rg_material_received = root.findViewById(R.id.rg_material_received);
        rg_cardel_support = root.findViewById(R.id.rg_cardel_support);
        from = root.findViewById(R.id.from);
        to = root.findViewById(R.id.to);
        authorised_by = root.findViewById(R.id.authorised_by);
        assigned_to = root.findViewById(R.id.assigned_to);
        received_by = root.findViewById(R.id.received_by);
        die_availability_yes = root.findViewById(R.id.die_availability_yes);
        drawing_clear_yes = root.findViewById(R.id.drawing_clear_yes);
        material_received_yes = root.findViewById(R.id.material_received_yes);
        cardel_support_yes = root.findViewById(R.id.cardel_support_yes);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate1 = df.format(c);
        mounted_date_text.setText(formattedDate1);
        assigned_date_text.setText(formattedDate1);

        MaterialDatePicker.Builder materialDateBuilder_mounted = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder_mounted.setTitleText("MOUNTED ON VEHICLE DATE");

        final MaterialDatePicker materialDatePicker_mounted = materialDateBuilder_mounted.build();

        mounted_date.setOnClickListener(v -> {
            materialDatePicker_mounted.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        materialDatePicker_mounted.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate  = format.format(calendar.getTime());
            mounted_date_text.setText(formattedDate);
        });

        /*     Assigned Date     */

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("ASSIGNED DATE");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        assigned_date.setOnClickListener(v -> {
            materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        materialDatePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate  = format.format(calendar.getTime());
            assigned_date_text.setText(formattedDate);
        });


        prev_tankfeb.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_planning);
        });

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(VehicleStepsFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        vehicle_unid = sharedpreferences.getString("vehicle_unid","");

        SharedPreferences sharedpreferences_1 = getActivity().getSharedPreferences(UserLoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = sharedpreferences_1.getString("user_id","");

        SharedPreferences sharedpreferences_3 = getActivity().getSharedPreferences(DashboardFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        serial_no = sharedpreferences_3.getString("serial_no","");

        if(!serial_no.equals("")){
            //Toast.makeText(getActivity(),vehicle_unid, Toast.LENGTH_SHORT).show();
            GetPlanningData();
        }else{
            //Toast.makeText(getActivity(),"empty", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedpreferences2 = getActivity().getSharedPreferences(HomeFragment.MyPREFERENCES, Context.MODE_PRIVATE);
            vehicle_unid = sharedpreferences2.getString("unid","");
        }

        next_tankfeb.setOnClickListener(view -> {

            txt_from = from.getText().toString();
            txt_to = to.getText().toString();
            txt_authorised_by = authorised_by.getText().toString();
            txt_assigned_to = assigned_to.getText().toString();
            txt_received_by = received_by.getText().toString();
            txt_mounted_date = mounted_date_text.getText().toString();
            txt_assigned_date = assigned_date_text.getText().toString();

            int die_id = rg_die_availability.getCheckedRadioButtonId();
            die_availability = (RadioButton) root.findViewById(die_id);
            txt_die_availability = die_availability.getText().toString();

            int drawing_id = rg_drawing_clear.getCheckedRadioButtonId();
            drawing_clear = (RadioButton) root.findViewById(drawing_id);
            txt_drawing_clear = drawing_clear.getText().toString();

            int material_id = rg_material_received.getCheckedRadioButtonId();
            material_received = (RadioButton) root.findViewById(material_id);
            txt_material_received = material_received.getText().toString();

            int cardel_id = rg_cardel_support.getCheckedRadioButtonId();
            cardel_support = (RadioButton) root.findViewById(cardel_id);
            txt_cardel_support = cardel_support.getText().toString();

            //Navigation.findNavController(view).navigate(R.id.nav_fitting);

            UserLoginFunction(view);

            //createPdf();

        });

        return root;
    }

    public void GetPlanningData() {

        class GetPlanningDataClass extends AsyncTask<String, Void, String> {

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

                        if(jsonObject_data.getString("die_availability").equals("1"))
                            die_availability_yes.setChecked(true);
                        if(jsonObject_data.getString("drawing_clear").equals("1"))
                            drawing_clear_yes.setChecked(true);
                        if(jsonObject_data.getString("material_received").equals("1"))
                            material_received_yes.setChecked(true);
                        if(jsonObject_data.getString("cardel_support").equals("1"))
                            cardel_support_yes.setChecked(true);
                        mounted_date_text.setText(jsonObject_data.getString("mounted_date"));
                        from.setText(jsonObject_data.getString("gp_from"));
                        to.setText(jsonObject_data.getString("gp_to"));
                        authorised_by.setText(jsonObject_data.getString("authorised_by"));
                        assigned_to.setText(jsonObject_data.getString("assigned_to"));
                        assigned_date_text.setText(jsonObject_data.getString("assigned_date"));
                        received_by.setText(jsonObject_data.getString("received_by"));
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

                finalResult_GetPlanning = httpParse.postRequest(hashMap, HttpURLGetPlanning);

                System.out.println(finalResult_GetPlanning);

                return finalResult_GetPlanning;
            }
        }

        GetPlanningDataClass getPlanningDataClass = new GetPlanningDataClass();
        getPlanningDataClass.execute();
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

                                createPdf();

                                if(!serial_no.equals("")){
                                    Navigation.findNavController(view).navigate(R.id.nav_dashboard);
                                }else{
                                    Navigation.findNavController(view).navigate(R.id.nav_fitting);
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

                hashMap.put("die_availability", txt_die_availability);
                hashMap.put("drawing_clear", txt_drawing_clear);
                hashMap.put("material_received", txt_material_received);
                hashMap.put("cardel_support", txt_cardel_support);
                hashMap.put("mounted_date", txt_mounted_date);
                hashMap.put("from", txt_from);
                hashMap.put("to", txt_to);
                hashMap.put("authorised_by", txt_authorised_by);
                hashMap.put("assigned_to", txt_assigned_to);
                hashMap.put("assigned_date", txt_assigned_date);
                hashMap.put("received_by", txt_received_by);
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

    private void createPdf(){

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "E4/Gatepass");

        if (!dir.exists()){
            if(dir.mkdirs()){
                System.out.println("done");
            }else{
                System.out.println("failed");
            }
        }


        Document mDoc = new Document();

        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());

        //String mFilePath = Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf";

        try {

            PdfWriter.getInstance(mDoc, new FileOutputStream(dir+ "/" + mFileName + ".pdf"));

            mDoc.open();
            //mDoc.addAuthor("Atif Pervaiz");
            mDoc.add(new Paragraph("Text"));
            mDoc.close();

            Toast.makeText(getActivity(), mFileName +".pdf\nis saved to\n"+ dir, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){

            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}