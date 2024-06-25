package org.sportApp.userInterface.sportsman.ui.plans;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.sportApp.dto.ExerciseDto;
import org.sportApp.dto.PlanDto;
import org.sportApp.dto.TrainingEventDto;
import org.sportApp.dto.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.EventsAdapter;
import org.sportApp.userInterface.adapters.ExercisesAdapter;
import org.sportApp.userInterface.sportsman.ui.events.OneEvent;
import org.sportApp.userInterface.sportsman.ui.events.TypeSelection;
import org.sportApp.userInterface.sportsman.ui.exercise.AddExerciseWindow;
import org.sportApp.userInterface.sportsman.ui.overview.BaseActivity;
import org.sportApp.userInterface.sportsman.ui.overview.BaseCreateActivity;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.List;

public class CreatePlan extends BaseCreateActivity<TrainingEventDto, PlanDto> {
    private final List<TrainingEventDto> events = new ArrayList<>();
    private EventsAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_edit_plan;
    }

    @Override
    protected List<TrainingEventDto> getItems() {
        return events;
    }

    @Override
    protected int getRecyclerView() {
        return R.id.trainingEventRecyclerView;
    }

    @Override
    protected Class<?> getShowWindowClass() {
        return OneEvent.class;
    }

    @Override
    protected String getName() {
        return "PlanDto";
    }

    @Override
    protected void openAddWindow() {
        Intent intent = new Intent(this, TypeSelection.class);
        addEventLauncher.launch(intent);
    }

    @Override
    protected int getAddButton() {
        return R.id.buttonAddTraining;
    }

    @Override
    protected int getSaveButton() {
        return R.id.buttonSaveChanges;
    }

    @Override
    protected int getNameEditText() {
        return R.id.editTextName;
    }

    @Override
    protected BaseAdapter<TrainingEventDto, ? extends BaseAdapter.BaseViewHolder<TrainingEventDto>> getAdapter() {
        return adapter;
    }

    @Override
    protected BaseAdapter<TrainingEventDto, ? extends BaseAdapter.BaseViewHolder<TrainingEventDto>> createAdapter() {
        adapter = new EventsAdapter(events, new BaseAdapter.OnItemClickListener<TrainingEventDto>() {
            @Override
            public void onItemClick(int position) {
                showItem(position, "trainingEventDto");
            }
        });
        return adapter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void save() {
        PlanDto planDto = new PlanDto();
        UserDto currentUser;
        if (UserManager.getInstance().getType().equals(UserDto.Kind.coach)) {
            currentUser = UserManager.getLastUser();
            planDto.setCoachId(UserManager.getInstance().getId());
        }
        else {
            currentUser = UserManager.getInstance();
        }

        planDto.setSportsmanId(currentUser.getId());
        EditText editText = findViewById(getNameEditText());
        planDto.setName(editText.getText().toString());
        planDto.setTrainings(events);
        createPlan(planDto);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("planDto", planDto);
        setResult(RESULT_OK, resultIntent);
        Log.d("ApplicationTag", "CreatePlanWindow: user's id in training " + planDto.getPlanId());
        finish();
    }

    private final ActivityResultLauncher<Intent> addEventLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            assert result.getData() != null;
            TrainingEventDto eventDto = (TrainingEventDto) result.getData().getSerializableExtra("eventDto");
            assert eventDto != null;
//            if (result.getData() != null) {
//                TrainingEventDto eventDto = (TrainingEventDto) result.getData().getSerializableExtra("eventDto");
//                events.add(eventDto);
//                Log.d("ApplicationTag", "CreatePlan: events" + events);
//                adapter.notifyItemInserted(events.size() - 1);
//            }
        }
    });

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            TrainingEventDto eventDto = (TrainingEventDto) data.getSerializableExtra("eventDto");
            if (eventDto != null) {
                events.add(eventDto);
                Log.d("ApplicationTag", "CreatePlanWindow: events" + events);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void createPlan(@NonNull PlanDto planDto) {
        Log.d("ApplicationTag", "CreatePlanWindow: trainingsDto " + (planDto.getTrainings()));
        BackendService.createPlan(planDto).thenAccept(resultDto -> {
                    planDto.setPlanId(resultDto);
                    Log.d("ApplicationTag", "CreatePlanWindow: planId is " + resultDto);
                })
                .exceptionally(e -> {
                    Log.e("ApplicationTag", "CreatePlanWindow: " + e.getMessage(), e);
                    return null;
                }).join();
    }
}