package com.huji.foodtricks.buddies;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

interface EventFetchingCompletion {
    public void onResponse(EventModel model);
    public void onError(DatabaseError error);
}

/**
 * Interface for passing methods to execute after fetching a user model.
 */
interface UserFetchingCompletion {
    public void onResponse(UserModel model);
    public void onError(DatabaseError error);
}


/**
 * Interface for passing methods to execute after fetching a list of EventModels.
 */
interface EventListFetchingCompletion {
    public void onResponse(List<EventModel> modelList);
    public void onError(DatabaseError error);
}

/**
 * Interface for passing methods to execute after updating a UserModel.
 */
interface UserUpdateCompletion {
    public void onResponse();
    public void onError(DatabaseError error);
}

/**
 * Interface for passing methods to execute after updating all UserModels who are invitees of Event.
 */
interface AddEventToUsersCompletion {
    public void onResponse();
    public void onError(DatabaseError error);
}


/**
 * Object used to read/write event and user objects from Firebase.
 */
public class DatabaseStreamer {

    private DatabaseReference mDatabase;

    public DatabaseStreamer() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public String writeNewEventModel(EventModel eventModel) {
        DatabaseReference newEventRef = mDatabase.child("events").push();
        newEventRef.setValue(eventModel);
        return newEventRef.getKey();
    }

    public String writeNewUsermodel(UserModel userModel) {
        DatabaseReference newUserModelRef = mDatabase.child("users").push();
        newUserModelRef.setValue(userModel);
        return newUserModelRef.getKey();
    }

    public EventModel getDummyEvent(){
        ArrayList<String> invitees = new ArrayList<>();
        invitees.add("amit");
        invitees.add("michael");
        invitees.add("ido");
        invitees.add("buddy");
        Date today = Calendar.getInstance().getTime();
        return new EventModel("yay", today,  invitees, "stam mishu");
    }

    public UserModel getDummyUser(){
        ArrayList<String> invitees = new ArrayList<>();
        invitees.add("amit");
        invitees.add("michael");
        invitees.add("ido");
        invitees.add("buddy");
        Date today = Calendar.getInstance().getTime();
        return new UserModel("AmitsFakeId", "KingSilber", "Amit", "Silber");
    }


    void fetchUserModelForFirebaseId(String userFirebaseId, final UserFetchingCompletion completion) {
        mDatabase.child("users").orderByChild("userFirebaseId").equalTo(userFirebaseId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        completion.onResponse(userModel);
                        // todo: what happens if there's no such user?
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /// todo: right now the completion is called when all users are updated, we might want to add a
    /// completion for each user. i.e., when user is update - send him a notification.
    /// alternatively, we may want to add listeners to user updates to send this notification.
    /// need to discuss this!
    void addEventIdToUserIdList(final List<String> userIdList, String eventId,
                                final AddEventToUsersCompletion completion){
        final AtomicInteger usersUpdatedCount = new AtomicInteger(0);
        final int usersCount = userIdList.size();
        for (String userId :
                userIdList) {
            addEventIdToUserById(userId, eventId, new UserUpdateCompletion() {
                @Override
                public void onResponse() {
                    int count = usersUpdatedCount.addAndGet(1);
                    if (count == usersCount) {
                        completion.onResponse();
                    }
                }

                @Override
                public void onError(DatabaseError error) {

                }
            });
        }
    }

    void addEventIdToUserById(final String userModelId, final String eventId,
                              final UserUpdateCompletion completion){
        fetchUserModelById(userModelId,
                new UserFetchingCompletion() {
                    @Override
                    public void onResponse(UserModel model) {
                        addEventIdToUser(model, userModelId, eventId, completion);
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        completion.onError(error);
                    }
                });
    }

    void addEventIdToUser(UserModel userModel, String userModelId, String eventId,
                          final UserUpdateCompletion completion){
        userModel.addEventId(eventId);
        mDatabase.child("users").child(userModelId).setValue(userModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                completion.onResponse();
            }
        });
    }

    // todo: If stuff works, should add function here to remove an event from the user.

    void fetchEventModelById(String eventId, final EventFetchingCompletion completion) {
        DatabaseReference ref = mDatabase.child("events").child(eventId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EventModel eventModel = dataSnapshot.getValue(EventModel.class);
                if (eventModel == null) {
                    completion.onError(DatabaseError.fromStatus("No eventModel for given Id"));
                    return;
                }
                completion.onResponse(eventModel);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                completion.onError(error);
            }
            });
    }

    void fetchUserModelById(String userModelId, final UserFetchingCompletion completion) {
        DatabaseReference ref = mDatabase.child("users").child(userModelId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                if (userModel == null) {
                    completion.onError(DatabaseError.fromStatus("No userModel for given Id"));
                    return;
                }
                completion.onResponse(userModel);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                completion.onError(error);
            }
        });
    }

    public void fetchEventModelsForUserId(String userId,
                                          final EventListFetchingCompletion completion) {
        fetchUserModelById(userId, new UserFetchingCompletion() {
            @Override
            public void onResponse(UserModel user) {
                List<String> eventIdsList = user.getEventIDs();
                fetchEventModelsForEventIdsList(eventIdsList, completion);
            }

            @Override
            public void onError(DatabaseError error) {
                completion.onError(error);
            }
        });
    }

    private void fetchEventModelsForEventIdsList(final List<String> eventModelIds,
                                         final EventListFetchingCompletion completion) {
        final int numberOfEvents = eventModelIds.size();
        final ArrayList<EventModel> modelsList = new ArrayList<>(numberOfEvents);
        final AtomicInteger finishedCount = new AtomicInteger(0);

        for (String eventModelId: eventModelIds) {
            fetchEventModelById(eventModelId, new EventFetchingCompletion() {
                @Override
                public void onResponse(EventModel model) {
                    int index = finishedCount.getAndAdd(1);
                    modelsList.set(index, model);
                    if (index == numberOfEvents - 1) {
                        completion.onResponse(modelsList);
                    }
                }

                @Override
                public void onError(DatabaseError error) {
                    completion.onError(error);
                }
            });
        }
    }
}
