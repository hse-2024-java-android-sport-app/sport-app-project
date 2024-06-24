package org.sportApp.userInterface.sportsman.ui.trainings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.sportApp.dto.TrainingDto;
import org.sportApp.dto.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.TrainingsAdapter;
import org.sportApp.userInterface.sportsman.ui.overview.BaseFragment;
import org.sportApp.userInterface.sportsman.ui.overview.FragmentWithAddButton;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.List;

public class MyTrainings extends FragmentWithAddButton<TrainingDto> {
    private List<TrainingDto> trainings = new ArrayList<>();

    @Override
    protected Class<?> getAddWindowClass() {
        return CreateTraining.class;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_trainings;
    }

    @Override
    protected int getRecyclerView() {
        return R.id.trainingRecyclerView;
    }

    @Override
    protected int getAddButtonId() {
        return R.id.addTrainingButton;
    }

    @Override
    protected Class<?> getShowWindowClass() {
        return OneTraining.class;
    }

    @Override
    protected BaseAdapter<TrainingDto, ? extends BaseAdapter.BaseViewHolder<TrainingDto>> createAdapter() {
        return new TrainingsAdapter(trainings, new TrainingsAdapter.OnItemClickListener<TrainingDto>() {
            @Override
            public void onItemClick(int position) {
                showTraining(position);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_trainings, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        UserDto currentUser;
        if (UserManager.getInstance().getType().equals(UserDto.Kind.coach)) {
            currentUser = UserManager.getLastUser();
        }
        else {
            currentUser = UserManager.getInstance();
        }
        getAllTrainings(currentUser.getId());
        super.onViewCreated(view, savedInstanceState);
        super.startAddButton(view);
    }

    private void showTraining(int position) {
        super.showItem(position, trainings, "trainingDto");
    }

    private void getAllTrainings(Long userId) {
        BackendService.getAllTrainings(userId).thenAccept(resultDto -> {
                    trainings = resultDto;
                    Log.d("UserType", "resultDto: " + resultDto);
                })
                .exceptionally(e -> null).join();
    }
}
