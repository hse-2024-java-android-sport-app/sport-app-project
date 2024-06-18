package org.sportApp.userInterface.coach.ui.listOfSportsmen;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.sportApp.dto.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CoachListOfSportsmenViewModel extends ViewModel {
    private List<UserDto> userList;
    private MutableLiveData<UserAdapter> mUserAdapter;

    public CoachListOfSportsmenViewModel() {
        userList = new ArrayList<>();
//        Random random = new Random();
//        for (int i = 0; i < 50; ++i) {
//            UserDto user = new UserDto();
//            user.setFirstName(nameExamples.get(random.nextInt(nameExamples.size())));
//            user.setSecondName(surnameExamples.get(random.nextInt(surnameExamples.size())));
//            user.setType(UserDto.Kind.sportsman);
//            user.setId((long) (i + 1));
//            userList.add(user);
//        }
        BackendService.getSportsmen(UserManager.getInstance().getId()).thenAccept(resultDto -> {
            userList = resultDto;
            Log.d("UserType", "resultDto: " + resultDto);
        }).exceptionally(e -> null).join();
    }

    public void setUserAdapterWithContext(android.content.Context context) {
        mUserAdapter = new MutableLiveData<>();
        mUserAdapter.setValue(new UserAdapter(context, userList));
    }

    public MutableLiveData<UserAdapter> getUserAdapter() {
        return mUserAdapter;
    }
}
