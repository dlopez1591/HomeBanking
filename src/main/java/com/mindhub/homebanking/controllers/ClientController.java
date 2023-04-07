package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.mindhub.homebanking.utils.Utilitis.GenereteNumber;
import static java.util.stream.Collectors.toList;
@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientServices clientServices;

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientServices.getClients();
    }

    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        return clientServices.getCurrentClient(authentication.getName());
    }

    @GetMapping("/clients/{id}")
    public Optional<ClientDTO> getClient(@PathVariable Long id){
        return clientServices.getClient(id);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password) {
        return clientServices.register(firstName, lastName, email, password);
    }

    @GetMapping("/cardDTO")
    public List<CardDTO> cardclientDTO(Authentication authentication) {
        return clientServices.getClientCards(authentication.getName());
    }
}