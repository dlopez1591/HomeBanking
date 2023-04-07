package com.mindhub.homebanking.dtos;

import java.util.ArrayList;
import java.util.List;

public class LoanCreationDTO {
    private String name;
    private double maxAmount;
    private List<Integer> payments = new ArrayList<>();
    private double interest;
    public String getName() {return name;}
    public double getMaxAmount() {
        return maxAmount;
    }
    public List<Integer> getPayments() {
        return payments;
    }
    public double getInterest() {
        return interest;
    }
}

