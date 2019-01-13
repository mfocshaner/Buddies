package com.huji.foodtricks.buddies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.huji.foodtricks.buddies.Models.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class RSVPListAdapter extends ArrayAdapter {


    //to reference the Activity
    private final Activity context;

    //to store the animal images
    private final UserModel[] UserModels;

    //to store the list of countries
    private final String[] nameArray;

    //to store the list of countries
    private final String[] RSVPArray;

    public RSVPListAdapter(Activity context, String[] nameArrayParam, String[] RSVPArray, UserModel[] userModels) {

        super(context, R.layout.rsvp_list_row_layout, nameArrayParam);


        this.context = context;
        this.UserModels = userModels;
        this.nameArray = nameArrayParam;
        this.RSVPArray = RSVPArray;

    }


    private static void getUsersFromDB(UsersRSVPListAdapterCompletion completion) {
        DatabaseStreamer dbs = new DatabaseStreamer();
        dbs.fetchAllUsersInDBMap(completion);
    }

    public static RSVPListAdapter setupUserList(Activity context,EventAttendanceProvider eventAttendanceProvider) {

        getUsersFromDB(new UsersRSVPListAdapterCompletion() {
            @Override
            public RSVPListAdapter onFetchSuccess(HashMap<String, UserModel> usersMap) {
                RSVPListAdapter rsvpListAdapter = buildRSVPListAdapter(context,eventAttendanceProvider, usersMap);
                ListView listView = context.findViewById(R.id.rsvp_listview);
                listView.setAdapter(rsvpListAdapter);
                return rsvpListAdapter;
            }

            @Override
            public void onNoUsersFound() {
                // TODO: show that there are no users?

            }
        });
        return null;
    }

    public static RSVPListAdapter buildRSVPListAdapter(Activity context,EventAttendanceProvider eventAttendanceProvider, HashMap<String, UserModel> usersMap) {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> RSVPs = new ArrayList<>();
        ArrayList<UserModel> userModels = new ArrayList<>();
        for (String userId : usersMap.keySet()) {
            if (eventAttendanceProvider.getInvitees().keySet().contains(userId)) {
                UserModel userModel = usersMap.get(userId);
                names.add(userModel.getUserName());
                userModels.add(userModel);
                EventAttendanceProvider.RSVP rsvp = eventAttendanceProvider.getUserRSVP(userId);
                switch (rsvp) {
                    case ATTENDING:
                        RSVPs.add("Going");
                        break;
                    case NOT_ATTENDING:
                        RSVPs.add("Not going");
                        break;
                    case TENTATIVE:
                        RSVPs.add("Maybe");
                        break;
                    case NON_RESPONSIVE:
                        RSVPs.add("Didn't response");
                        break;
                }
            }
        }
        String[] namesArray = (String[]) names.toArray(new String[0]);
        String[] RSVPsArray = (String[]) RSVPs.toArray(new String[0]);
        UserModel[] userModelsArray = (UserModel[]) userModels.toArray(new UserModel[userModels.size()]);
        return new RSVPListAdapter(context, namesArray,RSVPsArray,userModelsArray);
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.rsvp_list_row_layout, null, true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.nameTextViewID);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.infoTextViewID);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1ID);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameArray[position]);
        infoTextField.setText(RSVPArray[position]);
        String imageURL = UserModels[position].getImageUrl();
        GlideApp.with(getContext())
                .load(Objects.requireNonNull(imageURL))
                .override(200, 200)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
        return rowView;

    }


}
