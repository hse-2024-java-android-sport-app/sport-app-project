package org.sportApp.userInterface.coach.ui.listOfSportsmen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CoachListOfSportsmenViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public CoachListOfSportsmenViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is coach list of sportsmen fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
