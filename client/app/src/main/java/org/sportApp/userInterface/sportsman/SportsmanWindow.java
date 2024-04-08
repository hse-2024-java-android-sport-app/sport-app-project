package org.sportApp.userInterface.sportsman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import org.sportApp.userInterface.R;

public class SportsmanWindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportsman_window);

        Button addTrainingButton = findViewById(R.id.addTrainingButton);

        addTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SportsmanWindow.this, AddTrainingActivity.class);
                startActivity(intent);
            }
        });
    }
}
