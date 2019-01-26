package com.huji.foodtricks.buddies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.ArrayRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

public class ArrayAdapterWithTitle extends ArrayAdapter<CharSequence> {
    private String mSpinnerText = "";
    private int placeHolderPosition;

    public ArrayAdapterWithTitle(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<CharSequence> objects) {
        super(context, resource, textViewResourceId, addPlaceholderToOptions(objects));
        placeHolderPosition = objects.size();
    }

    public static @NonNull ArrayAdapterWithTitle createFromResource
            (@NonNull Context context, @ArrayRes int textArrayResId, @LayoutRes int textViewResId) {
        final List<CharSequence> strings = Arrays.asList(context.getResources().getStringArray(textArrayResId));
        return new ArrayAdapterWithTitle(context, textViewResId, 0, strings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView tv = view.findViewById(android.R.id.text1);
        tv.setText(mSpinnerText);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = null;
        if (position == placeHolderPosition) {
            TextView tv = new TextView(getContext());
            tv.setVisibility(View.GONE);
            tv.setHeight(0);
            v = tv;
            v.setVisibility(View.GONE);
        }
        else
            v = super.getDropDownView(position, null, parent);
        return v;
    }

    public int getPlaceHolderPosition() {
        return placeHolderPosition;
    }

    public void setCustomText(String spinnerText) {
        mSpinnerText = spinnerText;
        notifyDataSetChanged();
    }

    private static List<CharSequence> addPlaceholderToOptions(@NonNull List<CharSequence> objects){
        ArrayList<CharSequence> list = new ArrayList<>(objects);
        list.add("");
        return list;
    }
}
