package com.sharex.token.api.util;

import com.sharex.token.api.currency.okex.resp.AccountsFree;
import org.junit.Test;

import java.lang.reflect.Field;

public class ReflexUtilTest {

    @Test
    public void getDeclaredFields() {


        Field[] fields = AccountsFree.class.getDeclaredFields();

        for (int i=0; i<fields.length; i++) {
            System.out.println(fields[i].getName());
        }
    }
}
