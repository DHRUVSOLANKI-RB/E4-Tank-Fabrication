package com.example.e4.ui.report;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.e4.R;

public class AddDrawingFragment extends Fragment {

    AutoCompleteTextView loading;
    String[] arr_loading = {"Top", "Bottom"};
    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_drawing, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, arr_loading);
        loading = (AutoCompleteTextView) root.findViewById(R.id.loading);
        loading.setThreshold(1);
        loading.setAdapter(adapter);
        loading.setTextColor(Color.BLACK);

        return root;
    }
}