package com.huji.foodtricks.buddies.ui.viewsingleevent;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huji.foodtricks.buddies.R;

public class ViewSingleEventFragment extends Fragment {

    private ViewSingleEventViewModel mViewModel;

    public static ViewSingleEventFragment newInstance() {
        return new ViewSingleEventFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_single_event_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ViewSingleEventViewModel.class);
        // TODO: Use the ViewModel
    }

}
