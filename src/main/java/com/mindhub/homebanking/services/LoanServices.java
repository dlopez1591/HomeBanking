package com.mindhub.homebanking.services;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanCreationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.util.List;


public interface LoanServices {
    List<LoanDTO> getLoans();
    ResponseEntity<String> getLoan(Authentication authentication, LoanApplicationDTO loanApplicationDTO);
    ResponseEntity<Object> getLoanAdmin(LoanCreationDTO loanCreationDTO);
}