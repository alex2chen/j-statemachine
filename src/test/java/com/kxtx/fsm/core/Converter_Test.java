package com.kxtx.fsm.core;

import com.kxtx.fsm.core.convert.Converter;
import com.kxtx.fsm.core.convert.support.ConverterImpl;
import org.junit.Test;

import java.util.Date;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/9
 */
public class Converter_Test {
    private Converter converter;

    public enum OrderState {
        INIT,
        CREATE
    }

    public enum OrderEvent {
        POSTORDER,
        ALIPAY
    }

    @Test
    public void go_objToString() {
        Converter converter = new ConverterImpl(OrderEvent.class);
        System.out.println(converter.objToString(OrderEvent.ALIPAY));

        converter = new ConverterImpl(String.class);
        System.out.println(converter.objToString("Sss"));
    }

    @Test
    public void go_stringToObj() {
        converter = new ConverterImpl(OrderState.class);
        System.out.println(converter.stringToObj("CREATE"));

        converter = new ConverterImpl(String.class);
        System.out.println(converter.stringToObj(new Date().toString()));
    }
}
