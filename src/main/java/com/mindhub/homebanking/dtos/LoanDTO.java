package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class LoanDTO {
    private Long id;
    private String name;
    private Double maxAmount;
    private List<Integer> payments = new ArrayList<>();

    private Double PorcentajeInteres;

    public LoanDTO(){}
    public LoanDTO(Loan loan){
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.PorcentajeInteres= loan.getPorcentajeInteres();

    }

    public Long getId() {
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

    public Double getPorcentajeInteres(){return PorcentajeInteres;}

}