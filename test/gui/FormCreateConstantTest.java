package gui;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FormCreateConstantTest {
    private FormCreateConstant form;

    @Before
    public void setUp() throws Exception {
        form = new FormCreateConstant("ConstName", "ConstValue");
    }

    @Test
    public void getConstName() {
        assertEquals("ConstName", form.getConstName());
    }

    @Test
    public void getConstValue() {
        assertEquals("ConstValue", form.getConstValue());
    }
}