package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.Utilitis.GenereteNumber;
import static java.util.stream.Collectors.toList;
@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountServices accountServices;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountServices.getAllAccounts();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountServices.getAccountById(id);
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getCurrentAccount(Authentication authentication) {
        return accountServices.getCurrentClientAccounts(authentication);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> newAccount(Authentication authentication,
                                             @RequestParam String accountType) {
        return accountServices.createNewAccount(authentication, accountType);
    }

    @PatchMapping(path = "/clients/current/accounts/delete/{idAccount}")
    public ResponseEntity<Object> deleteAccount(@PathVariable Long idAccount) {
        return accountServices.deleteAccountById(idAccount);
    }

}
