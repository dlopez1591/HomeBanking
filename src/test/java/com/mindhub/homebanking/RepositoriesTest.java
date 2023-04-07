package com.mindhub.homebanking;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {
    @Autowired
    LoanRepository loanRepository;
    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }
    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }
    @Test
    public void existAutomotiveLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Automotive"))));
    }
    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void existAccounts() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, is(not(empty())));
    }

    @Test
    public void findAccountByNumber() {
        Account account = accountRepository.findByNumber("VIN-001");
        assertThat(account.getNumber(), is("123456789"));
    }

    @Test
    public void accountExistsByNumber() {
        Boolean exists = accountRepository.existsAccountByNumber("123456789");
        assertThat(exists, is(true));
    }
    @Autowired
    private CardRepository cardRepository;

    // Verifica que existan tarjetas en la base de datos
    @Test
    public void existCards() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, is(not(empty())));
    }

    // Verifica que exista al menos una tarjeta con el número "123456789"
    @Test
    public void existCardByNumber() {
        Boolean exists = cardRepository.existsCardByNumber("123456789");
        assertThat(exists, is(true));
    }

    // Verifica que no exista una tarjeta con el número "111111111"
    @Test
    public void notExistCardByNumber() {
        Boolean exists = cardRepository.existsCardByNumber("111111111");
        assertThat(exists, is(false));
    }

    @Autowired
    private ClientRepository clientRepository;

    // Verifica que se pueda buscar un cliente por su correo electrónico y se reciba el cliente correcto
    @Test
    public void findClientByEmail() {
        Client client = clientRepository.findByEmail("dlopez1591@gmail.com");
        assertThat(client.getFirstName(), is("Daniel"));
    }

    // Verifica que no exista un cliente con el correo electrónico "nonexisting@example.com"
    @Test
    public void notExistClientByEmail() {
        Client client = clientRepository.findByEmail("nonexisting@example.com");
        assertThat(client, is(nullValue()));
    }

    // Verifica que exista al menos un cliente en la base de datos
    @Test
    public void existClients() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }
    @Autowired
    private TransactionRepository transactionRepository;

    // Verifica que existan transacciones en la base de datos
    @Test
    public void existTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, is(not(empty())));
    }

    // Verifica que no exista una transacción con el id "12345"
    @Test
    public void notExistTransactionById() {
        Optional<Transaction> transaction = transactionRepository.findById(12345L);
        assertThat(transaction, is(Optional.empty()));
    }



}