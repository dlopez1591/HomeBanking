package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.mindhub.homebanking.utils.PdfGenerator;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;
@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> newTransactions(
            Authentication authentication,
            @RequestParam Double amount,
            @RequestParam String description,
            @RequestParam String numAccountOrigin,
            @RequestParam String numAccountDestini) {

        Client clientAuthen = clientRepository.findByEmail(authentication.getName());
        if (amount < 0)
            return new ResponseEntity<>("The amount cannot be negative", HttpStatus.BAD_REQUEST);

        if (amount.isNaN() || amount == 0)
            return new ResponseEntity<>("Missing amount", HttpStatus.BAD_REQUEST);

        if (description.isEmpty())
            return new ResponseEntity<>("Missing description", HttpStatus.BAD_REQUEST);

        if (numAccountOrigin.isEmpty())
            return new ResponseEntity<>("Missing number account Origin", HttpStatus.BAD_REQUEST);

        if (numAccountDestini.isEmpty())
            return new ResponseEntity<>("Missing number account Destini", HttpStatus.BAD_REQUEST);

        if (numAccountDestini.equals(numAccountOrigin))
            return new ResponseEntity<>("The accounts are the same", HttpStatus.BAD_REQUEST);

        if (!accountRepository.existsAccountByNumber(numAccountDestini))
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.BAD_REQUEST);

        if (clientAuthen.getAccounts().stream().noneMatch(account -> account.getNumber().equals(numAccountOrigin)))
            return new ResponseEntity<>("Source account does not exist", HttpStatus.BAD_REQUEST);

        if (accountRepository.findByNumber(numAccountOrigin).getBalance() < amount)
            return new ResponseEntity<>("The amount in the account is not enough", HttpStatus.BAD_REQUEST);

        Account accountOrigin = accountRepository.findByNumber(numAccountOrigin);
        Account accountDestini = accountRepository.findByNumber(numAccountDestini);
        Transaction transactionOrigin = new Transaction(TransactionType.DEBIT, amount, description, LocalDateTime.now(),accountOrigin.getBalance() - amount);
        Transaction transactionDestini = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now(),accountDestini.getBalance() + amount);
        accountOrigin.addTransaction(transactionOrigin);
        accountDestini.addTransaction(transactionDestini);
        accountOrigin.setBalance(accountOrigin.getBalance() - amount);
        accountDestini.setBalance(accountDestini.getBalance() + amount);
        accountRepository.save(accountOrigin);
        accountRepository.save(accountDestini);
        transactionRepository.save(transactionOrigin);
        transactionRepository.save(transactionDestini);
        return new ResponseEntity<>("successful transaction", HttpStatus.ACCEPTED);
    }
    @GetMapping("/api/transactions/account/{account_id}")
    public ResponseEntity<?> getTransactionsByAccountId(
            @PathVariable("account_id") Long accountId,
            @RequestParam(value="start_date", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value="end_date", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            HttpServletResponse response
    ) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (!account.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<TransactionDTO> transactionDTOs;
        if(startDate == null || endDate == null){
            // Obtener todas las transacciones de la cuenta
            transactionDTOs = account.get().getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
        } else {
            // Obtener solo las transacciones de la cuenta dentro del rango de fechas establecido
            List<Transaction> transactions = transactionRepository.findTransactionsBetween(accountId, startDate, endDate);
            for(Transaction transaction : transactions) {
                System.out.println(transaction.getId() + " - " + transaction.getAmount());
            }
            transactionDTOs = transactions.stream().map(TransactionDTO::new).collect(Collectors.toList());
        }

        // Generar archivo PDF y descargarlo en la respuesta
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=transactions.pdf");
        try {
            PdfGenerator.generatePdf("transactions.pdf", transactionDTOs);
            InputStream inputStream = new FileInputStream("transactions.pdf");
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return ResponseEntity.ok(transactionDTOs);
    }

}
