package com.example.listycity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.regex.Pattern;

public class AddExpenseFragment extends DialogFragment {
    //Class that handles creating an AlertDialog
    //Handles logic of error handling by creating an error message
    //Passes that error message and error bool to the onOkPressed(), to be implemented via the interface
    //If error state is true, doesn't add data to code, and instead passes error to onOkPressed()
    //sends new message, to be displayed, to onOkPressed()
    //if error state is true, then displays old incoming error message
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
    private boolean errorState = false;

    private String newMessage = "";
    private String previousMessage = "";


    private Expense editableExpense;

    private boolean validateStartDate(String startDate) {
        //Checks via regex if date is in yyyy-mm format
        if (startDate.length() == 0) {
            return false;
        } else {
            return pattern.matcher(startDate).find();
        }
    }

    private boolean validateExpenseName(String expenseName) {
        //checks to make sure that name isn't empty
        if (expenseName.length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validateMonthlyCharge(String monthlyCharge) {
        //checks to make sure the value is a double and is non empty, non negative
        if (monthlyCharge.length() == 0) {
            return false;
        } else {
            try {
                double charge = Double.parseDouble(monthlyCharge);

                if (charge < 0) {
                    return false;
                } else {
                    return true;
                }

            } catch (Exception e) {
                return false;
            }
        }
    }

    private boolean isErrorState(String name, String startDate, String monthlyCharge) {
        //checks each validation step, and then appends the appropriate message
        boolean errorState = false;
        if (!validateExpenseName(name)) {
            newMessage += "Name Required\n";
            errorState = true;
        }
        if (!validateMonthlyCharge(monthlyCharge)) {
            newMessage += "Non-negative value Required\n";
            errorState = true;
        }
        if (!validateStartDate(startDate)) {
            newMessage += "\'YYYY-MM\' Format Required\n";
            errorState = true;
        }
        return errorState;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        //makes sure that mainActivity implements OnFragmentInteractionListener
        //then casts the context to be of type OnFragmentInteractionListener
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "OnFragmentInteractionListener is not implemented");
        }
    }

    public AddExpenseFragment() {
        //Constructor called when it's the first time adding an expense
    }

    public AddExpenseFragment(Expense selectedExpense) {
        //Constructor called when it's time to edit an existing expense
        this.editMode = true;
        this.editableExpense = selectedExpense;

    }

    public AddExpenseFragment(Expense selectedExpense, boolean errorState, String previousMessage)
    {
        //Constructor called when the previous dialog state had an error and error message
        //Passes in the same filled out information as last time so user can change it
        this.editMode = true;
        this.editableExpense = selectedExpense;
        this.errorState = errorState;
        this.previousMessage = previousMessage;
    }


    public interface OnFragmentInteractionListener {
        //creates interface for handling onOKpressed
        void onOKPressed(Expense expense, boolean edit, boolean error, String errorMessage);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //inflate the layout and return the created dialog using builder
        //if the editMode is true, then fill the default text with the passed in info
        //if error message is true, then also display the last dialog state error message
        //
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_expense_fragment_layout, null);
        expenseName = view.findViewById(R.id.expense_name);
        startDate = view.findViewById(R.id.start_date);
        monthlyCharge = view.findViewById(R.id.monthly_cost);
        comment = view.findViewById(R.id.comment);

        if (this.editMode) {
            expenseName.setText(this.editableExpense.getName());
            startDate.setText(this.editableExpense.getStartDate());
            monthlyCharge.setText(this.editableExpense.getMonthlyChargeAsString());
            comment.setText(this.editableExpense.getComment());
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        AlertDialog dialog = builder
                .setView(view)
                .setTitle("Add Or Edit City")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get the values inputted for each field
                        //if there is an error, set error flag to true
                        //pass in information accordingly, based on if it's a new entry or not
                        String tempExpenseName = expenseName.getText().toString();
                        String tempStartDate = startDate.getText().toString();
                        String tempMonthlyCharge = monthlyCharge.getText().toString();
                        String tempComment = comment.getText().toString();

                        if (!isErrorState(tempExpenseName, tempStartDate, tempMonthlyCharge)) {

                            double monthlyChargeDouble = Double.parseDouble(tempMonthlyCharge);


                            if (editMode) {
                                editableExpense.setName(tempExpenseName);
                                editableExpense.setMonthlyCharge(monthlyChargeDouble);
                                editableExpense.setComment(tempComment);
                                editableExpense.setStartDate(tempStartDate);
                                listener.onOKPressed(editableExpense, true, false, "");
                                editMode = false;
                            } else {
                                listener.onOKPressed(new Expense(tempExpenseName, monthlyChargeDouble, tempComment, tempStartDate), false, false, "");
                            }
                        } else {
                            if (editMode) {
                                listener.onOKPressed(editableExpense, true, true, newMessage);
                            } else {
                                try {
                                    listener.onOKPressed(new Expense(tempExpenseName, Double.parseDouble(tempMonthlyCharge), tempComment, tempStartDate), true, true, newMessage);
                                }catch (NumberFormatException e){
                                    listener.onOKPressed(new Expense(tempExpenseName, -1, tempComment, tempStartDate), true, true, newMessage);
                                }
                            }

                        }

                    }
                }).create();

        if (this.errorState) {

            dialog.setMessage(this.previousMessage);
        }


        return dialog;
    }
}
