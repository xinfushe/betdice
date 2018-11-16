package com.aidilude.betdice.po;

import java.math.BigDecimal;

public class InviteRecord {

    private String walletAddress;

    private Integer inviteCount;

    private BigDecimal inviteRewardAmount;

    private Integer inviteRewardCount;

    public InviteRecord() {

    }

    public InviteRecord(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public Integer getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(Integer inviteCount) {
        this.inviteCount = inviteCount;
    }

    public BigDecimal getInviteRewardAmount() {
        return inviteRewardAmount;
    }

    public void setInviteRewardAmount(BigDecimal inviteRewardAmount) {
        this.inviteRewardAmount = inviteRewardAmount;
    }

    public Integer getInviteRewardCount() {
        return inviteRewardCount;
    }

    public void setInviteRewardCount(Integer inviteRewardCount) {
        this.inviteRewardCount = inviteRewardCount;
    }

}