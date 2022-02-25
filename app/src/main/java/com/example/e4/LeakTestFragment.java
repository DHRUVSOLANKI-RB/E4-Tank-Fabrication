package com.example.e4;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class LeakTestFragment extends Fragment {

    TextView leak_date_text;
    EditText driver_name,tank_team_member,gp_from,gp_to;
    RadioGroup rg_leak_fix;
    RadioButton leak_fix;
    String txt_leak_date = "",txt_driver_name = "",txt_tank_team_member = "",txt_gp_from = "",txt_gp_to = "",txt_leak_fix = "",vehicle_unid = "";

    String HttpURL = "http://3.222.104.176/index.php/leaktest";
    HashMap<String, String> hashMap = new HashMap<>();
    com.example.e4.HttpParse httpParse = new com.example.e4.HttpParse();
    ProgressDialog progressDialog;
    String finalResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_leak_test, container, false);

        leak_date_text = root.findViewById(R.id.leak_date_text);
        Button leak_date = root.findViewById(R.id.leak_date);
        Button next_leaktest = root.findViewById(R.id.next_leaktest);
        Button prev_leaktest = root.findViewById(R.id.prev_leaktest);
        driver_name = root.findViewById(R.id.driver_name);
        tank_team_member = root.findViewById(R.id.tank_team_member);
        gp_from = root.findViewById(R.id.gp_from);
        gp_to = root.findViewById(R.id.gp_to);
        rg_leak_fix = root.findViewById(R.id.rg_leak_fix);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate1 = df.format(c);
        leak_date_text.setText(formattedDate1);

        MaterialDatePicker.Builder materialDateBuilder_mounted = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder_mounted.setTitleText("ASSIGNED DATE");

        final MaterialDatePicker materialDatePicker_mounted = materialDateBuilder_mounted.build();

        leak_date.setOnClickListener(v -> {
            materialDatePicker_mounted.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        materialDatePicker_mounted.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate  = format.format(calendar.getTime());
            leak_date_text.setText(formattedDate);
        });


        prev_leaktest.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_fitting);
        });

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(HomeFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        vehicle_unid = sharedpreferences.getString("unid","");

        next_leaktest.setOnClickListener(view -> {

            txt_driver_name = driver_name.getText().toString();
            txt_tank_team_member = tank_team_member.getText().toString();
            txt_gp_from = gp_from.getText().toString();
            txt_gp_to = gp_to.getText().toString();
            txt_leak_date = leak_date.getText().toString();

            int leak_id = rg_leak_fix.getCheckedRadioButtonId();
            leak_fix = (RadioButton) root.findViewById(leak_id);
            txt_leak_fix = leak_fix.getText().toString();

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

                                createPdf();

                                Navigation.findNavController(view).navigate(R.id.nav_post_test);

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

                hashMap.put("driver_name", txt_driver_name);
                hashMap.put("leak_date", txt_leak_date);
                hashMap.put("tank_team_member", txt_tank_team_member);
                hashMap.put("leak_fix", txt_leak_fix);
                hashMap.put("gp_from", txt_gp_from);
                hashMap.put("gp_to", txt_gp_to);
                hashMap.put("vehicle_unid", vehicle_unid);

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