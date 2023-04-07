package com.mindhub.homebanking.services;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import org.springframework.http.ResponseEntity;
import java.util.List;
public interface CardServices {

    List<CardDTO> getCurrentAccount(String email);

    ResponseEntity<Object> newCard(String email, CardType type, CardColor color);

    ResponseEntity<String> hideCard(Long id);

}
