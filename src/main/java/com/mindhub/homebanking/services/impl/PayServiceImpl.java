package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.controllers.PayServiceController;
import com.mindhub.homebanking.dtos.PayServiceDTO;
import com.mindhub.homebanking.services.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PayServiceImpl implements PayService {

    @Autowired
    private PayServiceController payServiceController;

    @Override
    public boolean processPayment(String cardNumber, int cvv, double amount, String description) {
        PayServiceDTO payServiceDTO = new PayServiceDTO(cardNumber, cvv, amount, description);
        payServiceDTO.setNumber(cardNumber);
        payServiceDTO.setCvv(cvv);
        payServiceDTO.setAmount(amount);
        payServiceDTO.setDescription(description);

        ResponseEntity<Object> response = payServiceController.payCards(payServiceDTO);

        return response.getStatusCode() == HttpStatus.ACCEPTED;
    }
}
