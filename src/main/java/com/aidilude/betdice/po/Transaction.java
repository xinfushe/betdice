package com.aidilude.betdice.po;

public class Transaction {

    private String tid;

    private Integer timestamp;

    private String round;

    private String owner;

    private Integer number;

    private Integer numberWin;

    private String amount;

    private String amountWin;

    private String currency;

    private String mining;

    private String miningCurrency;

    private String inviterWallet;

    private String inviterGetAmount;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumberWin() {
        return numberWin;
    }

    public void setNumberWin(Integer numberWin) {
        this.numberWin = numberWin;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountWin() {
        return amountWin;
    }

    public void setAmountWin(String amountWin) {
        this.amountWin = amountWin;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMining() {
        return mining;
    }

    public void setMining(String mining) {
        this.mining = mining;
    }

    public String getMiningCurrency() {
        return miningCurrency;
    }

    public void setMiningCurrency(String miningCurrency) {
        this.miningCurrency = miningCurrency;
    }

    public String getInviterWallet() {
        return inviterWallet;
    }

    public void setInviterWallet(String inviterWallet) {
        this.inviterWallet = inviterWallet;
    }

    public String getInviterGetAmount() {
        return inviterGetAmount;
    }

    public void setInviterGetAmount(String inviterGetAmount) {
        this.inviterGetAmount = inviterGetAmount;
    }

}