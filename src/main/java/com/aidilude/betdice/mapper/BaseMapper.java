package com.aidilude.betdice.mapper;

import com.aidilude.betdice.mapper.provider.BaseProvider;
import com.aidilude.betdice.mapper.provider.MiningRecordProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

public interface BaseMapper {

    @UpdateProvider(type = BaseProvider.class, method = "renameTable")
    public Integer renameTable(@Param("tableName") String tableName, @Param("suffix") String suffix);

}
