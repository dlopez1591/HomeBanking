package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.PayServiceDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.mindhub.homebanking.models.CardType.DEBIT;

@RestController
@RequestMapping("/api")
public class PayServiceController {
    @Autowired
    private CardRepository CardRepo;
    @Autowired
    private ClientRepository ClientRepo;
    @Autowired
    private TransactionRepository TransactionRepo;
    @Autowired
    private AccountRepository AccountRepo;
    @Transactional
    @CrossOrigin //-> permite ejecutar desde cualquier parte de aplicaccion JAVA
    @PostMapping("/payment")
    public ResponseEntity<Object> payCards(
            @RequestBody(required = false) PayServiceDTO payServicesDTO ) {

        Card card = CardRepo.findByNumber(payServicesDTO.getNumber());
        Account account = card.getClient().getAccounts().stream().iterator().next();

        if (payServicesDTO.getNumber().isEmpty()) {
            return new ResponseEntity<>("El numero de tarjeta no fue encontrado", HttpStatus.BAD_REQUEST);
        }
        if (payServicesDTO.getAmount() == null && payServicesDTO.getAmount() > 0) {
            return new ResponseEntity<>("El monto esta vacio", HttpStatus.BAD_REQUEST);
        }
        if (payServicesDTO.getCvv() == 0) {
            return new ResponseEntity<>("El numero de cvv no fue encontrado", HttpStatus.BAD_REQUEST);
        }
        if (payServicesDTO.getDescription().isEmpty()) {
            return new ResponseEntity<>("La descripcion esta vacia", HttpStatus.BAD_REQUEST);
        }
        if (card == null) {
            return new ResponseEntity<>("Tarjeta no encontrada", HttpStatus.BAD_REQUEST);
        }
        if (!card.isHidden()) {
            return new ResponseEntity<>("La trajeta no esta activa", HttpStatus.BAD_REQUEST);
        }
        if (card.getType() != DEBIT) {
            return new ResponseEntity<>("La tarjeta no es de debito", HttpStatus.BAD_REQUEST);
        }
        if (card.getThruDate().isBefore(LocalDate.now())) {
            return new ResponseEntity<>("La tarjeta esta expirada", HttpStatus.BAD_REQUEST);
        }
        if (account == null) {
            return new ResponseEntity<>("La cuenta no se encuentra registrada en nuestra base de datos", HttpStatus.BAD_REQUEST);
        }
        if (!account.getShow()){
            return new ResponseEntity<>("La cuenta no esta activa", HttpStatus.BAD_REQUEST);
        }
        if (account.getBalance()<payServicesDTO.getAmount()) {
            return new ResponseEntity<>("El monto de tu cuenta es menor al pago requerido", HttpStatus.BAD_REQUEST);
        }
        if (!Objects.equals(payServicesDTO.getNumber(), card.getNumber()) ||!Objects.equals(payServicesDTO.getCvv(), card.getCvv())) {
            return new ResponseEntity<>("La informacion es incorrecta, revisa los datos ingresados", HttpStatus.BAD_REQUEST);
        }

        Transaction transaction = new Transaction(TransactionType.DEBIT,payServicesDTO.getAmount(), payServicesDTO.getDescription(), LocalDateTime.now(),account.getBalance() - payServicesDTO.getAmount());
        account.setBalance(account.getBalance() - payServicesDTO.getAmount());
        account.addTransaction(transaction);
        TransactionRepo.save(transaction);
        AccountRepo.save(account);
        return new ResponseEntity<>("Payment made",HttpStatus.ACCEPTED);
    }
}
