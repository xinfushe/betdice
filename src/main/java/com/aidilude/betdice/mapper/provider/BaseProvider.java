package com.aidilude.betdice.mapper.provider;

import org.apache.ibatis.annotations.Param;

public class BaseProvider {

    public String renameTable(@Param("tableName") String tableName, @Param("suffix") String suffix){
        return "rename table " + tableName + " to " + tableName + suffix;
    }

}