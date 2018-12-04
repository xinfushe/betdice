package com.aidilude.betdice.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InviteRecord {

    private String walletAddress;

    private Integer inviteCount;

    private BigDecimal inviteRewardAmount;

    private Integer inviteRewardCount;

    public InviteRecord(String walletAddress) {
        this.walletAddress = walletAddress;
    }

}