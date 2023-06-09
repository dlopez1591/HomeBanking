package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

public class AccountDTO {
    private final long id;
    private final String number;
    private final LocalDateTime creationDate;
    private final double balance;

    private final Set <TransactionDTO> transaction;

    private Boolean show;
    private AccountType accountType;
    public AccountDTO(Account account) {

        this.id = account.getId();

        this.number = account.getNumber();

        this.creationDate = account.getCreationDate();

        this.balance = account.getBalance();

        this.transaction = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(toSet());

        this.show = account.getShow();
        this.accountType = account.getAccountType();
    }
    public String getNumber(){
        return number;
    }
    public LocalDateTime getCreationDate(){
        return creationDate;
    }
    public double getBalance(){
        return balance;
    }
    public long getId(){
        return id;
    }
    public Set<TransactionDTO> getTransaction() {
        return transaction;
    }
    public Boolean getShow (){return show;}
    public void setShow(Boolean show) {
        this.show = show;
    }
    public void setAccountType (AccountType accountType){
        this.accountType = accountType;
    }
    public AccountType getAccountType (){
        return accountType;
    }
}