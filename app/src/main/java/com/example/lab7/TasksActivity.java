package com.example.lab7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TasksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TasksAdapter tasksAdapter;
    private EditText editTextNewTask;
    private Button buttonAddTask, buttonDone, buttonCompleted;
    private int dayId;
    private DaysRepository daysRepository;
    private Day currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        dayId = getIntent().getIntExtra("day_id", -1);
        daysRepository = DaysRepository.getInstance(this);
        currentDay = daysRepository.getDayById(dayId);

        recyclerView = findViewById(R.id.recycler_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new TasksAdapter(currentDay.getTasks());
        recyclerView.setAdapter(tasksAdapter);

        editTextNewTask = findViewById(R.id.edit_new_task);
        buttonAddTask = findViewById(R.id.button_add_task);
        buttonDone = findViewById(R.id.button_done);
        buttonCompleted = findViewById(R.id.button_completed);

        buttonAddTask.setOnClickListener(v -> {
            String newTaskText = editTextNewTask.getText().toString().trim();
            if (!newTaskText.isEmpty()) {
                currentDay.getTasks().add(new TaskItem(newTaskText, false));
                tasksAdapter.notifyDataSetChanged();
                editTextNewTask.setText("");
                daysRepository.save();
            }
        });

        buttonDone.setOnClickListener(v -> {
            daysRepository.save();
            Intent intent = new Intent(TasksActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        buttonCompleted.setOnClickListener(v -> {
            currentDay.getTasks().clear();
            daysRepository.removeDay(dayId);
            daysRepository.save();
            Intent intent = new Intent(TasksActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
}