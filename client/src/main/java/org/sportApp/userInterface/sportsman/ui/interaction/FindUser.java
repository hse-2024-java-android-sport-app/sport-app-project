package org.sportApp.userInterface.sportsman.ui.interaction;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.sportApp.dto.UserDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.FindUserAdapter;
import org.sportApp.userInterface.sportsman.ui.overview.BaseFragment;

import java.util.ArrayList;
import java.util.List;


public abstract class FindUser extends BaseFragment<UserDto> {

    private EditText name;
    List<UserDto> users = new ArrayList<>();


    @Override
    protected BaseAdapter<UserDto, ? extends BaseAdapter.BaseViewHolder<UserDto>> createAdapter() {
        return new FindUserAdapter(users, new BaseAdapter.OnItemClickListener<UserDto>() {
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_find_user;
    }

    @Override
    protected int getRecyclerView() {
        return R.id.recyclerViewCoaches;
    }


    protected int getSearchButton() {
        return R.id.buttonSearch;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int searchButton = getSearchButton();
        Button buttonSearch = view.findViewById(searchButton);
        editSearchField(view, buttonSearch);
//        buttonSearch.setOnClickListener(v -> {
//            if (name != null) {
//                searchUsers(name.getText().toString());
//                for (int i = 0; i < 10; i++) {
//                    UserDto fakeCoach = new UserDto();
//                    fakeCoach.setFirstName("Fake");
//                    fakeCoach.setSecondName("Coach");
//                    users.add(fakeCoach);
//                }
//                name.setText("");
//                super.onViewCreated(view, savedInstanceState);
//            } else {
//                Toast.makeText(getContext(), "Enter the coach's first name and second name or login", Toast.LENGTH_SHORT).show();
//            }
//            buttonSearch.setVisibility(View.GONE);
//        });
    }

    protected abstract void searchUsers(String userName);

    void editSearchField(@NonNull View view, Button buttonSearch) {
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
