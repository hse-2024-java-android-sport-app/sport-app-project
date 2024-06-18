package org.sportApp.userInterface.sportsman.ui.overview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public abstract class FragmentWithAddButton<T> extends BaseFragment<T> {
    protected abstract Class<?> getAddWindowClass();

    protected abstract int getAddButtonId();

    protected abstract Class<?> getShowWindowClass();

    protected void startAddButton(@NonNull View view) {
        int buttonId = getAddButtonId();
        ImageButton add = view.findViewById(buttonId);
        Class<?> nextWindow = getAddWindowClass();
        add.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), nextWindow);
            startActivity(intent);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
