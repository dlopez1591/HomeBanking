package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.controllers.LoanController;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanCreationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.services.LoanServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServicesImpl implements LoanServices {

    private final LoanController loanController;

    @Autowired
    public LoanServicesImpl(LoanController loanController) {
        this.loanController = loanController;
    }

    @Override
    public List<LoanDTO> getLoans() {
        return loanController.getLoans();
    }

    @Override
    public ResponseEntity<String> getLoan(Authentication authentication, LoanApplicationDTO loanApplicationDTO) {
        return loanController.getLoan(authentication, loanApplicationDTO);
    }

    @Override
    public ResponseEntity<Object> getLoanAdmin(LoanCreationDTO loanCreationDTO) {
        return loanController.getLoanAdmin(loanCreationDTO);
    }
}
