package com.mindhub.homebanking.services;

public interface PayService {
    boolean processPayment(String cardNumber, int cvv, double amount, String description);
}
