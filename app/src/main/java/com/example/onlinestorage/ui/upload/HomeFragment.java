package com.example.onlinestorage.ui.upload;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.onlinestorage.EndPoints;
import com.example.onlinestorage.HttpParse;
import com.example.onlinestorage.MainActivity;
import com.example.onlinestorage.R;
import com.example.onlinestorage.VolleyMultipartRequest;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private static final int SELECT_VIDEO = 3;
    private static final String TAG = "";
    EditText txt_directorate;
    EditText txt_rdsospecs;
    EditText txt_vendor;
    EditText txt_vendorid;
    EditText txt_fileno;
    EditText txt_itemname;
    TextView filename;
    String EmailHolder;
    String Directorate;
    String User;
    Button select_file;
    Button upload;
    String vendor;
    String vendorid;
    String fileno;
    String itemname;
    String rdsospecs;
    Boolean CheckEditText;
    String HttpURL = "http://192.168.1.100:8585/OnlineStorage/UserRegistration.php";
    private final String upload_URL = "http://192.168.1.100:8585/OnlineStorage/uploadfile.php?";
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    ProgressDialog progressDialog;
    String get_filename;
    private HomeViewModel homeViewModel;
    private String selectedPath;
    private Bitmap bitmap;
    String url = "https://www.google.com";
    Uri uri;
    ArrayList<Uri> arrayList = new ArrayList<>();
    String displayName = null;
    private ArrayList<HashMap<String, String>> arraylist;
    private RequestQueue rQueue;

    ArrayList<Bitmap> images = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_upload, container, false);

        select_file = root.findViewById(R.id.select_file);
        txt_directorate = root.findViewById(R.id.directorate);
        txt_vendor = root.findViewById(R.id.vendor);
        txt_vendorid = root.findViewById(R.id.vendorid);
        txt_fileno = root.findViewById(R.id.fileno);
        txt_itemname = root.findViewById(R.id.itemname);
        txt_rdsospecs = root.findViewById(R.id.rdsospecs);
        upload = root.findViewById(R.id.upload);
        filename = root.findViewById(R.id.filename);

        select_file.setOnClickListener(v -> imageChooser());

        MainActivity activity = (MainActivity) getActivity();
        Map<String, String> myDataFromActivity = activity.getUserData();

        User = myDataFromActivity.get("User");
        Directorate = myDataFromActivity.get("Directorate");

        txt_directorate.setText(Directorate);

        txt_vendor.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        txt_vendorid.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        txt_fileno.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        txt_itemname.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        txt_rdsospecs.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        upload.setOnClickListener(view -> {

            CheckEditTextIsEmptyOrNot();

            if (CheckEditText) {

                //uploadBitmap(bitmap);
                uploadPDF(displayName, uri);
            } else {

                Toast.makeText(getContext(), "Please fill all form fields.", Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {

            uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();


            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getContext().getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.d("nameeeee>>>>  ", displayName);

                        get_filename = displayName;
                        filename.setText(displayName);
                        //uploadPDF(displayName,uri);
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
                Log.d("nameeeee>>>>  ", displayName);
            }

        }
    }

    private void uploadPDF(final String pdfname, Uri pdffile) {

        InputStream iStream = null;
        try {

            iStream = getContext().getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.UPLOAD_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.d("ressssssoo", new String(response.data));
                            rQueue.getCache().clear();

                            progressDialog.dismiss();

                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                //Toast.makeText(getContext().getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                new MaterialAlertDialogBuilder(getContext()).setTitle("File Uploaded Successfully.").setPositiveButton("Ok", (dialogInterface, i) -> {

                                    filename.setText("");
                                    txt_rdsospecs.getText().clear();
                                    txt_vendor.getText().clear();
                                    txt_vendorid.getText().clear();
                                    txt_fileno.getText().clear();
                                    txt_itemname.getText().clear();
                                }).show();

//                                jsonObject.toString().replace("\\\\","");
//
//                                if (jsonObject.getString("status").equals("true")) {
//                                    Log.d("come::: >>>  ","yessssss");
//                                    arraylist = new ArrayList<HashMap<String, String>>();
//                                    JSONArray dataArray = jsonObject.getJSONArray("data");
//
//
//                                    for (int i = 0; i < dataArray.length(); i++) {
//                                        JSONObject dataobj = dataArray.getJSONObject(i);
//                                        url = dataobj.optString("pathToFile");
//                                        //tv.setText(url);
//                                    }
//
//
//                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    error -> Toast.makeText(getContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show()) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("vendor", vendor);
                    params.put("vendorid", vendorid);
                    params.put("fileno", fileno);
                    params.put("itemname", itemname);
                    params.put("rdsospecs", rdsospecs);
                    params.put("directorate", Directorate);
                    params.put("user", User);

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

            progressDialog = ProgressDialog.show(getContext(), "Loading Data", null, true, true);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CheckEditTextIsEmptyOrNot() {

        vendor = txt_vendor.getText().toString();
        vendorid = txt_vendorid.getText().toString();
        fileno = txt_fileno.getText().toString();
        itemname = txt_itemname.getText().toString();
        rdsospecs = txt_rdsospecs.getText().toString();

        CheckEditText = !TextUtils.isEmpty(vendor) && !TextUtils.isEmpty(vendorid) && !TextUtils.isEmpty(fileno) && !TextUtils.isEmpty(itemname) && !TextUtils.isEmpty(get_filename);
    }

    void imageChooser() {

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 100);

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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