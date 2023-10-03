package com.example.listycity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddExpenseFragment.OnFragmentInteractionListener {
    //Main class that handles all activity in the main activity
    //sets the logic for the handling clicks and long holds on items
    //set the logic for updating the totalCost
    private ArrayList<Expense> dataList;
    private ListView expenseListView;
    private ArrayAdapter<Expense> expenseListAdapter;

    private TextView totalCost;


    private void setTotalCostView(ArrayList<Expense> expenses) {
        //takes in list of expenses and adds up the expenses and stores them in totalCost
        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getMonthlyCharge();
        }

        totalCost.setText("" + total);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //defines all of the widgets on the main activity
        //creates new list for storing info and passes it into the custom adapter
        //creates onclick handlers for the add button and the list.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataList = new ArrayList<>();

        totalCost = findViewById(R.id.total_cost);
        expenseListView = findViewById(R.id.city_list);

        expenseListAdapter = new CustomList(this, dataList);
        expenseListView.setAdapter(expenseListAdapter);

        final FloatingActionButton addButton = findViewById(R.id.add_city_buton);
        //simply show a new add expense dialog, no edit mode
        addButton.setOnClickListener(v -> {
            new AddExpenseFragment().show(getSupportFragmentManager(), "ADD_CITY");
        });

        expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //show a new edit expense dialog that passes in the appropriate object
            //enables you to edit information
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AddExpenseFragment(dataList.get(i)).show(getSupportFragmentManager(), "ADD_CITY");
            }

        });

        expenseListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            //simply get the position of the long held item and delete it
            //call setTotalCostView to update TotalCost view
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                dataList.remove(i);
                expenseListAdapter.notifyDataSetChanged();
                setTotalCostView(dataList);
                return true;
            }
        });

    }

    @Override
    public void onOKPressed(Expense expense, boolean edit, boolean error,String message) {
        //used to handle adding information into list
        //if error state, them simply create another dialog
        //set the error state to true, and pass along the old message
        //if not, if the object is already in the list, then simply notify adapter of change
        //if object is new to list, then add it to the list
        //notify adapter of change
        //call setTotalCostView to update TotalCost view

        if (!error) {
            if (!dataList.contains(expense)) {
                dataList.add(expense);
            }
            expenseListAdapter.notifyDataSetChanged();
            setTotalCostView(dataList);
        }else{
            new AddExpenseFragment(expense,true,message).show(getSupportFragmentManager(), "ADD_CITY");
        }
    }


}