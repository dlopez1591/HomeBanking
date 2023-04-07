package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity // Esta anotación indica que esta clase es una entidad de base de datos.
public class Account {

    @Id // Indica que el campo es la clave principal de la tabla.
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native") // Indica cómo se generará el valor de la clave principal.
    @GenericGenerator(name = "native",strategy = "native") // Personaliza la generación de claves principales.
    private long id; // Un identificador único para cada cuenta.
    private String number;  // Número de cuenta.
    private LocalDateTime creationDate; // Fecha de creación de la cuenta.
    private double balance;  // Saldo actual de la cuenta.
    @OneToMany(mappedBy = "account",fetch = FetchType.EAGER)  // Indica que la cuenta tiene muchas transacciones.
    private final Set<Transaction> transactions = new HashSet<>(); // Almacena todas las transacciones de la cuenta.
    @ManyToOne(fetch = FetchType.EAGER) // Indica que muchas cuentas pueden pertenecer a un solo cliente.
    @JoinColumn(name = "client_id") // Especifica el nombre de la columna para la relación entre la cuenta y el cliente.
    private Client client;  // Almacena el cliente propietario de la cuenta.

    private Boolean show;

    private AccountType accountType;  // Tipo de cuenta (ahorro, corriente, etc.).

//constructores
    public Account(){}
    public Account(String number,LocalDateTime creationDate,double balance, Boolean show, AccountType accountType){
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.show= show;
        this.accountType = accountType;
    }
    // Agrega una transacción a la cuenta.
    public void addTransaction(Transaction transaction){
        // Establece la cuenta de la transacción.
        transaction.setAccount(this);
        // Agrega la transacción al conjunto de transacciones de la cuenta.
        transactions.add(transaction);
    }
    public long getId(){
        return id;
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
    public Client getClient(){ return client; }
    public Set <Transaction> getTransactions(){
        return transactions;
    }
    public void setNumber(String number){
        this.number = number;
    }
    public void setCreationDate(LocalDateTime creationDate){
        this.creationDate=creationDate;
    }
    public void setBalance(double balance){
        this.balance = balance;
    }
    public void setClient(Client client){
        this.client = client;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    @Override

    public  String toString(){
        return number+" "+creationDate+" "+balance;
    }
    public void setHidden(boolean show) { this.show = show; }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
