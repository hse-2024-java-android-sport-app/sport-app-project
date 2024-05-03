package org.sportApp.userInterface.coach.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.sportApp.userInterface.coach.MainActivity;

public class MainAccountViewModel extends ViewModel {
    private final MutableLiveData<String> mUserName;
    private final MutableLiveData<String> mUserType;
    public MainAccountViewModel() {
        mUserName = new MutableLiveData<>();
        mUserType = new MutableLiveData<>();
        mUserName.setValue(MainActivity.mainUser.getName());
        if (MainActivity.mainUser.getType() == 0) {
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
