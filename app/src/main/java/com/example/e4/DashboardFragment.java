package com.example.e4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardFragment extends Fragment {

    ListView serial_list;
    String HttpURL = "http://3.222.104.176/index.php/planning";
    String HttpURLGet = "http://3.222.104.176/index.php/getvehicleserialno";
    HashMap<String, String> hashMap = new HashMap<>();
    com.example.e4.HttpParse httpParse = new com.example.e4.HttpParse();
    ProgressDialog progressDialog;
    String user_id = "";
    String finalResult;
    public static final String MyPREFERENCES = "Dashboard";
    SharedPreferences sp_vehicle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        sp_vehicle = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences preferences = getActivity().getSharedPreferences("Dashboard", 0);
        preferences.edit().remove("serial_no").commit();

        serial_list = root.findViewById(R.id.list);

        GetSerialNo();

        SharedPreferences sharedpreferences_1 = getActivity().getSharedPreferences(UserLoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = sharedpreferences_1.getString("user_id","");

        serial_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String serial_no = (String) (serial_list.getItemAtPosition(position));

                //Toast.makeText(getActivity(),serial_no, Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = sp_vehicle.edit();
                editor.putString("serial_no", serial_no);
                editor.apply();

                Navigation.findNavController(view).navigate(R.id.nav_steps);
            }
        });

        return root;
    }

    public void GetSerialNo() {

        class GetSerialNoClass extends AsyncTask<String, Void, String> {

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
                            //System.out.println(obj_drawing.getString("serial_no"));
                            drawing_list.add(obj_drawing.getString("serial_no"));
                        }

                        ArrayAdapter<String> arr = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, drawing_list);
                        serial_list.setAdapter(arr);
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

        GetSerialNoClass getSerialNoClass = new GetSerialNoClass();
        getSerialNoClass.execute();
    }
}