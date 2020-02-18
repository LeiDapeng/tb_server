package com.pay.entity;

import java.math.BigDecimal;

public class BalanceInfo {
    private String cardNo;

    private String accountNo;

    private BigDecimal balance;

    public BalanceInfo(String cardNo, String accountNo, BigDecimal balance) {
        this.cardNo = cardNo;
        this.accountNo = accountNo;
        this.balance = balance;
    }

    public BalanceInfo() {
        super();
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo == null ? null : accountNo.trim();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}