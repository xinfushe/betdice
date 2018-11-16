package com.aidilude.betdice.po;

import java.math.BigDecimal;

public class PersonalStatistics {

    private String round;

    private String walletAddress;

    private String currency;

    private Integer betCount;

    private BigDecimal betAmount;

    private Integer winCount;

    private BigDecimal winAmount;

    public PersonalStatistics() {

    }

    public PersonalStatistics(String round, String walletAddress, String currency) {
        this.round = round;
        this.walletAddress = walletAddress;
        this.currency = currency;
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

    public Integer getBetCount() {
        return betCount;
    }

    public void setBetCount(Integer betCount) {
        this.betCount = betCount;
    }

    public BigDecimal getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    public Integer getWinCount() {
        return winCount;
    }

    public void setWinCount(Integer winCount) {
        this.winCount = winCount;
    }

    public BigDecimal getWinAmount() {
        return winAmount;
    }

    public void setWinAmount(BigDecimal winAmount) {
        this.winAmount = winAmount;
    }

}