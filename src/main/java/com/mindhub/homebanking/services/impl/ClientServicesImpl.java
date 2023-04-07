package com.mindhub.homebanking.services.impl;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.Utilitis.GenereteNumber;

@Service
public class ClientServicesImpl implements ClientServices {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;


    // Este método devuelve una lista de ClientDTO que se obtiene de la base de datos
    // utilizando el método findAll() del repositorio ClientRepository
    @Override
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }
    // Este método devuelve un objeto ClientDTO correspondiente al cliente con el correo
    // electrónico proporcionado como parámetro. El objeto se obtiene de la base de datos
    // utilizando el método findByEmail() del repositorio ClientRepository. Si el cliente
    // no es encontrado, se lanza una excepción.
    @Override
    public ClientDTO getCurrentClient(String email) {
        Client client = clientRepository.findByEmail(email);
        if (client == null) {
            throw new RuntimeException("Client not found");
        }
        return new ClientDTO(client);
    }
    // Este método devuelve un objeto Optional<ClientDTO> correspondiente al cliente con el id
    // proporcionado como parámetro. El objeto se obtiene de la base de datos utilizando el método
    // findById() del repositorio ClientRepository. Si el cliente no es encontrado, se devuelve
    // un objeto Optional vacío.
    @Override
    public Optional<ClientDTO> getClient(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.map(ClientDTO::new);
    }
    // Este método registra un nuevo cliente en la base de datos. Si alguno de los parámetros
    // es nulo o vacío, se devuelve un objeto ResponseEntity con un mensaje de error y un
    // código HTTP correspondiente. Si el correo electrónico proporcionado ya se encuentra
    // en uso por otro cliente, también se devuelve un objeto ResponseEntity con un mensaje
    // de error y un código HTTP correspondiente. Si no hay errores, se crea un nuevo objeto
    // Account y un nuevo objeto Client, se añade la cuenta al cliente, se codifica la contraseña
    // del cliente utilizando el PasswordEncoder y se guardan ambos objetos en la base de datos.
    // Finalmente, se devuelve un objeto ResponseEntity con un código HTTP correspondiente.
    @Override
    public ResponseEntity<Object> register(String firstName, String lastName, String email, String password) {
        if (firstName.isEmpty())
            return new ResponseEntity<>("Missing firstname", HttpStatus.BAD_REQUEST);
        else if (lastName.isEmpty())
            return new ResponseEntity<>("Missing lastname", HttpStatus.BAD_REQUEST);
        else if (email.isEmpty())
            return new ResponseEntity<>("Missing email", HttpStatus.BAD_REQUEST);
        else if (password.isEmpty())
            return new ResponseEntity<>("Missing password", HttpStatus.BAD_REQUEST);
        if (clientRepository.findByEmail(email) != null)
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        Account newAccount = new Account(GenereteNumber(accountRepository), LocalDateTime.now(), 0, false, AccountType.CORRIENTE);
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        newClient.addAccount(newAccount);
        clientRepository.save(newClient);
        accountRepository.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public List<CardDTO> getClientCards(String email) {
// Buscar al cliente a partir de su correo electrónico en el repositorio.
        Client clientAutenticado = clientRepository.findByEmail(email);
// Si el cliente no es encontrado, lanzar una excepción de tiempo de ejecución.
        if (clientAutenticado == null) {
            throw new RuntimeException("Client not found");
        }
// Recuperar todas las tarjetas del cliente autenticado y transformarlas en una lista de objetos CardDTO.
        return clientAutenticado.getCards().stream().map(CardDTO::new).collect(Collectors.toList());
    }
}
