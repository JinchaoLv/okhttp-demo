package com.lvjc.util;

import com.google.gson.reflect.TypeToken;
import com.lvjc.dto.RemoteApiResult;

import java.util.HashMap;

/**
 * Created by lvjc on 2017/8/1.
 */
public class MapUtil {

    public static <K, V> HashMap<K, V> newHashMap(K key, V value){
        if(key == null)
            return null;
        HashMap<K, V> map = new HashMap<>(1);
        map.put(key, value);
        return map;
    }

    public static <K, V> HashMap<K, V> newHashMap(K[] keys, V[] values){
        if(keys == null || keys.length == 0)
            return null;
        HashMap<K, V> map = new HashMap<>(keys.length);
        for(int i = 0; i < keys.length; ++i){
            if(i < values.length)
                map.put(keys[i], values[i]);
            else
                map.put(keys[i], null);
        }
        return map;
    }

}
