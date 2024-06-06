package org.sportApp.userInterface.sportsman.ui.plans;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.sportApp.requests.BackendService;
import org.sportApp.training.PlanDto;
import org.sportApp.training.TrainingEventDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.sportsman.ui.trainingEvents.CreatingTypeSelectionWindow;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EditPlanWindow extends AppCompatActivity {
    private final PlanDto planDto = new PlanDto();
    private final List<TrainingEventDto> trainingEvents = new ArrayList<>();
    private ActivityResultLauncher<Intent> addEventLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plan);
        Button saveChangesButton = findViewById(R.id.buttonSaveChanges);

        EditText nameEditText = findViewById(R.id.editTextName);
        nameEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String planName = textView.getText().toString().trim();
                if (!planName.isEmpty()) {
                    textView.setEnabled(false);
                    saveChangesButton.requestFocus();
                    return true;
                }
            }
            return false;
        });
        addEventLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    TrainingEventDto trainingEventDto = (TrainingEventDto) result.getData().getSerializableExtra("trainingEventDto");
                    trainingEvents.add(trainingEventDto);
                }
            }
        });


        Button addEvent = findViewById(R.id.buttonAddTraining);
        addEvent.setOnClickListener(v -> {
            Intent intent = new Intent(EditPlanWindow.this, CreatingTypeSelectionWindow.class);
            addEventLauncher.launch(intent);
        });
        saveChangesButton.setOnClickListener(v -> {
            planDto.setSportsmanId(UserManager.getInstance().getUserId());
            planDto.setName(nameEditText.getText().toString());
            saveChanges(planDto);
        });
    }

    private void saveChanges(@NonNull PlanDto planDto) {
        planDto.setTrainings(trainingEvents);
        createPlan(planDto);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("planDto", planDto);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

//    private void createPlan(PlanDto planDto) {
//        BackendService.createPlan(planDto).thenAccept(resultDto -> {
//            planDto.setPlanId(resultDto);
//            Log.d("myTag", "plan's id: " + resultDto);
//        }).exceptionally(e -> {
//            Log.e("myTag", "Failed to create plan.", e);
//            return null;
//        }).join();
//    }

    private void createPlan(PlanDto planDto) {
        BackendService.createPlan(planDto)
                .thenCompose(resultDto -> {
                    planDto.setPlanId(resultDto);
                    Log.d("myTag", "training's id: " + resultDto);

                    List<CompletableFuture<Long>> futures = new ArrayList<>();
                    for (TrainingEventDto event : planDto.getTrainings()) {
                        CompletableFuture<Long> future = addEventByPlan(event, resultDto);
                        futures.add(future);
                    }
                    return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
                })
                .thenAccept(voidResult -> Log.d("myTag", "All exercises added successfully."))
                .exceptionally(e -> {
                    Log.e("myTag", "Failed to create training or exercises.", e);
                    return null;
                }).join();
    }

    private CompletableFuture<Long> addEventByPlan(TrainingEventDto event, Long planId) {
        return BackendService.addEventByPlan(event, planId)
                .thenApply(resultDto -> {
                    Log.d("myTag", "exercise's id: " + resultDto);
                    event.setEventId(resultDto);
                    return resultDto;
                }).exceptionally(e -> {
                    //Toast.makeText(AuthorizationWindow.this, "Authorization failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                });
    }
}