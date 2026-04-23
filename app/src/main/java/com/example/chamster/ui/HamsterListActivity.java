package com.example.chamster.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.chamster.R;
import com.example.chamster.data.HamsterRepository;

public class HamsterListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_list);

        RecyclerView recycler = findViewById(R.id.recyclerAnimals);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        HamsterAdapter adapter = new HamsterAdapter(this, HamsterRepository.getItems());
        recycler.setAdapter(adapter);
    }
}
