package com.huji.foodtricks.buddies;

import com.goodiebag.horizontalpicker.HorizontalPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides parameters for creating new event.
 */
class EventParametersProvider {

    static ArrayList<HorizontalPicker.PickerItem> getWhoItems(List<String> groupNames) {
        ArrayList<HorizontalPicker.PickerItem> items = new ArrayList<>();
        for (String groupName :
                groupNames) {
            items.add(new HorizontalPicker.TextItem(groupName));
        }
        return items;
    }



    static ArrayList<HorizontalPicker.PickerItem> getWhenItems(){
        ArrayList<HorizontalPicker.PickerItem> items = new ArrayList<>();
        items.add(new HorizontalPicker.TextItem("Tonight"));
        items.add(new HorizontalPicker.TextItem("Tomorrow"));
        items.add(new HorizontalPicker.TextItem("Custom"));

        return items;
    }
}
