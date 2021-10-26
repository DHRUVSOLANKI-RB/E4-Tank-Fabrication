package com.example.onlinestorage.ui.upload;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.io.IOException;
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
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    ProgressDialog progressDialog;
    String get_filename;
    private HomeViewModel homeViewModel;
    private String selectedPath;
    private Bitmap bitmap;
    ArrayList<Uri> arrayList = new ArrayList<>();

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

        upload.setOnClickListener(view -> {

            CheckEditTextIsEmptyOrNot();

            if (CheckEditText) {

                uploadBitmap(bitmap);
            } else {

                Toast.makeText(getContext(), "Please fill all form fields.", Toast.LENGTH_LONG).show();
            }
        });

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

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {

            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);

                get_filename = getFileName(imageUri);
                filename.setText(get_filename);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.UPLOAD_URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {

                    progressDialog.dismiss();

                    JSONObject obj = new JSONObject(new String(response.data));
                    //Toast.makeText(getContext().getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle("File Uploaded Successfully.")
                            //.setMessage("File Uploaded Successfully.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    filename.setText("");
                                    txt_rdsospecs.getText().clear();
                                    txt_vendor.getText().clear();
                                    txt_vendorid.getText().clear();
                                    txt_fileno.getText().clear();
                                    txt_itemname.getText().clear();
                                }
                            })
                            .show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

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

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                params.put("file_data", new DataPart(get_filename, getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getContext()).add(volleyMultipartRequest);

        progressDialog = ProgressDialog.show(getContext(), "Loading Data", null, true, true);
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

        Intent i = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(i, 100);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}