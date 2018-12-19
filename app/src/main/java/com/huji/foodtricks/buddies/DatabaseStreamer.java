package com.huji.foodtricks.buddies;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huji.foodtricks.buddies.Models.EventModel;
import com.huji.foodtricks.buddies.Models.UserModel;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

interface EventFetchingCompletion {
    void onFetchSuccess(EventModel model);
    void onNoEventFound();
}

/**
 * Interface for passing methods to execute after fetching a user model.
 */
interface UserFetchingCompletion {
    void onFetchSuccess(UserModel model);
    void onNoUserFound();
}


/**
 * Interface for passing methods to execute after fetching a list of EventModels.
 */
interface EventMapFetchingCompletion {
    void onFetchSuccess(Map<String, EventModel> modelList);
    void onNoEventsFound();
}

/**
 * Interface for passing methods to execute after updating a UserModel.
 */
interface UserUpdateCompletion {
    void onUpdateSuccess();
}

/**
 * Interface for passing methods to execute after updating a UserModel.
 */
interface EventUpdateCompletion {
    void onUpdateSuccess();
}

/**
 * Interface for passing methods to execute after updating all UserModels who are invitees of Event.
 */
interface AddEventToUsersCompletion {
    void onSuccess();
}


/**
 * Object used to read/write event and user objects from Firebase.
 */
public class DatabaseStreamer {

    private DatabaseReference mDatabase;

    public DatabaseStreamer() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    /// Writing

    public String writeNewEventModel(EventModel eventModel) {
        DatabaseReference newEventRef = mDatabase.child("events").push();
        newEventRef.setValue(eventModel);
        return newEventRef.getKey();
    }

    public void writeNewUserModel(UserModel userModel, String userAuthenticationId) {
        DatabaseReference newUserModelRef = mDatabase.child("users").child(userAuthenticationId);
        newUserModelRef.setValue(userModel);
    }

    /// Updating

    public void modifyEvent(EventModel modifiedEventModel, String eventModelId,
                            final EventUpdateCompletion completion){
        mDatabase.child("events").child(eventModelId).setValue(modifiedEventModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        completion.onUpdateSuccess();
                    }
                });
    }

    public void modifyUser(UserModel modifiedUserModel, String userModelId,
                           final UserUpdateCompletion completion){
        mDatabase.child("users").child(userModelId).setValue(modifiedUserModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        completion.onUpdateSuccess();
                    }
                });
    }


    /// Fetching

    public void fetchEventModelById(String eventId, final EventFetchingCompletion completion) {
        DatabaseReference ref = mDatabase.child("events").child(eventId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    completion.onNoEventFound();
                    return;
                }
                EventModel eventModel = dataSnapshot.getValue(EventModel.class);
                completion.onFetchSuccess(eventModel);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("fetchError", error.getMessage());
            }
        });
    }

    public void fetchUserModelById(String userModelId, final UserFetchingCompletion completion) {
        DatabaseReference ref = mDatabase.child("users").child(userModelId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    completion.onNoUserFound();
                    return;
                }
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                completion.onFetchSuccess(userModel);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("fetchError", error.getMessage());
            }
        });
    }

    public void fetchEventModelsMapForCurrentUser(final EventMapFetchingCompletion completion) {
        String userAuthenticationId = FirebaseAuth.getInstance().getUid();
        fetchEventModelsMapForUserId(userAuthenticationId, completion);
    }

    public void fetchEventModelsMapForUserId(String userId,
                                          final EventMapFetchingCompletion completion) {
        fetchUserModelById(userId, new UserFetchingCompletion() {
            @Override
            public void onFetchSuccess(UserModel user) {
                List<String> eventIdsList = user.getEventIDs();
                fetchEventModelsForEventIdsList(eventIdsList, completion);
            }

            @Override
            public void onNoUserFound() { // log that the current user has no user in the DB?
                completion.onNoEventsFound();
            }
        });
    }

    // fetch helper

    private void fetchEventModelsForEventIdsList(final List<String> eventModelIds,
                                                 final EventMapFetchingCompletion completion) {
        final int numberOfEvents = eventModelIds.size();
        final HashMap<String, EventModel> modelsMap = new HashMap<>(numberOfEvents);
        final AtomicInteger finishedCount = new AtomicInteger(0);
        final AtomicBoolean stopFetching = new AtomicBoolean(false);

        for (final String eventModelId: eventModelIds) {
            fetchEventModelById(eventModelId, new EventFetchingCompletion() {
                @Override
                public void onFetchSuccess(EventModel model) {
                    int index = finishedCount.getAndAdd(1);
                    modelsMap.put(eventModelId, model);
                    if (index == numberOfEvents - 1 && !stopFetching.get()) {
                        completion.onFetchSuccess(modelsMap);
                    }
                }

                // currently stops fetching if a single non-existing event was requested.
                @Override
                public void onNoEventFound() {
                    stopFetching.set(true);
                    completion.onNoEventsFound();
                }
            });
        }
    }

    // Adding an event to users

    public void addEventIdToUserIdList(final List<String> userIdList, String eventId,
                                       final AddEventToUsersCompletion completion){
        final AtomicInteger usersUpdatedCount = new AtomicInteger(0);
        final int usersCount = userIdList.size();

        for (String userId : userIdList) {
            addEventIdToUserById(userId, eventId, new UserUpdateCompletion() {
                @Override
                public void onUpdateSuccess() {
                    int count = usersUpdatedCount.addAndGet(1);
                    if (count == usersCount) {
                        completion.onSuccess();
                    }
                }
            });
        }
    }

    public void addEventIdToUserById(final String userModelId, final String eventId,
                                     final UserUpdateCompletion completion){
        fetchUserModelById(userModelId, new UserFetchingCompletion() {
                    @Override
                    public void onFetchSuccess(UserModel model) {
                        addEventIdToUser(model, userModelId, eventId, completion);
                    }

                    @Override
                    public void onNoUserFound() {
                        Log.d("fetchingFailure", "tried to add event to user that doesn't exist: " + userModelId);
                    }
                });
    }

    // event adding helper

    private void addEventIdToUser(UserModel userModel, String userModelId, String eventId,
                                 final UserUpdateCompletion completion) {
        userModel.addEventId(eventId);
        modifyUser(userModel, userModelId, completion);
    }
}
