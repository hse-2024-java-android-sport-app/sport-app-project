package org.sportApp.userInterface.sportsman.ui.overview;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.userInterface.adapters.BaseAdapter;

import java.io.Serializable;
import java.util.List;

public abstract class BaseFragment<T> extends Fragment {
    protected abstract Class<?> getAddWindowClass();

    protected abstract int getLayout();

    protected abstract int getRecyclerView();

    protected abstract int getAddButtonId();

    protected abstract Class<?> getShowWindowClass();

    protected abstract BaseAdapter<T, ? extends BaseAdapter.BaseViewHolder<T>> createAdapter();

    protected void startAddButton(@NonNull View view) {
        int buttonId = getAddButtonId();
        ImageButton add = view.findViewById(buttonId);
        Class<?> nextWindow = getAddWindowClass();
        add.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), nextWindow);
            startActivity(intent);
        });
    }

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
        startAddButton(view);
    }


    protected void showItem(int position, List<T> items, String dto) {
        if (position != RecyclerView.NO_POSITION) {
            T item = items.get(position);
            Class<?> showClass = getShowWindowClass();
            Intent intent = new Intent(requireContext(), showClass);
            intent.putExtra(dto, (Serializable) item);
            startActivity(intent);
        }
    }
}
