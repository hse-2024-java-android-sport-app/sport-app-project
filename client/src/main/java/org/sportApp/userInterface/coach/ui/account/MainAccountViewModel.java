package org.sportApp.userInterface.coach.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.sportApp.registration.UserDto;
import org.sportApp.userInterface.coach.MainActivity;
import org.sportApp.utils.UserManager;

public class MainAccountViewModel extends ViewModel {
    private final MutableLiveData<String> mUserName;
    private final MutableLiveData<String> mUserType;
    public MainAccountViewModel() {
        mUserName = new MutableLiveData<>();
        mUserType = new MutableLiveData<>();
        mUserName.setValue(UserManager.getInstance().getFullName());
        if (UserManager.getInstance().getType() == UserDto.Kind.coach) {
            mUserType.setValue("Coach");
        } else {
            mUserType.setValue("Sportsman");
        }
    }

    public LiveData<String> getUserName() {
        return mUserName;
    }
    public LiveData<String> getUserType() {
        return mUserType;
    }
}
