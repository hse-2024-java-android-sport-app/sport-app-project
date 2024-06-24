package org.sportApp.userInterface.sportsman.ui.overview;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.userInterface.adapters.BaseAdapter;

public abstract class BaseCreateActivity<T, R> extends BaseActivity<T, R> {

    protected abstract void openAddWindow();

    protected abstract void save();

    protected abstract int getAddButton();

    protected abstract int getSaveButton();

    protected abstract int getNameEditText();

    protected abstract BaseAdapter<T, ? extends BaseAdapter.BaseViewHolder<T>> getAdapter();

    @Override
    protected BaseAdapter<T, ? extends BaseAdapter.BaseViewHolder<T>> createAdapter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int recyclerView = getRecyclerView();
        RecyclerView currentRecyclerView = findViewById(recyclerView);
        BaseAdapter<T, ? extends BaseAdapter.BaseViewHolder<T>> currentAdapter = createAdapter();
        currentRecyclerView.setAdapter(currentAdapter);
        currentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton addButton = findViewById(getAddButton());
        addButton.setOnClickListener(v -> openAddWindow());

        Button saveTrainingButton = findViewById(getSaveButton());
        saveTrainingButton.setOnClickListener(v -> save());

        EditText nameEditText = findViewById(getNameEditText());

        nameEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String trainingName = textView.getText().toString().trim();
                if (!trainingName.isEmpty()) {
                    textView.setEnabled(false);
                    saveTrainingButton.requestFocus();
                    return true;
                }
            }
            return false;
        });
    }
}
