package com.huji.foodtricks.buddies;

import com.goodiebag.horizontalpicker.HorizontalPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides parameters for creating new event.
 */
public class EventParametersProvider {

    static ArrayList<HorizontalPicker.PickerItem> getWhoItems(){
        ArrayList<HorizontalPicker.PickerItem> imageItems = new ArrayList<>();
        imageItems.add(new HorizontalPicker.DrawableItem(R.drawable.chili));
        imageItems.add(new HorizontalPicker.DrawableItem(R.drawable.chicken));
        imageItems.add(new HorizontalPicker.DrawableItem(R.drawable.carrot));
        imageItems.add(new HorizontalPicker.TextItem("Custom"));

        return imageItems;
    }



    static ArrayList<HorizontalPicker.PickerItem> getWhenItems(){
        ArrayList<HorizontalPicker.PickerItem> items = new ArrayList<>();
        items.add(new HorizontalPicker.TextItem("Tonight"));
        items.add(new HorizontalPicker.TextItem("Tomorrow"));
        items.add(new HorizontalPicker.TextItem("Custom"));

        return items;
    }
}
