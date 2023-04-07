package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private Long id;
    private Double amount;
    private Integer payments;
    private String accountDestiny;

    public LoanApplicationDTO(long id, Double amount, Integer payments, String accountDestiny) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.accountDestiny = accountDestiny;
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getAccountDestiny() {
        return accountDestiny;
    }
}
