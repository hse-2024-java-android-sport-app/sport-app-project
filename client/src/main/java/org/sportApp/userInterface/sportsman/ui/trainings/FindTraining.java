package org.sportApp.userInterface.sportsman.ui.trainings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.sportApp.userInterface.R;

public class FindTraining extends AppCompatActivity {

    EditText editTextSearch;
    Button buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_training);

        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);

        buttonSearch.setOnClickListener(v -> {
            String searchQuery = editTextSearch.getText().toString();
        });
    }
}