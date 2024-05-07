package org.sportApp.userInterface.sportsman.ui.account;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.sportApp.userInterface.R;

public class SearchWindow extends Fragment {

    private EditText editTextSearch;

    public SearchWindow() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        Button buttonSearch = view.findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(v -> performSearch());
    }

    @SuppressLint("SetTextI18n")
    private void performSearch() {
        String query = editTextSearch.getText().toString().trim();
    }
}
