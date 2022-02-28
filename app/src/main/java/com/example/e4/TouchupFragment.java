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

public class TouchupFragment extends Fragment {

    String HttpURL = "http://3.222.104.176/index.php/touchup";
    HashMap<String, String> hashMap = new HashMap<>();
    com.example.e4.HttpParse httpParse = new com.example.e4.HttpParse();
    ProgressDialog progressDialog;
    String finalResult;
    CheckBox main_hole,safety_fittings,ladder,al_top,locking_safety,accessories,final_color,sticker;
    String txt_main_hole = "",txt_safety_fittings = "",txt_ladder = "",txt_al_top = "",txt_locking_safety = "",txt_accessories = "",txt_final_color = "",
            txt_sticker = "",vehicle_unid = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_touchup, container, false);

        Button prev_touchup = root.findViewById(R.id.prev_touchup);
        Button next_touchup = root.findViewById(R.id.next_touchup);

        main_hole = root.findViewById(R.id.main_hole);
        safety_fittings = root.findViewById(R.id.safety_fittings);
        ladder = root.findViewById(R.id.ladder);
        al_top = root.findViewById(R.id.al_top);
        locking_safety = root.findViewById(R.id.locking_safety);
        accessories = root.findViewById(R.id.accessories);
        final_color = root.findViewById(R.id.final_color);
        sticker = root.findViewById(R.id.sticker);

        prev_touchup.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.nav_coloring);
        });

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(HomeFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        vehicle_unid = sharedpreferences.getString("unid","");

        next_touchup.setOnClickListener(view -> {

            if(main_hole.isChecked())
                txt_main_hole = main_hole.getText().toString();
            if(safety_fittings.isChecked())
                txt_safety_fittings = safety_fittings.getText().toString();
            if(ladder.isChecked())
                txt_ladder = ladder.getText().toString();
            if(al_top.isChecked())
                txt_al_top = al_top.getText().toString();
            if(locking_safety.isChecked())
                txt_locking_safety = locking_safety.getText().toString();
            if(accessories.isChecked())
                txt_accessories = accessories.getText().toString();
            if(final_color.isChecked())
                txt_final_color = final_color.getText().toString();
            if(sticker.isChecked())
                txt_sticker = sticker.getText().toString();

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

                                Navigation.findNavController(view).navigate(R.id.nav_checklistpredelivery);

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

                hashMap.put("main_hole", txt_main_hole);
                hashMap.put("safety_fittings", txt_safety_fittings);
                hashMap.put("ladder", txt_ladder);
                hashMap.put("al_top", txt_al_top);
                hashMap.put("locking_safety", txt_locking_safety);
                hashMap.put("accessories", txt_accessories);
                hashMap.put("final_color", txt_final_color);
                hashMap.put("sticker", txt_sticker);
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