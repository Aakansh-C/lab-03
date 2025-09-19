package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class EditCityFragment extends DialogFragment {
    interface EditCityDialogListener {
        void editCity(int position,City updatedcity);
    }
    private static final String ARG_POSITION = "arg_position";
    private static final String ARG_CITY = "arg_city";

    private EditCityDialogListener listener;
    private EditText etCity, etProvince;

    public static EditCityFragment newInstance(int position, City city) {
        EditCityFragment frag = new EditCityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putSerializable(ARG_CITY, city);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof EditCityDialogListener) {
            listener = (EditCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement EditCityDialogListener");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_city, null);

        etCity = view.findViewById(R.id.edit_city);
        etProvince = view.findViewById(R.id.edit_province);

        // Read args and prefill
        Bundle args = getArguments();
        final int position = args != null ? args.getInt(ARG_POSITION, -1) : -1;
        final City city = args != null ? (City) args.getSerializable(ARG_CITY) : null;

        // Sets input box to current text, so that it can be edited
        if (city != null) {
            etCity.setText(city.getName());
            etProvince.setText(city.getProvince());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Edit City")
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = etCity.getText().toString().trim();
                    String newProvince = etProvince.getText().toString().trim();

                    if (newName.isEmpty()) {
                        // Toast to tell the user that they the inputs cannot be empty
                        Toast.makeText(getActivity(), "City name cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (city != null && listener != null) {
                        // Update the city object (
                        city.setName(newName);
                        city.setProvince(newProvince);
                        listener.editCity(position, city);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Doesn't need to do anything, automatic exit
                });

        return builder.create();
    }
}