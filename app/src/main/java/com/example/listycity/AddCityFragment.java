package com.example.listycity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    private EditText cityName;
    private EditText provinceName;
    private OnFragmentInteractionListener listener;


    private boolean editMode = false;
    private City editableCity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + "OnFragmentInteractionListener is not implemented");
        }
    }

    public AddCityFragment(){}

    public AddCityFragment(City selectedCity){
        this.editMode = true;
        this.editableCity = selectedCity;

    }

    public interface OnFragmentInteractionListener {
        void onOKPressed(City city, boolean edit);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_city_fragment_layout, null);

        cityName = view.findViewById(R.id.city_name_edit_text);
        provinceName = view.findViewById(R.id.province_name_edit_text);

        if (this.editMode){
            cityName.setText(this.editableCity.getName());
            provinceName.setText(this.editableCity.getProvince());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Add Or Edit City")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = cityName.getText().toString();
                        String province = provinceName.getText().toString();
                        if (editMode){
                            editableCity.setName(name);
                            editableCity.setProvince(province);
                            listener.onOKPressed(editableCity,true);
                            editMode = false;
                        }else{
                            listener.onOKPressed(new City(name, province),false);
                        }

                    }
                }).create();
    }
}
