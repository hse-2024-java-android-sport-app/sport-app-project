package org.sportApp.userInterface.sportsman.ui.overview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.userInterface.adapters.BaseAdapter;

public abstract class BaseFragment<T> extends Fragment {
    protected abstract int getLayout();

    protected abstract int getRecyclerView();


    protected abstract BaseAdapter<T, ? extends BaseAdapter.BaseViewHolder<T>> createAdapter();


    protected void setUpAdapter(@NonNull View view, int recyclerView, BaseAdapter<T, ? extends BaseAdapter.BaseViewHolder<T>> currentAdapter) {
        RecyclerView currentRecyclerView = view.findViewById(recyclerView);
        currentRecyclerView.setAdapter(currentAdapter);
        currentRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }


    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layout = getLayout();
        return inflater.inflate(layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int recyclerView = getRecyclerView();
        BaseAdapter<T, ? extends BaseAdapter.BaseViewHolder<T>> currentAdapter = createAdapter();
        setUpAdapter(view, recyclerView, currentAdapter);
    }
}
