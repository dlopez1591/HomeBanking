package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AccountServices {

    List<AccountDTO> getAllAccounts();

    AccountDTO getAccountById(Long id);

    List<AccountDTO> getCurrentClientAccounts(Authentication authentication);

    ResponseEntity<Object> createNewAccount(Authentication authentication, String accountType);

    ResponseEntity<Object> deleteAccountById(Long id);

}
