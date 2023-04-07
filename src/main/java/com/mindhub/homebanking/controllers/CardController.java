package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static com.mindhub.homebanking.utils.Utilitis.randomNumberCard;
import static com.mindhub.homebanking.utils.Utilitis.returnCvvNumber;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
public class CardController {

    private final CardServices cardServices;

    @Autowired
    public CardController(CardServices cardServices) {
        this.cardServices = cardServices;
    }

    @GetMapping("/clients/current/cards")
    public List<CardDTO> getCurrentAccount(Authentication authentication) {
        String email = authentication.getName();
        return cardServices.getCurrentAccount(email);
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> newCard(Authentication authentication,
                                          @RequestParam CardType type,
                                          @RequestParam CardColor color) {
        String email = authentication.getName();
        return cardServices.newCard(email, type, color);
    }

    @PutMapping("/cards/{id}/hide")
    public ResponseEntity<String> hideCard(@PathVariable Long id) {
        return cardServices.hideCard(id);
    }
}
