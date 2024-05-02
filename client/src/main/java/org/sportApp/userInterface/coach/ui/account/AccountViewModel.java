package org.sportApp.userInterface.coach.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.sportApp.model.User;
import org.sportApp.userInterface.coach.MainActivity;

public class AccountViewModel extends ViewModel {
    public static User lastOpenedAccount = MainActivity.mainUser;
    private final MutableLiveData<String> mUserName;
    private final MutableLiveData<String> mUserType;
    public AccountViewModel() {
        mUserName = new MutableLiveData<>();
        mUserType = new MutableLiveData<>();
        mUserName.setValue(lastOpenedAccount.getName());
        if (lastOpenedAccount.getType() == 0) {
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
