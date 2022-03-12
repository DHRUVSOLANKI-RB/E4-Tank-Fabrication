package com.example.e4;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DashboardFragment extends Fragment {

    ListView l;
    String tutorials[] = { "Algorithms", "Data Structures", "Languages", "Interview Corner", "GATE", "ISRO CS", "UGC NET CS", "CS Subjects", "Web Technologies" };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        l = root.findViewById(R.id.list);
        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, tutorials);
        l.setAdapter(arr);

        return root;
    }
}