package com.sharex.token.api.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MapTest {

    @Test
    public void hashMap() {

        Map<String, String> map = new HashMap<>();

        map.put("key1", "val1");

        map.put("key2", "val2");

        map.put("key1", "val3");

        for (Map.Entry<String, String> entry:map.entrySet()) {

            System.out.println("key: " + entry.getKey() + ", val: " + entry.getValue());
        }
    }
}
