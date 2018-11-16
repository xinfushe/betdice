package com.aidilude.betdice.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "system.api")
@PropertySource("classpath:property/system.properties")
public class ApiProperties {

    private String chainRequestProtocol;

    private String chainIP;

    private String chainPort;

    private String shareBonusCurrency;

    private String HoldCurrency;

    private String rankWinCurrency;

    private String rankMiningCurrency;

    private Integer rankOffset;

    private String currencys;

    private String preffix;

    private String queryHoldAmount;

    private String queryOfficialAmount;

    private String newWalletAddress;

    private String transfer;

    private Integer transferType;

    private String transferFee;

    private String minHoldAmount;

    public String getChainRequestProtocol() {
        return chainRequestProtocol;
    }

    public void setChainRequestProtocol(String chainRequestProtocol) { this.chainRequestProtocol = chainRequestProtocol; }

    public String getChainIP() {
        return chainIP;
    }

    public void setChainIP(String chainIP) {
        this.chainIP = chainIP;
    }

    public String getChainPort() {
        return chainPort;
    }

    public void setChainPort(String chainPort) {
        this.chainPort = chainPort;
    }

    public String getShareBonusCurrency() {
        return shareBonusCurrency;
    }

    public void setShareBonusCurrency(String shareBonusCurrency) {
        this.shareBonusCurrency = shareBonusCurrency;
    }

    public String getHoldCurrency() {
        return HoldCurrency;
    }

    public void setHoldCurrency(String holdCurrency) {
        HoldCurrency = holdCurrency;
    }

    public String getRankWinCurrency() { return rankWinCurrency; }

    public void setRankWinCurrency(String rankWinCurrency) { this.rankWinCurrency = rankWinCurrency; }

    public String getRankMiningCurrency() { return rankMiningCurrency; }

    public void setRankMiningCurrency(String rankMiningCurrency) { this.rankMiningCurrency = rankMiningCurrency; }

    public Integer getRankOffset() { return rankOffset; }

    public void setRankOffset(Integer rankOffset) { this.rankOffset = rankOffset; }

    public String getCurrencys() {
        return currencys;
    }

    public void setCurrencys(String currencys) {
        this.currencys = currencys;
    }

    public String getPreffix() {
        return preffix;
    }

    public void setPreffix(String preffix) {
        this.preffix = preffix;
    }

    public String getQueryHoldAmount() {
        return queryHoldAmount;
    }

    public void setQueryHoldAmount(String queryHoldAmount) {
        this.queryHoldAmount = queryHoldAmount;
    }

    public String getQueryOfficialAmount() {
        return queryOfficialAmount;
    }

    public void setQueryOfficialAmount(String queryOfficialAmount) {
        this.queryOfficialAmount = queryOfficialAmount;
    }

    public String getNewWalletAddress() {
        return newWalletAddress;
    }

    public void setNewWalletAddress(String newWalletAddress) {
        this.newWalletAddress = newWalletAddress;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public Integer getTransferType() {
        return transferType;
    }

    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
    }

    public String getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(String transferFee) {
        this.transferFee = transferFee;
    }

    public String getMinHoldAmount() {
        return minHoldAmount;
    }

    public void setMinHoldAmount(String minHoldAmount) {
        this.minHoldAmount = minHoldAmount;
    }

}