package org.sportApp.userInterface.sportsman.ui.account;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.sportApp.registration.UserRegistrationDto;
import org.sportApp.requests.BackendService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.sportApp.userInterface.R;


public class SearchWindow extends Fragment {

    private EditText editTextSearch;

    public SearchWindow() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        Button buttonSearch = view.findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(
                v -> {
                    UserRegistrationDto userDto = performSearch();
                    if (userDto != null) {
                        addCoach(userDto);
                    } else {
                        Toast.makeText(getContext(), "Enter the coach's first name and second name or login", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @SuppressLint("SetTextI18n")
    private UserRegistrationDto performSearch() {
        String query = editTextSearch.getText().toString().trim();
        UserRegistrationDto userDto = new UserRegistrationDto();
        String[] words = query.split("\\s+");
        if (words.length == 1) {
            String login = words[0];
            userDto.setLogin(login);
        } else if (words.length == 2) {
            String firstName = words[0];
            String secondName = words[1];
            userDto.setFirstName(firstName);
            userDto.setSecondName(secondName);
        } else {
            return null;
        }
        return userDto;
    }

    private void addCoach(UserRegistrationDto userDto) {
        BackendService.addCoach(userDto)
                .thenAccept(resultDto -> {
                    if (resultDto != null) {
                        // add coach's id to sportsman's information
                    } else {
                        Toast.makeText(getContext(), "Coach not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .exceptionally(e -> {
                    Log.d("SearchWindow", "Search failed: " + e.getMessage(), e);
                    Toast.makeText(getContext(), "Search failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                });
    }
}
