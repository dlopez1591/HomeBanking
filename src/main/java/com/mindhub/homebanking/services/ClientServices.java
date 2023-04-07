package com.mindhub.homebanking.services;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

public interface ClientServices {
    List<ClientDTO> getClients();

    ClientDTO getCurrentClient(String email);

    Optional<ClientDTO> getClient(Long id);

    ResponseEntity<Object> register(String firstName, String lastName, String email, String password);

    List<CardDTO> getClientCards(String email);

}
