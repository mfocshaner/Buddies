package com.huji.foodtricks.buddies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArrayAdapterWithTitle extends ArrayAdapter<CharSequence> {
    private String mSpinnerText = "";

    public ArrayAdapterWithTitle(Context context, String[] options){
        super(context, android.R.layout.simple_spinner_dropdown_item, options);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView tv = (TextView)view.findViewById(android.R.id.text1);
        tv.setText(mSpinnerText);
        return view;
    }

    public void setCustomText(String spinnerText) {
        mSpinnerText = spinnerText;
        notifyDataSetChanged();
    }
}
