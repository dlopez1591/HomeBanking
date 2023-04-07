package com.mindhub.homebanking.repositories;
import java.util.Collection;
import java.util.List;
import java.time.LocalDateTime;

import com.mindhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId AND t.date >= :startDate AND t.date <= :endDate")
    List<Transaction> findTransactionsBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);

    // Método para imprimir los resultados
    default void printTransactions(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = this.findTransactionsBetween(accountId, startDate, endDate);
        System.out.println("Cantidad de transacciones encontradas: " + transactions.size());
        for (Transaction transaction : transactions) {
            System.out.println(transaction.getId() + " - " + transaction.getAmount());
        }
    }

}
