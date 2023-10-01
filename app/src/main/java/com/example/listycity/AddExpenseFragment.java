package com.example.listycity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class AddExpenseFragment extends DialogFragment {
    private EditText expenseName;
    private EditText startDate;

    private EditText monthlyCharge;

    private EditText comment;
    private OnFragmentInteractionListener listener;

    //https://www.w3schools.com/java/java_regex.asp
    //Resource used for finding the API reference for Regex in Java
    private String dataRegex = "[0-9]{4}-[0-1][0-9]";
    private Pattern pattern = Pattern.compile(dataRegex);

    private boolean editMode = false;
    private Expense editableExpense;

    private boolean validateStartDate(String startDate) {
        System.out.println("Checked startDate");
        if (startDate.length() == 0) {
            System.out.println("startDate invalid");
            return false;
        } else {
            return pattern.matcher(startDate).find();
        }
    }

    private boolean validateExpenseName(String expenseName) {
        System.out.println("Checked expenseName");
        if (expenseName.length() == 0) {
            System.out.println("expenseName invalid");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateMonthlyCharge(String monthlyCharge) {
        System.out.println("Checked monthlyCharge");
        if (monthlyCharge.length() == 0) {
            System.out.println("monthlyCharge Invalid");
            return false;
        } else if (Double.parseDouble(monthlyCharge) < 0) {
            return false;
        } else {
            return true;
        }
    }
    private boolean isErrorState(ArrayList<EditText> inputFields) {
        boolean errorState = false;

        for (EditText input : inputFields) {
            if (input.getError() != null) {
                return true;
            }
        }

        return errorState;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "OnFragmentInteractionListener is not implemented");
        }
    }

    public AddExpenseFragment() {
    }

    public AddExpenseFragment(Expense selectedExpense) {
        this.editMode = true;
        this.editableExpense = selectedExpense;

    }

    public interface OnFragmentInteractionListener {
        void onOKPressed(Expense expense, boolean edit);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_expense_fragment_layout, null);
        ArrayList<EditText> inputFields = new ArrayList<>();
        expenseName = view.findViewById(R.id.expense_name);
        inputFields.add(expenseName);
        startDate = view.findViewById(R.id.start_date);
        inputFields.add(startDate);
        monthlyCharge = view.findViewById(R.id.monthly_cost);
        inputFields.add(monthlyCharge);
        comment = view.findViewById(R.id.comment);
        inputFields.add(comment);

        if (this.editMode) {
            expenseName.setText(this.editableExpense.getName());
            startDate.setText(this.editableExpense.getStartDate());
            monthlyCharge.setText(this.editableExpense.getMonthlyChargeAsString());
            comment.setText(this.editableExpense.getComment());
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Add Or Edit City")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tempExpenseName = expenseName.getText().toString();
                        String tempStartDate = startDate.getText().toString();
                        String tempMonthlyCharge = monthlyCharge.getText().toString();
                        String tempComment = comment.getText().toString();

                        if (!validateExpenseName(tempExpenseName)) {
                            System.out.println("caused an error");
                            expenseName.setError("Expense Name Required");
                        } else {
                            expenseName.setError(null);
                        }

                        if (!validateStartDate(tempStartDate)) {
                            System.out.println("caused an error");
                            startDate.setError("\'YYYY-MM\' Format Required");
                        } else {
                            startDate.setError(null);
                        }

                        if (!validateMonthlyCharge(tempMonthlyCharge)) {
                            System.out.println("caused an error");
                            monthlyCharge.setError("Non-negative value Required");
                        } else {
                            monthlyCharge.setError(null);
                        }

                        if (isErrorState(inputFields) == false) {

                            double monthlyChargeDouble = Double.parseDouble(tempMonthlyCharge);

                            if (editMode) {
                                editableExpense.setName(tempExpenseName);
                                editableExpense.setMonthlyCharge(monthlyChargeDouble);
                                editableExpense.setComment(tempComment);
                                editableExpense.setStartDate(tempStartDate);
                                listener.onOKPressed(editableExpense, true);
                                editMode = false;
                            } else {
                                listener.onOKPressed(new Expense(tempExpenseName, monthlyChargeDouble, tempComment, tempStartDate), false);
                            }
                        }

                    }
                }).create();
    }
}
