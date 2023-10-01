package com.example.listycity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<Expense> {
    private ArrayList<Expense> expenses;
    private Context context;

    public CustomList(Context context, ArrayList<Expense> expenses) {
        super(context, 0, expenses);
        this.expenses = expenses;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content, parent, false);
        }

        Expense expense = expenses.get(position);
        TextView expenseName = view.findViewById(R.id.expense_name);
        TextView monthlyCost = view.findViewById(R.id.monthly_cost);
        TextView startDate = view.findViewById(R.id.start_date);
        TextView comment = view.findViewById(R.id.comment);


        expenseName.setText(expense.getName());
        monthlyCost.setText(expense.getMonthlyChargeAsString());
        startDate.setText(expense.getStartDate());
        comment.setText(expense.getComment());
        return view;
    }

}
