package com.example.e4.ui.upload;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.e4.DashboardFragment;
import com.example.e4.EndPoints;
import com.example.e4.MainActivity;
import com.example.e4.PlanningFragment;
import com.example.e4.R;
import com.example.e4.UserLoginActivity;
import com.example.e4.VolleyMultipartRequest;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class HomeFragment<array_uri> extends Fragment {

    EditText unladenwgt,model,wheelbase,ladenwgt,engineno,chessisno,regno,delivername,deliverphone,receivername,receiverphone,customername,customeradd,
            contactdetail,companyname,contactno,capacity,compdistri,depotname,diesel_tank,cabin_color,denting_painting,existing_fault,remarks,
            compartment_1,compartment_2,compartment_3,compartment_4,compartment_5,compartment_6;

    TextInputLayout layout_compartment_1,layout_compartment_2,layout_compartment_3,layout_compartment_4,layout_compartment_5,layout_compartment_6;

    RadioGroup rg_fire_extinguisher,rg_dip_rod,rg_delivery_hose,rg_parking_cone;

    RadioButton fire_extinguisher,dip_rod,delivery_hose,parking_cone,fire_extinguisher_yes,dip_rod_yes,delivery_hose_yes,parking_cone_yes;

    TextView indate,delivery_date;

    AutoCompleteTextView category,bs_type,vehicle_type,make,oilcompany;

    ImageView imageview_1,imageview_2,imageview_3,imageview_4,imageview_5,imageview_6;

    Button image_1,image_2,image_3,image_4,image_5,image_6;

    CheckBox spare_wheel,jack,jack_rod,tool_kit,back_sensors,reflector,cabin_fire_extingusher,rear_lights,battery_serial_number;

    Boolean CheckEditText;
    ProgressDialog progressDialog;

    String get_filename = "",all_filename = "",displayName = "",get_buttonid = "",txt_category = "",txt_make = "",txt_model = "",txt_bs_type = "",
            txt_wheelbase = "",txt_ladenwgt = "",txt_unladenwgt = "",txt_engineno = "", txt_chessisno = "",txt_regno = "",txt_delivername = "",txt_deliverphone = "",
            txt_receivername = "",txt_receiverphone = "",txt_customername = "",txt_customeradd = "",txt_contactdetail = "", txt_companyname = "",txt_contactno = "",
            txt_vehicle_type = "",txt_capacity = "",txt_compdistri = "",txt_oilcompany = "",txt_depotname = "",txt_indate = "",txt_delivery_date = "",txt_spare_wheel = "",
            txt_jack = "",txt_jack_rod = "",txt_tool_kit = "",txt_back_sensors = "",txt_reflector = "",txt_cabin_fire_extingusher = "",txt_rear_lights = "",
            txt_battery_serial_number = "",txt_fire_extinguisher = "",txt_dip_rod = "",txt_delivery_hose = "",txt_parking_cone = "",txt_cabin_color = "",
            txt_denting_painting = "",txt_existing_fault = "",txt_remarks = "",txt_diesel_tank = "",user_id = "",txt_compartment_1 = "",txt_compartment_2 = "",
            txt_compartment_3 = "",txt_compartment_4 = "",txt_compartment_5 = "",txt_compartment_6 = "",serial_no = "",txt_sno = "",checklist_sno = "",compartment_sno = "";

    Uri uri;
    HashMap<String, String> array_file_uri = new HashMap<>();
    float file_size = (float) 0.00;
    boolean file_alert = false;
    private ArrayList<HashMap<String, String>> arraylist;
    private RequestQueue rQueue;

    String[] arr_category = {"Bio MRO", "Mobile Bowser", "DD Bowser", "TL Tank", "BL Tank", "Tank Shifting", "Storage Tank"};
    String[] arr_bstype = {"BSIII", "BSIV", "BSVI"};
    String[] arr_make = {"TATA", "ASHOK LEYLAND", "BHARATBENZ", "EICHER", "MAHINDRA"};
    String[] arr_oil = {"HPCL", "Nayara", "BPCL", "IOCl", "Reliance"};

    private static final String IMAGE_DIRECTORY = "/E4TankFabrication";
    private final int GALLERY = 1;
    private final int CAMERA = 2;
    int count_loop = 0;
    public static final String MyPREFERENCES = "Vehicle";
    SharedPreferences sp_vehicle;

    String HttpURLGet = "http://3.222.104.176/index.php/getvehicledata";
    HashMap<String, String> hashMap = new HashMap<>();
    com.example.e4.HttpParse httpParse = new com.example.e4.HttpParse();
    String finalResult;
    String loop_status = "";

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_vehicle, container, false);

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        sp_vehicle = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //select_file = root.findViewById(R.id.select_file);
        make = root.findViewById(R.id.make);
        model = root.findViewById(R.id.model);
        wheelbase = root.findViewById(R.id.wheelbase);
        ladenwgt = root.findViewById(R.id.ladenwgt);
        unladenwgt = root.findViewById(R.id.unladenwgt);
        engineno = root.findViewById(R.id.engineno);
        chessisno = root.findViewById(R.id.chessisno);
        regno = root.findViewById(R.id.regno);
        delivername = root.findViewById(R.id.delivername);
        deliverphone = root.findViewById(R.id.deliverphone);
        receivername = root.findViewById(R.id.receivername);
        receiverphone = root.findViewById(R.id.receiverphone);
        customername = root.findViewById(R.id.customername);
        customeradd = root.findViewById(R.id.customeradd);
        contactdetail = root.findViewById(R.id.contactdetail);
        companyname = root.findViewById(R.id.companyname);
        contactno = root.findViewById(R.id.contactno);
        vehicle_type = root.findViewById(R.id.vehicle_type);
        capacity = root.findViewById(R.id.capacity);
        compdistri = root.findViewById(R.id.compdistri);
        oilcompany = root.findViewById(R.id.oilcompany);
        depotname = root.findViewById(R.id.depotname);
        //upload = root.findViewById(R.id.upload);
        indate = root.findViewById(R.id.indate);
        Button select_in_date = root.findViewById(R.id.select_in_date);
        Button select_delivery_date = root.findViewById(R.id.select_delivery_date);
        delivery_date = root.findViewById(R.id.delivery_date);
        image_1 = root.findViewById(R.id.image_1);
        imageview_1 = root.findViewById(R.id.imageview_1);
        image_2 = root.findViewById(R.id.image_2);
        imageview_2 = root.findViewById(R.id.imageview_2);
        image_3 = root.findViewById(R.id.image_3);
        imageview_3 = root.findViewById(R.id.imageview_3);
        image_4 = root.findViewById(R.id.image_4);
        imageview_4 = root.findViewById(R.id.imageview_4);
        image_5 = root.findViewById(R.id.image_5);
        imageview_5 = root.findViewById(R.id.imageview_5);
        image_6 = root.findViewById(R.id.image_6);
        imageview_6 = root.findViewById(R.id.imageview_6);
        spare_wheel = root.findViewById(R.id.spare_wheel);
        jack = root.findViewById(R.id.jack);
        jack_rod = root.findViewById(R.id.jack_rod);
        tool_kit = root.findViewById(R.id.tool_kit);
        back_sensors = root.findViewById(R.id.back_sensors);
        reflector = root.findViewById(R.id.reflector);
        cabin_fire_extingusher = root.findViewById(R.id.cabin_fire_extingusher);
        rear_lights = root.findViewById(R.id.rear_lights);
        battery_serial_number = root.findViewById(R.id.battery_serial_number);
        rg_fire_extinguisher = root.findViewById(R.id.rg_fire_extinguisher);
        rg_dip_rod = root.findViewById(R.id.rg_dip_rod);
        rg_delivery_hose = root.findViewById(R.id.rg_delivery_hose);
        rg_parking_cone = root.findViewById(R.id.rg_parking_cone);
        diesel_tank = root.findViewById(R.id.diesel_tank);
        cabin_color = root.findViewById(R.id.cabin_color);
        denting_painting = root.findViewById(R.id.denting_painting);
        existing_fault = root.findViewById(R.id.existing_fault);
        remarks = root.findViewById(R.id.remarks);
        compartment_1 = root.findViewById(R.id.compartment_1);
        compartment_2 = root.findViewById(R.id.compartment_2);
        compartment_3 = root.findViewById(R.id.compartment_3);
        compartment_4 = root.findViewById(R.id.compartment_4);
        compartment_5 = root.findViewById(R.id.compartment_5);
        compartment_6 = root.findViewById(R.id.compartment_6);
        layout_compartment_1 = root.findViewById(R.id.main_compartment_1);
        layout_compartment_2 = root.findViewById(R.id.main_compartment_2);
        layout_compartment_3 = root.findViewById(R.id.main_compartment_3);
        layout_compartment_4 = root.findViewById(R.id.main_compartment_4);
        layout_compartment_5 = root.findViewById(R.id.main_compartment_5);
        layout_compartment_6 = root.findViewById(R.id.main_compartment_6);
        fire_extinguisher_yes = root.findViewById(R.id.fire_extinguisher_yes);
        dip_rod_yes = root.findViewById(R.id.dip_rod_yes);
        delivery_hose_yes = root.findViewById(R.id.delivery_hose_yes);
        parking_cone_yes = root.findViewById(R.id.parking_cone_yes);

        requestMultiplePermissions();

        final Button upload = (Button) root.findViewById(R.id.upload);

        image_1.setOnClickListener(v -> showPictureDialog(v.getId()));
        image_2.setOnClickListener(v -> showPictureDialog(v.getId()));
        image_3.setOnClickListener(v -> showPictureDialog(v.getId()));
        image_4.setOnClickListener(v -> showPictureDialog(v.getId()));
        image_5.setOnClickListener(v -> showPictureDialog(v.getId()));
        image_6.setOnClickListener(v -> showPictureDialog(v.getId()));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, arr_category);
        category = (AutoCompleteTextView) root.findViewById(R.id.category);
        category.setThreshold(1);
        category.setAdapter(adapter);
        category.setInputType(InputType.TYPE_NULL);
        category.setTextColor(Color.BLACK);

        ArrayAdapter<String> adapter_make = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, arr_make);
        make = (AutoCompleteTextView) root.findViewById(R.id.make);
        make.setThreshold(1);
        make.setAdapter(adapter_make);
        make.setInputType(InputType.TYPE_NULL);
        make.setTextColor(Color.BLACK);

        ArrayAdapter<String> adapter_bs_type = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, arr_bstype);
        bs_type = (AutoCompleteTextView) root.findViewById(R.id.bs_type);
        bs_type.setThreshold(1);
        bs_type.setAdapter(adapter_bs_type);
        bs_type.setInputType(InputType.TYPE_NULL);
        bs_type.setTextColor(Color.BLACK);

        ArrayAdapter<String> adapter_vehicle_type = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, arr_category);
        vehicle_type = (AutoCompleteTextView) root.findViewById(R.id.vehicle_type);
        vehicle_type.setThreshold(1);
        vehicle_type.setAdapter(adapter_vehicle_type);
        vehicle_type.setInputType(InputType.TYPE_NULL);
        vehicle_type.setTextColor(Color.BLACK);

        ArrayAdapter<String> adapter_oil = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, arr_oil);
        oilcompany = (AutoCompleteTextView) root.findViewById(R.id.oilcompany);
        oilcompany.setThreshold(1);
        oilcompany.setAdapter(adapter_oil);
        oilcompany.setInputType(InputType.TYPE_NULL);
        oilcompany.setTextColor(Color.BLACK);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate1 = df.format(c);
        indate.setText(formattedDate1);
        delivery_date.setText(formattedDate1);

        compdistri.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().equals("1")){
                    layout_compartment_1.setVisibility(View.VISIBLE);
                }else if(s.toString().equals("2")){
                    layout_compartment_1.setVisibility(View.VISIBLE);
                    layout_compartment_2.setVisibility(View.VISIBLE);
                }else if(s.toString().equals("3")){
                    layout_compartment_1.setVisibility(View.VISIBLE);
                    layout_compartment_2.setVisibility(View.VISIBLE);
                    layout_compartment_3.setVisibility(View.VISIBLE);
                }else if(s.toString().equals("4")){
                    layout_compartment_1.setVisibility(View.VISIBLE);
                    layout_compartment_2.setVisibility(View.VISIBLE);
                    layout_compartment_3.setVisibility(View.VISIBLE);
                    layout_compartment_4.setVisibility(View.VISIBLE);
                }else if(s.toString().equals("5")){
                    layout_compartment_1.setVisibility(View.VISIBLE);
                    layout_compartment_2.setVisibility(View.VISIBLE);
                    layout_compartment_3.setVisibility(View.VISIBLE);
                    layout_compartment_4.setVisibility(View.VISIBLE);
                    layout_compartment_5.setVisibility(View.VISIBLE);
                }else if(s.toString().equals("6")){
                    layout_compartment_1.setVisibility(View.VISIBLE);
                    layout_compartment_2.setVisibility(View.VISIBLE);
                    layout_compartment_3.setVisibility(View.VISIBLE);
                    layout_compartment_4.setVisibility(View.VISIBLE);
                    layout_compartment_5.setVisibility(View.VISIBLE);
                    layout_compartment_6.setVisibility(View.VISIBLE);
                }else{
                    layout_compartment_1.setVisibility(View.GONE);
                    layout_compartment_2.setVisibility(View.GONE);
                    layout_compartment_3.setVisibility(View.GONE);
                    layout_compartment_4.setVisibility(View.GONE);
                    layout_compartment_5.setVisibility(View.GONE);
                    layout_compartment_6.setVisibility(View.GONE);
                }

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT IN DATE");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        select_in_date.setOnClickListener((View.OnClickListener) v -> {
            materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        materialDatePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate  = format.format(calendar.getTime());
            indate.setText(formattedDate);
        });

        /*      Expected Delivery Date    */

        MaterialDatePicker.Builder materialDateBuilder_deldt = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder_deldt.setTitleText("SELECT IN DATE");

        final MaterialDatePicker materialDatePicker_deldt = materialDateBuilder_deldt.build();

        select_delivery_date.setOnClickListener((View.OnClickListener) v -> {
            materialDatePicker_deldt.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        materialDatePicker_deldt.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate  = format.format(calendar.getTime());
            delivery_date.setText(formattedDate);
        });

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(UserLoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = sharedpreferences.getString("user_id","");

        SharedPreferences sharedpreferences_1 = getActivity().getSharedPreferences(DashboardFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        serial_no = sharedpreferences_1.getString("serial_no","");

        if(!serial_no.equals("")){
            //Toast.makeText(getActivity(),serial_no, Toast.LENGTH_SHORT).show();
            GetVehicleData();
        }else{
            //Toast.makeText(getActivity(),"empty", Toast.LENGTH_SHORT).show();
        }

        upload.setOnClickListener(view -> {

            txt_category = category.getText().toString();
            txt_make = make.getText().toString();
            txt_model = model.getText().toString();
            txt_bs_type = bs_type.getText().toString();
            txt_wheelbase = wheelbase.getText().toString();
            txt_ladenwgt = ladenwgt.getText().toString();
            txt_unladenwgt = unladenwgt.getText().toString();
            txt_engineno = engineno.getText().toString();
            txt_chessisno = chessisno.getText().toString();
            txt_regno = regno.getText().toString();
            txt_delivername = delivername.getText().toString();
            txt_deliverphone = deliverphone.getText().toString();
            txt_receivername = receivername.getText().toString();
            txt_receiverphone = receiverphone.getText().toString();
            txt_customername = customername.getText().toString();
            txt_customeradd = customeradd.getText().toString();
            txt_contactdetail = contactdetail.getText().toString();
            txt_companyname = companyname.getText().toString();
            txt_contactno = contactno.getText().toString();
            txt_vehicle_type = vehicle_type.getText().toString();
            txt_capacity = capacity.getText().toString();
            txt_compdistri = compdistri.getText().toString();
            txt_oilcompany = oilcompany.getText().toString();
            txt_depotname = depotname.getText().toString();
            txt_indate = indate.getText().toString();
            txt_delivery_date = delivery_date.getText().toString();

            if(spare_wheel.isChecked())
                txt_spare_wheel = spare_wheel.getText().toString();
            if(jack.isChecked())
                txt_jack = jack.getText().toString();
            if(jack_rod.isChecked())
                txt_jack_rod = jack_rod.getText().toString();
            if(tool_kit.isChecked())
                txt_tool_kit = tool_kit.getText().toString();
            if(back_sensors.isChecked())
                txt_back_sensors = back_sensors.getText().toString();
            if(reflector.isChecked())
                txt_reflector = reflector.getText().toString();
            if(cabin_fire_extingusher.isChecked())
                txt_cabin_fire_extingusher = cabin_fire_extingusher.getText().toString();
            if(rear_lights.isChecked())
                txt_rear_lights = rear_lights.getText().toString();
            if(battery_serial_number.isChecked())
                txt_battery_serial_number = battery_serial_number.getText().toString();

            txt_diesel_tank = diesel_tank.getText().toString();

            int fire_id = rg_fire_extinguisher.getCheckedRadioButtonId();
            fire_extinguisher = (RadioButton) root.findViewById(fire_id);
            txt_fire_extinguisher = fire_extinguisher.getText().toString();

            int diprod_id = rg_dip_rod.getCheckedRadioButtonId();
            dip_rod = (RadioButton) root.findViewById(diprod_id);
            txt_dip_rod = dip_rod.getText().toString();

            int deliveryhose_id = rg_delivery_hose.getCheckedRadioButtonId();
            delivery_hose = (RadioButton) root.findViewById(deliveryhose_id);
            txt_delivery_hose = delivery_hose.getText().toString();

            int parkingcone = rg_parking_cone.getCheckedRadioButtonId();
            parking_cone = (RadioButton) root.findViewById(parkingcone);
            txt_parking_cone = parking_cone.getText().toString();

            txt_cabin_color = cabin_color.getText().toString();
            txt_denting_painting = denting_painting.getText().toString();
            txt_existing_fault = existing_fault.getText().toString();
            txt_remarks = remarks.getText().toString();

            txt_compartment_1 = compartment_1.getText().toString();
            txt_compartment_2 = compartment_2.getText().toString();
            txt_compartment_3 = compartment_3.getText().toString();
            txt_compartment_4 = compartment_4.getText().toString();
            txt_compartment_5 = compartment_5.getText().toString();
            txt_compartment_6 = compartment_6.getText().toString();

            //Toast.makeText(getActivity(), txt_parking_cone, Toast.LENGTH_LONG).show();

            CheckEditTextIsEmptyOrNot();

            if (CheckEditText) {

                progressDialog = ProgressDialog.show(getContext(), "Loading Data", null, true, true);

                count_loop = array_file_uri.size();

                if(array_file_uri.isEmpty()){
                    System.out.println("empty");
                    Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.drawable.edit);
                    array_file_uri.put("edit", uri.toString());
                    System.out.println(uri);
                }

                Iterator it = array_file_uri.entrySet().iterator();
                while (it.hasNext()) {

                    Map.Entry pair = (Map.Entry) it.next();

//                    if(count_loop == 1){
//                        loop_status = "end";
//                    }else{
//                        loop_status = "start";
//                    }
//                    System.out.println(count_loop);
//                    System.out.println(loop_status);

                    uploadPDF(pair.getKey().toString(), Uri.parse(pair.getValue().toString()), view);
                    it.remove();

                    count_loop--;
                }

            } else {

                Toast.makeText(getContext(), "Please select atleast 1 file.", Toast.LENGTH_LONG).show();
            }

            //Navigation.findNavController(view).navigate(R.id.nav_planning);

        });

        return root;
    }

    public void GetVehicleData() {

        class GetVehicleDataClass extends AsyncTask<String, Void, String> {

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

                        category.setText(jsonObject_data.getString("category"));
                        make.setText(jsonObject_data.getString("make"));
                        model.setText(jsonObject_data.getString("model"));
                        bs_type.setText(jsonObject_data.getString("bs_type"));
                        wheelbase.setText(jsonObject_data.getString("wheelbase"));
                        ladenwgt.setText(jsonObject_data.getString("ladenwgt"));
                        unladenwgt.setText(jsonObject_data.getString("unladenwgt"));
                        engineno.setText(jsonObject_data.getString("engineno"));
                        chessisno.setText(jsonObject_data.getString("chessisno"));
                        regno.setText(jsonObject_data.getString("regno"));
                        delivername.setText(jsonObject_data.getString("delivername"));
                        deliverphone.setText(jsonObject_data.getString("deliverphone"));
                        receivername.setText(jsonObject_data.getString("receivername"));
                        receiverphone.setText(jsonObject_data.getString("receiverphone"));
                        customername.setText(jsonObject_data.getString("customername"));
                        customeradd.setText(jsonObject_data.getString("customeradd"));
                        contactdetail.setText(jsonObject_data.getString("contactdetail"));
                        companyname.setText(jsonObject_data.getString("companyname"));
                        contactno.setText(jsonObject_data.getString("contactno"));
                        vehicle_type.setText(jsonObject_data.getString("vehicle_type"));
                        capacity.setText(jsonObject_data.getString("capacity"));
                        compdistri.setText(jsonObject_data.getString("compdistri"));
                        oilcompany.setText(jsonObject_data.getString("oilcompany"));
                        depotname.setText(jsonObject_data.getString("depotname"));
                        diesel_tank.setText(jsonObject_data.getString("diesel_tank"));
                        cabin_color.setText(jsonObject_data.getString("cabin_color"));
                        denting_painting.setText(jsonObject_data.getString("denting_painting"));
                        existing_fault.setText(jsonObject_data.getString("existing_fault"));
                        remarks.setText(jsonObject_data.getString("remarks"));
                        compartment_1.setText(jsonObject_data.getString("compartment_1"));
                        compartment_2.setText(jsonObject_data.getString("compartment_2"));
                        compartment_3.setText(jsonObject_data.getString("compartment_3"));
                        compartment_4.setText(jsonObject_data.getString("compartment_4"));
                        compartment_5.setText(jsonObject_data.getString("compartment_5"));
                        compartment_6.setText(jsonObject_data.getString("compartment_6"));
                        indate.setText(jsonObject_data.getString("indate"));
                        delivery_date.setText(jsonObject_data.getString("delivery_date"));
                        if(jsonObject_data.getString("spare_wheel").equals("1"))
                            spare_wheel.setChecked(true);
                        if(jsonObject_data.getString("jack").equals("1"))
                            jack.setChecked(true);
                        if(jsonObject_data.getString("jack_rod").equals("1"))
                            jack_rod.setChecked(true);
                        if(jsonObject_data.getString("tool_kit").equals("1"))
                            tool_kit.setChecked(true);
                        if(jsonObject_data.getString("back_sensors").equals("1"))
                            back_sensors.setChecked(true);
                        if(jsonObject_data.getString("reflector").equals("1"))
                            reflector.setChecked(true);
                        if(jsonObject_data.getString("cabin_fire_extingusher").equals("1"))
                            cabin_fire_extingusher.setChecked(true);
                        if(jsonObject_data.getString("rear_lights").equals("1"))
                            rear_lights.setChecked(true);
                        if(jsonObject_data.getString("battery_serial_no").equals("1"))
                            battery_serial_number.setChecked(true);
                        if(jsonObject_data.getString("fire_extinguisher").equals("1"))
                            fire_extinguisher_yes.setChecked(true);
                        if(jsonObject_data.getString("dip_rod").equals("1"))
                            dip_rod_yes.setChecked(true);
                        if(jsonObject_data.getString("delivery_hose").equals("1"))
                            delivery_hose_yes.setChecked(true);
                        if(jsonObject_data.getString("parking_cone").equals("1"))
                            parking_cone_yes.setChecked(true);
                        String[] img_file = jsonObject_data.getString("img_file").split(",");
                        get_filename = jsonObject_data.getString("img_file");
                        txt_sno = jsonObject_data.getString("vehicle_sno");
                        checklist_sno = jsonObject_data.getString("checklist");
                        compartment_sno = jsonObject_data.getString("compartment_sno");
                        //Toast.makeText(getActivity(),txt_sno, Toast.LENGTH_SHORT).show();

                        for(int i=0; i< img_file.length; i++){

                            if(i == 0){
                                String url = "http://3.222.104.176/assets/vehiclein/" + img_file[0];
                                imageview_1.setVisibility(View.VISIBLE);
                                imageview_2.setVisibility(View.VISIBLE);
                                imageview_1.setImageBitmap(getBitmapFromURL(url));
                            }else if(i == 1){
                                String url = "http://3.222.104.176/assets/vehiclein/" + img_file[1];
                                imageview_1.setVisibility(View.VISIBLE);
                                imageview_2.setVisibility(View.VISIBLE);
                                imageview_2.setImageBitmap(getBitmapFromURL(url));
                            }else if(i == 2){
                                String url = "http://3.222.104.176/assets/vehiclein/" + img_file[2];
                                imageview_3.setVisibility(View.VISIBLE);
                                imageview_4.setVisibility(View.VISIBLE);
                                imageview_3.setImageBitmap(getBitmapFromURL(url));
                            }else if(i == 3){
                                String url = "http://3.222.104.176/assets/vehiclein/" + img_file[3];
                                imageview_3.setVisibility(View.VISIBLE);
                                imageview_4.setVisibility(View.VISIBLE);
                                imageview_4.setImageBitmap(getBitmapFromURL(url));
                            }else if(i == 4){
                                String url = "http://3.222.104.176/assets/vehiclein/" + img_file[4];
                                imageview_5.setVisibility(View.VISIBLE);
                                imageview_6.setVisibility(View.VISIBLE);
                                imageview_5.setImageBitmap(getBitmapFromURL(url));
                            }else if(i == 5){
                                String url = "http://3.222.104.176/assets/vehiclein/" + img_file[5];
                                imageview_5.setVisibility(View.VISIBLE);
                                imageview_6.setVisibility(View.VISIBLE);
                                imageview_6.setImageBitmap(getBitmapFromURL(url));
                            }
                        }

                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("serial_no", serial_no);
                hashMap.put("user_id", user_id);

                finalResult = httpParse.postRequest(hashMap, HttpURLGet);

                System.out.println(finalResult);

                return finalResult;
            }
        }

        GetVehicleDataClass getVehicleDataClass = new GetVehicleDataClass();
        getVehicleDataClass.execute();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    @SuppressLint("Range")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                uri = contentURI;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    //String path = saveImage(bitmap);
                    //Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    //imageview_1.setImageBitmap(bitmap);

                    switch (get_buttonid){
                        case "image_1":
                            imageview_1.setVisibility(View.VISIBLE);
                            imageview_2.setVisibility(View.VISIBLE);
                            imageview_1.setImageBitmap(bitmap);
                            break;
                        case "image_2":
                            imageview_1.setVisibility(View.VISIBLE);
                            imageview_2.setVisibility(View.VISIBLE);
                            imageview_2.setImageBitmap(bitmap);
                            break;
                        case "image_3":
                            imageview_3.setVisibility(View.VISIBLE);
                            imageview_4.setVisibility(View.VISIBLE);
                            imageview_3.setImageBitmap(bitmap);
                            break;
                        case "image_4":
                            imageview_3.setVisibility(View.VISIBLE);
                            imageview_4.setVisibility(View.VISIBLE);
                            imageview_4.setImageBitmap(bitmap);
                            break;
                        case "image_5":
                            imageview_5.setVisibility(View.VISIBLE);
                            imageview_6.setVisibility(View.VISIBLE);
                            imageview_5.setImageBitmap(bitmap);
                            break;
                        case "image_6":
                            imageview_5.setVisibility(View.VISIBLE);
                            imageview_6.setVisibility(View.VISIBLE);
                            imageview_6.setImageBitmap(bitmap);
                            break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }

                Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }

                if (get_filename.equals("")) {
                    get_filename = displayName;
                } else {
                    get_filename = get_filename + "," + displayName;
                }
                Log.d("nameeeee>>>>  ", get_filename);

                //Toast.makeText(getActivity(), displayName, Toast.LENGTH_SHORT).show();
                array_file_uri.put(displayName, uri.toString());
                cursor.close();
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), thumbnail, String.valueOf(Calendar.getInstance().getTimeInMillis()), null);
            uri = Uri.parse(path);

            System.out.println(uri);
            //imageview_1.setImageBitmap(thumbnail);

            switch (get_buttonid){
                case "image_1":
                    imageview_1.setVisibility(View.VISIBLE);
                    imageview_2.setVisibility(View.VISIBLE);
                    imageview_1.setImageBitmap(thumbnail);
                    break;
                case "image_2":
                    imageview_1.setVisibility(View.VISIBLE);
                    imageview_2.setVisibility(View.VISIBLE);
                    imageview_2.setImageBitmap(thumbnail);
                    break;
                case "image_3":
                    imageview_3.setVisibility(View.VISIBLE);
                    imageview_4.setVisibility(View.VISIBLE);
                    imageview_3.setImageBitmap(thumbnail);
                    break;
                case "image_4":
                    imageview_3.setVisibility(View.VISIBLE);
                    imageview_4.setVisibility(View.VISIBLE);
                    imageview_4.setImageBitmap(thumbnail);
                    break;
                case "image_5":
                    imageview_5.setVisibility(View.VISIBLE);
                    imageview_6.setVisibility(View.VISIBLE);
                    imageview_5.setImageBitmap(thumbnail);
                    break;
                case "image_6":
                    imageview_5.setVisibility(View.VISIBLE);
                    imageview_6.setVisibility(View.VISIBLE);
                    imageview_6.setImageBitmap(thumbnail);
                    break;
            }

            //saveImage(thumbnail);

            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }

            if (get_filename.equals("")) {
                get_filename = displayName;
            } else {
                get_filename = get_filename + "," + displayName;
            }
            Log.d("nameeeee>>>>  ", get_filename);

            //Toast.makeText(getActivity(), displayName, Toast.LENGTH_SHORT).show();

            array_file_uri.put(displayName, uri.toString());
            cursor.close();
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void showPictureDialog(int button_id){

        switch(button_id)
        {
            case R.id.image_1:
                get_buttonid = "image_1";
                break;
            case R.id.image_2:
                get_buttonid = "image_2";
                break;
            case R.id.image_3:
                get_buttonid = "image_3";
                break;
            case R.id.image_4:
                get_buttonid = "image_4";
                break;
            case R.id.image_5:
                get_buttonid = "image_5";
                break;
            case R.id.image_6:
                get_buttonid = "image_6";
                break;
            default:
                throw new RuntimeException("Unknow button ID");
        }

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    void imageChooser() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, 100);

    }


    public void CheckEditTextIsEmptyOrNot() {

        CheckEditText = !TextUtils.isEmpty(get_filename);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getActivity().getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getActivity().getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void uploadPDF(final String pdfname, Uri pdffile, View view) {

        InputStream iStream = null;
        try {

            iStream = getContext().getContentResolver().openInputStream(pdffile);

            System.out.println("source path " + pdffile);

            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.ROOT_URL, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    Log.d("ressssssoo", new String(response.data));
                    rQueue.getCache().clear();

                    //progressDialog.dismiss();

                    try {
                        JSONObject jsonObject = new JSONObject(new String(response.data));

                        if(!jsonObject.getString("status").equals("skipping")){

                            new MaterialAlertDialogBuilder(getContext()).setTitle(jsonObject.getString("statusMessage")).setPositiveButton("Ok", (dialogInterface, i) -> {

                                String error = null;
                                try {
                                    error = jsonObject.getString("status");

                                    if (error.equals("success")) {

                                        progressDialog.dismiss();

                                        get_filename = "";

                                        SharedPreferences.Editor editor = sp_vehicle.edit();
                                        editor.putString("unid", jsonObject.getString("unid"));
                                        editor.putString("compdistri", txt_compdistri);
                                        editor.apply();

                                        //Toast.makeText(getActivity(),txt_compdistri, Toast.LENGTH_LONG).show();

                                        if(!serial_no.equals("")){
                                            Navigation.findNavController(view).navigate(R.id.nav_dashboard);
                                        }else{
                                            Navigation.findNavController(view).navigate(R.id.nav_planning);
                                        }

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    error -> Toast.makeText(getContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show()) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("user_id", user_id);
                    params.put("category", txt_category);
                    params.put("make", txt_make);
                    params.put("model", txt_model);
                    params.put("bs_type", txt_bs_type);
                    params.put("wheelbase", txt_wheelbase);
                    params.put("ladenwgt", txt_ladenwgt);
                    params.put("unladenwgt", txt_unladenwgt);
                    params.put("engineno", txt_engineno);
                    params.put("chessisno", txt_chessisno);
                    params.put("regno", txt_regno);
                    params.put("delivername", txt_delivername);
                    params.put("deliverphone", txt_deliverphone);
                    params.put("receivername", txt_receivername);
                    params.put("receiverphone", txt_receiverphone);
                    params.put("customername", txt_customername);
                    params.put("customeradd", txt_customeradd);
                    params.put("contactdetail", txt_contactdetail);
                    params.put("companyname", txt_companyname);
                    params.put("contactno", txt_contactno);
                    params.put("vehicle_type", txt_vehicle_type);
                    params.put("capacity", txt_capacity);
                    params.put("compdistri", txt_compdistri);
                    params.put("oilcompany", txt_oilcompany);
                    params.put("depotname", txt_depotname);

                    params.put("indate", txt_indate);
                    params.put("delivery_date", txt_delivery_date);
                    params.put("spare_wheel", txt_spare_wheel);
                    params.put("jack", txt_jack);
                    params.put("jack_rod", txt_jack_rod);
                    params.put("tool_kit", txt_tool_kit);
                    params.put("back_sensors", txt_back_sensors);
                    params.put("reflector", txt_reflector);
                    params.put("cabin_fire_extingusher", txt_cabin_fire_extingusher);
                    params.put("rear_lights", txt_rear_lights);
                    params.put("battery_serial_number", txt_battery_serial_number);
                    params.put("diesel_tank", txt_diesel_tank);
                    params.put("fire_extinguisher", txt_fire_extinguisher);
                    params.put("dip_rod", txt_dip_rod);
                    params.put("delivery_hose", txt_delivery_hose);
                    params.put("parking_cone", txt_parking_cone);
                    params.put("cabin_color", txt_cabin_color);
                    params.put("denting_painting", txt_denting_painting);
                    params.put("existing_fault", txt_existing_fault);
                    params.put("remarks", txt_remarks);

                    System.out.println(count_loop);
                    if(count_loop == 0){
                        params.put("loop_status", "end");
                        System.out.println("loop_status end");
                    }else{
                        params.put("loop_status", "start");
                        System.out.println("loop_status start");
                    }

                    params.put("file_name", get_filename);
                    params.put("compartment_1", txt_compartment_1);
                    params.put("compartment_2", txt_compartment_2);
                    params.put("compartment_3", txt_compartment_3);
                    params.put("compartment_4", txt_compartment_4);
                    params.put("compartment_5", txt_compartment_5);
                    params.put("compartment_6", txt_compartment_6);
                    params.put("sno", txt_sno);
                    params.put("serial_no", serial_no);
                    params.put("checklist_sno", checklist_sno);
                    params.put("compartment_sno", compartment_sno);

                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();

                    params.put("file_data", new DataPart(pdfname, inputData));
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(HomeFragment.this.getContext());
            rQueue.add(volleyMultipartRequest);

            //progressDialog = ProgressDialog.show(getContext(), "Loading Data", null, true, true);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024 * 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}