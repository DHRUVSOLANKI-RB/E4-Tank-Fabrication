package com.example.e4.ui.upload;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.volley.RequestQueue;
import com.example.e4.MainActivity;
import com.example.e4.R;
import com.example.e4.UserLoginActivity;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class HomeFragment<array_uri> extends Fragment {

    EditText make;
    EditText unladenwgt;
    EditText model;
    EditText wheelbase;
    EditText ladenwgt;
    TextView indate;
    TextView delivery_date;
    String Directorate;
    AutoCompleteTextView category;
    AutoCompleteTextView bs_type;
    AutoCompleteTextView vehicle_type;
    ImageView imageview_1;
    ImageView imageview_2;
    Button image_1;
    Button image_2;
    String vendor;
    String vendorid;
    String fileno;
    String itemname;
    String rdsospecs;
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    String get_filename = "";
    String all_filename = "";
    Uri uri;
    HashMap<String, String> array_file_uri = new HashMap<>();
    float file_size = (float) 0.00;
    boolean file_alert = false;

    String count_loop = "";

    String displayName = null;
    private ArrayList<HashMap<String, String>> arraylist;
    private RequestQueue rQueue;

    String[] arr_category = {"Bio MRO", "Mobile Bowser", "DD Bowser", "TL Tank", "BL Tank", "Tank Shifting", "Storage Tank"};

    String[] arr_bstype = {"BSIII", "BSIV", "BSVI"};

    private static final String IMAGE_DIRECTORY = "/E4-Tank-Fabrication";

    private final int GALLERY = 1;
    private final int CAMERA = 2;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_vehicle, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //select_file = root.findViewById(R.id.select_file);
        make = root.findViewById(R.id.make);
        model = root.findViewById(R.id.model);
        wheelbase = root.findViewById(R.id.wheelbase);
        ladenwgt = root.findViewById(R.id.ladenwgt);
        unladenwgt = root.findViewById(R.id.unladenwgt);
        //upload = root.findViewById(R.id.upload);
        indate = root.findViewById(R.id.indate);
        Button select_in_date = root.findViewById(R.id.select_in_date);
        Button select_delivery_date = root.findViewById(R.id.select_delivery_date);
        delivery_date = root.findViewById(R.id.delivery_date);
        image_1 = root.findViewById(R.id.image_1);
        imageview_1 = root.findViewById(R.id.imageview_1);

        image_2 = root.findViewById(R.id.image_2);
        imageview_2 = root.findViewById(R.id.imageview_2);

        requestMultiplePermissions();

        final Button upload = (Button) root.findViewById(R.id.upload);

        image_1.setOnClickListener(v -> showPictureDialog());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, arr_category);
        category = (AutoCompleteTextView) root.findViewById(R.id.category);
        category.setThreshold(1);
        category.setAdapter(adapter);
        category.setTextColor(Color.BLACK);

        ArrayAdapter<String> adapter_bs_type = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, arr_bstype);
        bs_type = (AutoCompleteTextView) root.findViewById(R.id.bs_type);
        bs_type.setThreshold(1);
        bs_type.setAdapter(adapter_bs_type);
        bs_type.setTextColor(Color.BLACK);

        ArrayAdapter<String> adapter_vehicle_type = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, arr_category);
        vehicle_type = (AutoCompleteTextView) root.findViewById(R.id.vehicle_type);
        vehicle_type.setThreshold(1);
        vehicle_type.setAdapter(adapter_vehicle_type);
        vehicle_type.setTextColor(Color.BLACK);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT IN DATE");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        select_in_date.setOnClickListener((View.OnClickListener) v -> {
            materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        materialDatePicker.addOnPositiveButtonClickListener(
                selection -> indate.setText(materialDatePicker.getHeaderText()));

        /*      Expected Delivery Date    */

        MaterialDatePicker.Builder materialDateBuilder_deldt = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder_deldt.setTitleText("SELECT IN DATE");

        final MaterialDatePicker materialDatePicker_deldt = materialDateBuilder_deldt.build();

        select_delivery_date.setOnClickListener((View.OnClickListener) v -> {
            materialDatePicker_deldt.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        materialDatePicker_deldt.addOnPositiveButtonClickListener(
                selection -> delivery_date.setText(materialDatePicker_deldt.getHeaderText()));


        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(UserLoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);

        upload.setOnClickListener(view -> {

            //Toast.makeText(getActivity(), "Please fill all form fields.", Toast.LENGTH_LONG).show();

            Navigation.findNavController(view).navigate(R.id.nav_planning);

        });

        return root;
    }

    @SuppressLint("Range")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
//
//                uri = data.getData();
//                String uriString = uri.toString();
//                File myFile = new File(uriString);
//                String path = myFile.getAbsolutePath();
//
//                System.out.println("file:-"+ uri.toString());
//
//                imageview_1.setImageURI(uri);
//
//        }

        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageview_1.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageview_1.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPictureDialog(){
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

        vendor = model.getText().toString();
        vendorid = bs_type.getText().toString();
        fileno = wheelbase.getText().toString();
        itemname = ladenwgt.getText().toString();
        rdsospecs = unladenwgt.getText().toString();

        CheckEditText = !TextUtils.isEmpty(vendor) && !TextUtils.isEmpty(vendorid) && !TextUtils.isEmpty(fileno) && !TextUtils.isEmpty(itemname) && !TextUtils.isEmpty(get_filename);
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
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
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
                            Toast.makeText(getActivity().getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
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

}