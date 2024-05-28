package org.sportApp.userInterface.sportsman.ui.interaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.sportApp.registration.UserDto;
import org.sportApp.requests.BackendService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.FindCoachAdapter;
import org.sportApp.userInterface.adapters.TrainingsAdapter;

import java.util.ArrayList;
import java.util.List;


public class FindCoach extends Fragment {

    private EditText name;
    List<UserDto> coachs = new ArrayList<>();

    public FindCoach() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonSearch = view.findViewById(R.id.buttonSearch);
        name = view.findViewById(R.id.editTextSearch);
        name.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                String trainingName = textView.getText().toString().trim();
                if (!trainingName.isEmpty()) {
                    textView.setEnabled(false);
                    buttonSearch.requestFocus();
                    return true;
                }
            }
            return false;
        });
        buttonSearch.setOnClickListener(v -> {
            if (name != null) {
                searchCoaches(name.getText().toString());
                RecyclerView coachRecyclerView = view.findViewById(R.id.recyclerViewCoaches);
                FindCoachAdapter currentAdapter = new FindCoachAdapter(coachs, new FindCoachAdapter.OnItemClickListener() {
                    @Override
                    public void onItemLongClick(int position) {
                    }

                    @Override
                    public void onItemClick(int position) {
                        //    showTraining(position);
                    }
                });
                coachRecyclerView.setAdapter(currentAdapter);
                coachRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            } else {
                Toast.makeText(getContext(), "Enter the coach's first name and second name or login", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void searchCoaches(String userName) {
        BackendService.searchCoaches(userName).thenAccept(resultDto -> {
            if (resultDto != null) {
                coachs = resultDto;
            } else {
                Toast.makeText(getContext(), "Coach not found", Toast.LENGTH_SHORT).show();
            }
        }).exceptionally(e -> {
            Log.d("SearchWindow", "Search failed: " + e.getMessage(), e);
            Toast.makeText(getContext(), "Search failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        });
    }
}
