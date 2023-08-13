package com.ksyun.trade.cache;

import java.util.concurrent.ExecutionException;

public interface Cache<K,V> {
    V get(K key) throws ExecutionException;
    void put(K key,V value);
}
