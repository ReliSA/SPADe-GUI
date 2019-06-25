package gui;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import ostatni.Column;
import ostatni.ComboBoxItem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class FormCreateQueryTest {
    private FormCreateQuery form;

    @Before
    public void setUp(){
        HashMap<String, List<Column>> map = new HashMap<>();
        map.put("view", Arrays.asList(new Column("age", "number"), new Column("authorName", "text")));

        JSONObject object = new JSONObject();
        object.put("joinColumn", "authorName");
        object.put("agrColumn", "age");
        object.put("aggregate", "AVG");
        object.put("table", "view");
        object.put("columnName", "AverageAge");

        JSONArray conditions = new JSONArray();

        JSONObject conditionObject = new JSONObject();
        conditionObject.put("name", "age");
        conditionObject.put("operator", "<");
        conditionObject.put("value", "20");
        conditionObject.put("type", "number");
        conditions.put(conditionObject);

        object.put("conditions", conditions);

        form = new FormCreateQuery(map, object, Arrays.asList(new ComboBoxItem("jmeno", "text", "name")));
    }

    @Test
    public void getFormData() {
        JSONObject object = form.getFormData();
        assertTrue(object.has("joinColumn"));
        assertTrue(object.has("agrColumn"));
        assertTrue(object.has("aggregate"));
        assertTrue(object.has("table"));
        assertTrue(object.has("conditions"));
    }
}