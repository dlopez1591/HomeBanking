package com.mindhub.homebanking.services.impl;

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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.Utilitis.randomNumberCard;
import static com.mindhub.homebanking.utils.Utilitis.returnCvvNumber;

@Service
@Transactional
public class CardServicesImpl implements CardServices {

    @Autowired
    public CardRepository cardRepository;
    @Autowired
    public ClientRepository clientRepository;

    @Override
    public List<CardDTO> getCurrentAccount(String email) {
        return clientRepository.findByEmail(email).getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<Object> newCard(String email, CardType type, CardColor color) {
        Set<Card> cards = clientRepository.findByEmail(email).getCards().stream().filter(card -> card.getType() == type).collect(Collectors.toSet());
        if (cards.size() >= 3) {
            return new ResponseEntity<>("You have already reached the limit of 3 " + type + " cards, you cannot be given another one", HttpStatus.BAD_REQUEST);
        }
        if (clientRepository.findByEmail(email).getCards().stream().anyMatch(card -> card.getColor() == color && card.getType() == type)) {
            return new ResponseEntity<>("you have already the same card", HttpStatus.BAD_REQUEST);
        }
        Card card = new Card(clientRepository.findByEmail(email), type, color, randomNumberCard(cardRepository), returnCvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5));
        clientRepository.findByEmail(email).addCard(card);
        cardRepository.save(card);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> hideCard(Long id) {
        Optional<Card> optionalCard = cardRepository.findById(id);
        if (!optionalCard.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Card card = optionalCard.get();
        if (card.isHidden()) {
            return ResponseEntity.badRequest().body("Card is already hidden");
        }
        card.setHidden(true);
        cardRepository.save(card);
        return ResponseEntity.ok().body("Card successfully hidden");
    }
}
