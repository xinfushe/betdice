package com.aidilude.betdice.po;

import java.math.BigDecimal;

public class Turn {

    private String round;

    private String officialWalletAddress;

    private String officialWalletSecret;

    private Integer totalBetCount;

    private BigDecimal totalBetAmount;

    private Integer partakeCustomerCount;

    private Integer totalWinCount;

    private BigDecimal totalWinAmount;

    private BigDecimal jackpotAmount;

    private String currency;

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getOfficialWalletAddress() {
        return officialWalletAddress;
    }

    public void setOfficialWalletAddress(String officialWalletAddress) { this.officialWalletAddress = officialWalletAddress; }

    public String getOfficialWalletSecret() {
        return officialWalletSecret;
    }

    public void setOfficialWalletSecret(String officialWalletSecret) { this.officialWalletSecret = officialWalletSecret; }

    public Integer getTotalBetCount() {
        return totalBetCount;
    }

    public void setTotalBetCount(Integer totalBetCount) {
        this.totalBetCount = totalBetCount;
    }

    public BigDecimal getTotalBetAmount() {
        return totalBetAmount;
    }

    public void setTotalBetAmount(BigDecimal totalBetAmount) {
        this.totalBetAmount = totalBetAmount;
    }

    public Integer getPartakeCustomerCount() {
        return partakeCustomerCount;
    }

    public void setPartakeCustomerCount(Integer partakeCustomerCount) { this.partakeCustomerCount = partakeCustomerCount; }

    public Integer getTotalWinCount() {
        return totalWinCount;
    }

    public void setTotalWinCount(Integer totalWinCount) {
        this.totalWinCount = totalWinCount;
    }

    public BigDecimal getTotalWinAmount() {
        return totalWinAmount;
    }

    public void setTotalWinAmount(BigDecimal totalWinAmount) {
        this.totalWinAmount = totalWinAmount;
    }

    public BigDecimal getJackpotAmount() {
        return jackpotAmount;
    }

    public void setJackpotAmount(BigDecimal jackpotAmount) {
        this.jackpotAmount = jackpotAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}