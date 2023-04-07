package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.Utilitis.GenereteNumber;
import static java.util.stream.Collectors.toList;

@Service
public class AccountServicesImpl implements AccountServices {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @Override
    public AccountDTO getAccountById(Long id) {
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @Override
    public List<AccountDTO> getCurrentClientAccounts(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName()).getAccounts().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @Override
    public ResponseEntity<Object> createNewAccount(Authentication authentication, String accountType) {
        Client client = clientRepository.findByEmail(authentication.getName());
        AccountType accountType1 = AccountType.valueOf(accountType);
        List<Account> AccountListTrue = client.getAccounts().stream().filter(
                account1 -> account1.getShow().toString().equals("true")).collect(Collectors.toList());

        //si tiene mas de tres, no podes compi
        if (AccountListTrue.size()>=3) {
            return new ResponseEntity<>("You cannot have more than three Accounts", HttpStatus.FORBIDDEN);
        }

        //si ta to-do bien creamos la cuenta
        Account newAccount = new Account( GenereteNumber(accountRepository), LocalDateTime.now(),0.0,true, accountType1);
        client.addAccount(newAccount);
        accountRepository.save(newAccount);
        return new ResponseEntity<>("creado con exito",HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> deleteAccountById(Long id) {
        Account account = accountRepository.findById(id).orElse(null);

        if (id.toString().isEmpty()) {
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.BAD_REQUEST);
        }
        if (account.getBalance() > 0){
            return new ResponseEntity<>("No puedes borrar una cuenta si aun tiene dinero en esta cuenta", HttpStatus.BAD_REQUEST);
        }

        account.setShow(false);
        accountRepository.save(account);
        return new ResponseEntity<>("Tu cuenta fue eliminada exitosamente", HttpStatus.ACCEPTED);
    }
}
