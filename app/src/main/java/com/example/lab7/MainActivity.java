package com.example.lab7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DaysAdapter daysAdapter;
    private DaysRepository daysRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        daysRepository = DaysRepository.getInstance();
        recyclerView = findViewById(R.id.recycler_days);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        daysAdapter = new DaysAdapter(daysRepository.getDays(), day -> {
            Intent intent = new Intent(MainActivity.this, TasksActivity.class);
            intent.putExtra("day_id", day.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(daysAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_call_teacher) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(android.net.Uri.parse("tel:123456789"));
            startActivity(callIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();

        daysAdapter.notifyDataSetChanged();
    }

}