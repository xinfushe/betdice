package com.aidilude.betdice.mapper;

import com.aidilude.betdice.po.InviteRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface InviteRecordMapper {

    @Select("select * from invite_record where wallet_address = #{walletAddress}")
    public InviteRecord select(String walletAddress);

    @Insert("insert into invite_record(wallet_address,invite_count,invite_reward_amount,invite_reward_count) " +
            "values(#{walletAddress},#{inviteCount},#{inviteRewardAmount},#{inviteRewardCount})")
    public Integer insert(InviteRecord inviteRecord);

    @Update("update invite_record set invite_count = invite_count + #{inviteCount}," +
            "invite_reward_amount = invite_reward_amount + #{inviteRewardAmount}," +
            "invite_reward_count = invite_reward_count + #{inviteRewardCount} " +
            "where wallet_address = #{walletAddress}")
    public Integer update(InviteRecord inviteRecord);

}