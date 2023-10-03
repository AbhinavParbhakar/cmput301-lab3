package com.example.listycity;

public class Expense {
    private String name;
    private double monthlyCharge;

    private String comment;

    private String startDate;



    public Expense(String name, double monthlyCharge,String comment,String startDate) {
        this.name = name;
        this.monthlyCharge = monthlyCharge;
        this.comment = comment;
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public double getMonthlyCharge() {
        return this.monthlyCharge;
    }

    public String getMonthlyChargeAsString(){return "" + this.monthlyCharge;}

    public String getComment(){return this.comment;}

    public String getStartDate(){return this.startDate;}

    public void setName(String name) {
        this.name = name;
    }

    public void setMonthlyCharge(double monthlyCharge) {
        this.monthlyCharge = monthlyCharge;
    }


    public void setStartDate(String startDate){this.startDate = startDate;}

    public void setComment(String comment){this.comment = comment;}
}
