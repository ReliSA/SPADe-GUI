package gui;

import gui.utils.PanelDetectionColumnFactory;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ostatni.CustomComboBoxEditor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PanelDetectionColumnTest {
    private Color basicColor = new Color(238,238,238);
    private PanelDetectionColumnFactory factory;
    private PanelDetectionColumn normalPanel;

    @Before
    public void setUp(){
        factory = new PanelDetectionColumnFactory();
        normalPanel = factory.getDetectionEquals();
    }

    @Test
    public void validateInputOK() {
        Assert.assertTrue(normalPanel.validateInput());
    }

    @Test
    public void validateInputWrongInputs() {
        Assert.assertFalse(factory.getWrongInputsPanel().validateInput());
    }

    @Test
    public void detectValuesEquals() {
        PanelDetectionColumn panel = factory.getDetectionEquals();
        List<Boolean> list = panel.detectValues(new ArrayList<>(Arrays.asList(panel)));
        assertThat(list, CoreMatchers.hasItems(false, true, false));
    }

    @Test
    public void detectValuesGreater() {
        PanelDetectionColumn panel = factory.getDetectionGreater();
        List<Boolean> list = panel.detectValues(new ArrayList<>(Arrays.asList(panel)));
        assertThat(list, CoreMatchers.hasItems(true, false, false));
    }

    @Test
    public void detectValuesLesser() {
        PanelDetectionColumn panel = factory.getDetectionLesser();
        List<Boolean> list = panel.detectValues(new ArrayList<>(Arrays.asList(panel)));
        assertThat(list, CoreMatchers.hasItems(false, false, true));
    }

    @Test
    public void detectValuesBetween() {
        PanelDetectionColumn panel = factory.getDetectionBetween();
        List<Boolean> list = panel.detectValues(new ArrayList<>(Arrays.asList(panel)));
        assertThat(list, CoreMatchers.hasItems(false, true, false));
    }

    @Test
    public void detectValuesWithColumnsEquals() {
        PanelDetectionColumn panel = factory.getColumnDetectionEquals();
        List<Boolean> list = panel.detectValues(new ArrayList<>(Arrays.asList(normalPanel, panel)));
        assertThat(list, CoreMatchers.hasItems(true, true, true));
    }

    @Test
    public void detectValuesWithColumnsGreater() {
        PanelDetectionColumn panel = factory.getColumnDetectionGreater();
        List<Boolean> list = panel.detectValues(new ArrayList<>(Arrays.asList(normalPanel, panel)));
        assertThat(list, CoreMatchers.hasItems(false, false, false));
    }

    @Test
    public void detectValuesWithColumnsLesser() {
        PanelDetectionColumn panel = factory.getColumnDetectionLesser();
        List<Boolean> list = panel.detectValues(new ArrayList<>(Arrays.asList(normalPanel, panel)));
        assertThat(list, CoreMatchers.hasItems(false, false, false));
    }

    @Test
    public void detectValuesWithColumnsBetween() {
        PanelDetectionColumn panel = factory.getColumnDetectionBetween();
        List<Boolean> list = panel.detectValues(new ArrayList<>(Arrays.asList(normalPanel, panel)));
        assertThat(list, CoreMatchers.hasItems(false, false, false));
    }

    @Test
    public void arithmeticCountWrongOperator() {
        Assert.assertEquals(normalPanel.arithmeticCount(1.0, "test", 1.0), 1.0, 0.0);
    }

    @Test
    public void arithmeticCountPlus() {
        Assert.assertEquals(normalPanel.arithmeticCount(1.0, "+", 1.0), 2.0, 0.0);
    }

    @Test
    public void arithmeticCountMinus() {
        Assert.assertEquals(normalPanel.arithmeticCount(1.0, "-", 1.0), 0.0, 0.0);
    }

    @Test
    public void arithmeticCountMultiply() {
        Assert.assertEquals(normalPanel.arithmeticCount(2.0, "*", 2.0), 4.0, 0.0);
    }

    @Test
    public void arithmeticCountDivide() {
        Assert.assertEquals(normalPanel.arithmeticCount(2.0, "/", 2.0), 1.0, 0.0);
    }

    @Test
    public void getComboBoxValueItemValue() {
        normalPanel.getCboxVariableValues().setSelectedIndex(0);
        String result = normalPanel.getComboBoxValue(normalPanel.getCboxVariableValues());
        Assert.assertEquals(result, "name");
    }

    @Test
    public void getComboBoxValueTextValue() {
        normalPanel.getCboxVariableValues().getEditor().setItem("test");
        String result = normalPanel.getComboBoxValue(normalPanel.getCboxVariableValues());
        Assert.assertEquals(result, "test");
    }

    @Test
    public void getComboBoxValueNullValue() {
        normalPanel.getCboxVariableValues().getEditor().setItem(null);
        String result = normalPanel.getComboBoxValue(normalPanel.getCboxVariableValues());
        Assert.assertEquals(result, "");
    }

    @Test
    public void setCboxVariableValuesOk() {
        JComboBox jComboBox = new JComboBox();
        jComboBox.setEditor(new CustomComboBoxEditor());
        normalPanel.setCboxVariableValuesWarning(jComboBox);
        normalPanel.setCboxVariableValuesOk(jComboBox);
        Assert.assertTrue(jComboBox.getEditor().getEditorComponent().getBackground().equals(basicColor));
    }

    @Test
    public void setCboxVariableValuesWarning() {
        JComboBox jComboBox = new JComboBox();
        jComboBox.setEditor(new CustomComboBoxEditor());
        normalPanel.setCboxVariableValuesWarning(jComboBox);
        Assert.assertTrue(jComboBox.getEditor().getEditorComponent().getBackground().equals(Color.PINK));
    }
}