package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanCreationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class LoanController {

    private final ClientRepository clientRepo;
    private final AccountRepository accountRepo;
    private final TransactionRepository transactionRepo;
    private final ClientLoanRepository clientLoanRepo;
    private final LoanRepository loanRepo;

    @Autowired
    public LoanController(ClientRepository clientRepo, LoanRepository loanRepo,
                          AccountRepository accountRepo, ClientLoanRepository clientLoanRepo,
                          TransactionRepository transactionRepo) {
        this.clientRepo = clientRepo;
        this.loanRepo = loanRepo;
        this.accountRepo = accountRepo;
        this.clientLoanRepo = clientLoanRepo;
        this.transactionRepo = transactionRepo;
    }

    @GetMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanRepo.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/loans")
    @Transactional
    public ResponseEntity<String> getLoan(Authentication authentication,
                                          @RequestBody(required = false) LoanApplicationDTO loanApplicationDTO) {

        Client client = clientRepo.findByEmail(authentication.getName());
        if (client == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.CONFLICT);
        }

        if (loanApplicationDTO == null) {
            return new ResponseEntity<>("La solicitud de préstamo está vacía", HttpStatus.CONFLICT);
        }

        Loan loan = loanRepo.findById(loanApplicationDTO.getId()).orElse(null);
        if (loan == null) {
            return new ResponseEntity<>("Tipo de préstamo no encontrado", HttpStatus.CONFLICT);
        }

        Account account = accountRepo.findByNumber(loanApplicationDTO.getAccountDestiny());
        if (account == null || !client.getAccounts().contains(account)) {
            return new ResponseEntity<>("La cuenta de destino no está disponible", HttpStatus.CONFLICT);
        }

        if (loanApplicationDTO.getAmount() == null || loanApplicationDTO.getAmount() <= 0) {
            return new ResponseEntity<>("El monto debe ser mayor que cero", HttpStatus.CONFLICT);
        }

        if (loanApplicationDTO.getPayments() == null || loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("El número de pagos debe ser mayor que cero", HttpStatus.CONFLICT);
        }

        if (loanApplicationDTO.getAmount() > loanRepo.getReferenceById(loanApplicationDTO.getId()).getMaxAmount()) {
            return new ResponseEntity<>("El monto excede el límite del préstamo", HttpStatus.CONFLICT);
        }

        if (!loanRepo.getReferenceById(loanApplicationDTO.getId()).getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("Número de pagos no disponible para este préstamo", HttpStatus.CONFLICT);
        }

        int totalAmount = (int) (loanApplicationDTO.getAmount() * loan.getPorcentajeInteres());
        ClientLoan clientLoan = new ClientLoan(totalAmount, loanApplicationDTO.getPayments(), client, loan);
        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(),
                loan.getName() + " Préstamo aprobado", LocalDateTime.now(),
                account.getBalance() + loanApplicationDTO.getAmount());

        account.addTransaction(transaction);
        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
        client.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);

        accountRepo.save(account);
        clientLoanRepo.save(clientLoan);
        transactionRepo.save(transaction);
        clientRepo.save(client);
        loanRepo.save(loan);

        return new ResponseEntity<>("¡Tu préstamo fue exitoso!", HttpStatus.CREATED);
    }

    @PostMapping("/loans/admin")
    public ResponseEntity<Object> getLoanAdmin(
            @RequestBody LoanCreationDTO loanCreationDTO)
    {
        Loan loan = new Loan(loanCreationDTO.getName(), loanCreationDTO.getMaxAmount() ,loanCreationDTO.getPayments(), loanCreationDTO.getInterest());
        loanRepo.save(loan);
        return new ResponseEntity<>("¡Tu prestamo fue exitoso!",HttpStatus.CREATED);
    }
}

