package com.huji.foodtricks.buddies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huji.foodtricks.buddies.Models.UserModel;

import java.util.HashMap;

public class RSVPListAdapter extends ArrayAdapter {


    //to reference the Activity
    private final Activity context;

    //to store the animal images
    private final Integer[] imageIDarray;

    //to store the list of countries
    private final String[] nameArray;

    //to store the list of countries
    private final String[] infoArray;

    public RSVPListAdapter(Activity context, String[] nameArrayParam, String[] infoArrayParam, Integer[] imageIDArrayParam){

        super(context,R.layout.layout , nameArrayParam);


        this.context=context;
        this.imageIDarray = imageIDArrayParam;
        this.nameArray = nameArrayParam;
        this.infoArray = infoArrayParam;

    }

    private void getUsersFromDB(UsersMapFetchingCompletion completion) {
        DatabaseStreamer dbs = new DatabaseStreamer();
        dbs.fetchAllUsersInDBMap(completion);
    }

    private void setupUserList(EventAttendanceProvider eventAttendanceProvider) {

        getUsersFromDB(new UsersMapFetchingCompletion() {
            @Override
            public void onFetchSuccess(HashMap<String, UserModel> usersMap) {
                buildRSVPListAdapter(eventAttendanceProvider, usersMap);
            }

            @Override
            public void onNoUsersFound() {
                // TODO: show that there are no users?
            }
        });
    }
    public RSVPListAdapter buildRSVPListAdapter(EventAttendanceProvider eventAttendanceProvider, HashMap<String, UserModel> usersMap)
    {
               on
    }



    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.layout, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.nameTextViewID);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.infoTextViewID);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1ID);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameArray[position]);
        infoTextField.setText(infoArray[position]);
        imageView.setImageResource(imageIDarray[position]);

        return rowView;

    };
}
