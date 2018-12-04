package com.aidilude.betdice.mapper;

import com.aidilude.betdice.mapper.provider.PledgeRecordProvider;
import com.aidilude.betdice.po.PledgeRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface PledgeRecordMapper {

    @SelectProvider(type = PledgeRecordProvider.class, method = "selectByCondition")
    public List<PledgeRecord> selectByCondition(@Param("id") String id,
                                                @Param("pledgorAccount") String pledgorAccount,
                                                @Param("receivingAccount")String receivingAccount);

    @Insert("insert into pledge_record(id,pledgor_account,receiving_account,amount,transfer_time) " +
            "values(#{id},#{pledgorAccount},#{receivingAccount},#{amount},#{transferTime})")
    public Integer insert(PledgeRecord pledgeRecord);

}