package com.aidilude.betdice.po;

import java.math.BigDecimal;

public class Rank {

    private String round;

    private String walletAddress;

    private String winCurrency;

    private Integer betCount;

    private BigDecimal betAmount;

    private Integer winCount;

    private BigDecimal winAmount;

    private BigDecimal miningAmount;

    private String miningCurrency;

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

    public String getWinCurrency() {
        return winCurrency;
    }

    public void setWinCurrency(String winCurrency) {
        this.winCurrency = winCurrency;
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

    public BigDecimal getMiningAmount() {
        return miningAmount;
    }

    public void setMiningAmount(BigDecimal miningAmount) {
        this.miningAmount = miningAmount;
    }

    public String getMiningCurrency() {
        return miningCurrency;
    }

    public void setMiningCurrency(String miningCurrency) {
        this.miningCurrency = miningCurrency;
    }

}