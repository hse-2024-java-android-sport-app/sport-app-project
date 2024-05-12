package org.sportApp.userInterface.sportsman.ui.trainingEvents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import org.sportApp.userInterface.R;
public class CreatingTypeSelectionWindow extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Button createEvent = findViewById(R.id.createEvent);
        Button chooseEvent = findViewById(R.id.choseEvent);
        createEvent.setOnClickListener(v -> {
            Intent intent = new Intent(CreatingTypeSelectionWindow.this, AddTrainingEventWindow.class);
            startActivity(intent);
        });
        chooseEvent.setOnClickListener(v -> {
            Intent intent = new Intent(CreatingTypeSelectionWindow.this, AddTrainingEventWindow.class);
            startActivity(intent);
        });
    }
}
