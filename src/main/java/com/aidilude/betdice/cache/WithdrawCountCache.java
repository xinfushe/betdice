package com.aidilude.betdice.cache;

import java.util.concurrent.ConcurrentHashMap;

public class WithdrawCountCache {

    private static ConcurrentHashMap<String, Integer> cache;

    public static void init(){
        System.out.println("**********初始化提现次数缓存**********");

        cache = new ConcurrentHashMap<>();

        System.out.println("**********初始化完成**********");
    }

    public static Integer get(String walletAddress){
        return cache.get(walletAddress);
    }

    public static void put(String walletAddress, Integer withdrawCount){
        cache.put(walletAddress, withdrawCount);
    }

    public static ConcurrentHashMap<String, Integer> getCache(){
        return cache;
    }

    public static void clear(){
        cache.clear();
    }

    public static void main(String[] args) {
        Integer count = 0;
        System.out.println(++count);
        System.out.println(count);
    }

}