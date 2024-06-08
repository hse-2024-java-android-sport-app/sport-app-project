package org.sportApp.userInterface.sportsman.ui.interaction;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.sportApp.dto.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.FindCoachAdapter;
import org.sportApp.userInterface.sportsman.ui.overview.BaseFragment;

import java.util.ArrayList;
import java.util.List;


public class FindCoach extends BaseFragment<UserDto> {

    private EditText name;
    List<UserDto> coaches = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.fragment_search;
    }

    @Override
    protected int getRecyclerView() {
        return R.id.recyclerViewCoaches;
    }


    @Override
    protected BaseAdapter<UserDto, ? extends BaseAdapter.BaseViewHolder<UserDto>> createAdapter() {
        return new FindCoachAdapter(coaches, new BaseAdapter.OnItemClickListener<UserDto>() {
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonSearch = view.findViewById(R.id.buttonSearch);
        editSearchField(view, buttonSearch);
        buttonSearch.setOnClickListener(v -> {
            if (name != null) {
                searchCoaches(name.getText().toString());
                for (int i = 0; i < 10; i++) {
                    UserDto fakeCoach = new UserDto();
                    fakeCoach.setFirstName("Fake");
                    fakeCoach.setSecondName("Coach");
                    coaches.add(fakeCoach);
                }
                name.setText("");
                super.onViewCreated(view, savedInstanceState);
            } else {
                Toast.makeText(getContext(), "Enter the coach's first name and second name or login", Toast.LENGTH_SHORT).show();
            }
            buttonSearch.setVisibility(View.GONE);
        });
    }

    private void searchCoaches(String userName) {
        BackendService.searchCoaches(userName).thenAccept(resultDto -> {
            if (resultDto != null) {
                coaches = resultDto;
            } else {
                Toast.makeText(getContext(), "Coach not found", Toast.LENGTH_SHORT).show();
            }
        }).exceptionally(e -> {
            Log.d("SearchWindow", "Search failed: " + e.getMessage(), e);
            Toast.makeText(getContext(), "Search failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        });
    }

    private void editSearchField(@NonNull View view, Button buttonSearch) {
        name = view.findViewById(R.id.editTextSearch);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonSearch.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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
                buttonSearch.setVisibility(View.VISIBLE);
            }
            return false;
        });
    }
}
