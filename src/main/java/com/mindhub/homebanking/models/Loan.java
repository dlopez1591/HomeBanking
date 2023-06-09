package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private long id;
    private String name;
    private Double maxAmount;

    private Double PorcentajeInteres;
    @ElementCollection
    @Column (name = "payment")
    private List<Integer> payments = new ArrayList<>();
    @OneToMany(mappedBy = "loan",fetch = FetchType.EAGER)
    private final Set<ClientLoan> clientLoans = new HashSet<>();
    public Loan(){}
    public Loan(String name, Double maxAmount, List<Integer> payments, double porcentajeInteres) {
        this.name = name;

        this.maxAmount = maxAmount;

        this.payments = payments;

        this.PorcentajeInteres = porcentajeInteres;
    }
    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }
    public List<Client> getClients() {
        return clientLoans.stream().map(clientLoan -> clientLoan.getClient()).collect(toList());
    }
    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Double getMaxAmount() {
        return maxAmount;
    }
    public List<Integer> getPayments() {
        return payments;
    }
    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }
    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Double getPorcentajeInteres() {
        return PorcentajeInteres;
    }

    public void setPorcentajeInteres(Double porcentajeInteres) {
        PorcentajeInteres = porcentajeInteres;
    }
}
