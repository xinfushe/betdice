package com.aidilude.betdice.po;

import java.math.BigDecimal;

public class MiningRecord {

    private String round;

    private String walletAddress;

    private String currency;

    private BigDecimal miningAmount;

    public MiningRecord() {

    }

    public MiningRecord(String round, String walletAddress, String currency, BigDecimal miningAmount) {
        this.round = round;
        this.walletAddress = walletAddress;
        this.currency = currency;
        this.miningAmount = miningAmount;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getMiningAmount() {
        return miningAmount;
    }

    public void setMiningAmount(BigDecimal miningAmount) {
        this.miningAmount = miningAmount;
    }

}