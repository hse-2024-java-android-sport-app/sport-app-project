package org.sportApp.userInterface.coach.ui.listOfSportsmen;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.sportApp.registration.UserDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CoachListOfSportsmenViewModel extends ViewModel {
    private final ArrayList<UserDto> userList;
    private MutableLiveData<UserAdapter> mUserAdapter;
    private final static ArrayList<String> nameExamples =
            new ArrayList<>(Arrays.asList("Arina", "Diana", "Misha", "James", "Mary"));
    private final static ArrayList<String> surnameExamples =
            new ArrayList<>(Arrays.asList("I.", "Z.", "M.", "K.", "A."));

    public CoachListOfSportsmenViewModel() {
        userList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 50; ++i) {
            UserDto user = new UserDto();
            user.setFirstName(nameExamples.get(random.nextInt(nameExamples.size())));
            user.setSecondName(surnameExamples.get(random.nextInt(surnameExamples.size())));
            user.setType(UserDto.Kind.sportsman);
            user.setId((long) (i + 1));
            userList.add(user);
        }
    }

    public void setUserAdapterWithContext(android.content.Context context) {
        mUserAdapter = new MutableLiveData<>();
        mUserAdapter.setValue(new UserAdapter(context, userList));
    }

    public ArrayList<UserDto> getUserList() {
        return userList;
    }

    public MutableLiveData<UserAdapter> getUserAdapter() {
        return mUserAdapter;
    }
}
