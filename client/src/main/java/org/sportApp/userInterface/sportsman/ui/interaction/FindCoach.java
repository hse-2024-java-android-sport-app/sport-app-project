package org.sportApp.userInterface.sportsman.ui.interaction;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
    List<UserDto> fakeCoach = new ArrayList<>();

    public FindCoach() {
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonSearch = view.findViewById(R.id.buttonSearch);
        name = view.findViewById(R.id.editTextSearch);
        name.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String trainingName = textView.getText().toString().trim();
                if (!trainingName.isEmpty()) {
                    textView.setEnabled(false);
                    buttonSearch.requestFocus();
                    return true;
                }
            }
            return false;
        });
        buttonSearch.setOnClickListener(
                v -> {
                    if (name != null) {
                        searchCoaches(name.getText().toString());
                        createFakeCoach();
                        RecyclerView coachRecyclerView = view.findViewById(R.id.recyclerViewCoaches);
                        FindCoachAdapter currentAdapter = new FindCoachAdapter(fakeCoach, new FindCoachAdapter.OnItemClickListener() {
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

//    @SuppressLint("SetTextI18n")
//    private UserRegistrationDto performSearch() {
//        String query = editTextSearch.getText().toString().trim();
//        UserRegistrationDto userDto = new UserRegistrationDto();
//        String[] words = query.split("\\s+");
//        if (words.length == 1) {
//            String login = words[0];
//            userDto.setLogin(login);
//        } else if (words.length == 2) {
//            String firstName = words[0];
//            String secondName = words[1];
//            userDto.setFirstName(firstName);
//            userDto.setSecondName(secondName);
//        } else {
//            return null;
//        }
//        return userDto;
//    }

    private void searchCoaches(String userName) {
        BackendService.searchCoaches(userName)
                .thenAccept(resultDto -> {
                    if (resultDto != null) {
                        List<UserDto> coaches = resultDto;
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

    private void createFakeCoach() {
        for (int i = 0; i < 10; i++) {
            UserDto fakeUser = new UserDto();
            fakeUser.setLogin("mediana10" + i);
            fakeUser.setFirstName("Fake");
            fakeUser.setSecondName("User");
            fakeCoach.add(fakeUser);
        }
    }
}
