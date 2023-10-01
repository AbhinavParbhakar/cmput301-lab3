package com.example.listycity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddExpenseFragment.OnFragmentInteractionListener {

    private ArrayList<Expense> dataList;
    private ListView expenseListView;
    private ArrayAdapter<Expense> expenseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataList = new ArrayList<>();


        expenseListView = findViewById(R.id.city_list);

        expenseListAdapter = new CustomList(this, dataList);
        expenseListView.setAdapter(expenseListAdapter);

        final FloatingActionButton addButton = findViewById(R.id.add_city_buton);
        addButton.setOnClickListener(v -> {
            new AddExpenseFragment().show(getSupportFragmentManager(), "ADD_CITY");
        });

        expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AddExpenseFragment(dataList.get(i)).show(getSupportFragmentManager(), "ADD_CITY");
            }
        });
    }

    @Override
    public void onOKPressed(Expense expense, boolean edit) {
        System.out.println("Triggered onclick");
        if (edit == false) {
            dataList.add(expense);
            System.out.println("Added expense");
        }
        expenseListAdapter.notifyDataSetChanged();
    }

}