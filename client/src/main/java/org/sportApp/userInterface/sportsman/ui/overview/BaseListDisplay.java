package org.sportApp.userInterface.sportsman.ui.overview;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.sportApp.userInterface.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListDisplay<T> extends BaseFragment<T> {

    @Override
    protected int getLayout() {
        return R.layout.fragment_common;
    }

    @Override
    protected int getRecyclerView() {
        return R.id.commonRecyclerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getItems();
        super.onViewCreated(view, savedInstanceState);
        ImageButton addButton = view.findViewById(R.id.addCommonButton);
        addButton.setVisibility(View.GONE);
    }


    protected abstract void getItems();
}