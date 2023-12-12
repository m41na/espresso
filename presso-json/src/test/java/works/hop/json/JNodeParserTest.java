package works.hop.json;

import org.junit.jupiter.api.Test;
import works.hop.presso.json.JNodeParser;
import works.hop.presso.json.NodeValue;
import works.hop.presso.json.example.*;

import java.util.ArrayList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JNodeParserTest {

    @Test
    void parse_sample_json() {
        NodeValue<?> node = new JNodeParser().parse("employee.json");
        assertThat(((JNodeParser.JObject) node.getValue()).get("firstName").getValue()).isEqualTo("John");
        assertThat(((JNodeParser.JObject) node.getValue()).get("lastName").getValue()).isEqualTo("Smith");
    }

    @Test
    void parse_json_into_entity_of_object_type() {
        Employee emp = new JNodeParser().parse("employee.json", Employee.class, "employee-config.properties");
        assertThat(emp.getFirstName()).isEqualTo("John");
        assertThat(emp.getLastName()).isEqualTo("Smith");
    }

    @Test
    void parse_json_into_entity_of_list_type() {
        ContactsList contactsList = new JNodeParser().parse("contacts-list.json", ContactsList.class, "contacts-config-list.properties");
        assertThat(contactsList.get(0).getType()).isEqualTo(PhoneType.HOME);
        assertThat(contactsList.get(1).getNumber()).isEqualTo("212 555-1235");
    }

    @Test
    void parse_json_into_entity_of_map_type() {
        ContactsMap contactsList = new JNodeParser().parse("contacts-map.json", ContactsMap.class, "contacts-config-map.properties");
        assertThat(contactsList.get("HOME").getType()).isEqualTo(PhoneType.HOME);
        assertThat(contactsList.get("CELL").getNumber()).isEqualTo("212 555-1235");
    }

    @Test
    void parse_json_having_property_of_user_specific_list_type() {
        Employee emp = new JNodeParser().parse("employee.json", Employee.class, "employee_array_list-config.properties");
        assertThat(emp.getContactInfo().getClass()).isEqualTo(ArrayList.class);
        assertThat(emp.getFirstName()).isEqualTo("John");
        assertThat(emp.getLastName()).isEqualTo("Smith");
    }

    @Test
    void parse_json_having_property_of_array_type() {
        EmployeeAr emp = new JNodeParser().parse("employee.json", EmployeeAr.class, "employee_fixed_array-config.properties");
        assertThat(emp.getContactInfo().getClass()).isEqualTo(Phone[].class);
        assertThat(emp.getFirstName()).isEqualTo("John");
        assertThat(emp.getLastName()).isEqualTo("Smith");
        assertThat(emp.getContactInfo()[0].getType()).isEqualTo(PhoneType.HOME);
        assertThat(emp.getContactInfo()[1].getNumber()).isEqualTo("212 555-1235");
    }

    @Test
    void parse_json_having_property_of_map_type() {
        EmployeeKv emp = new JNodeParser().parse("employee_kv.json", EmployeeKv.class, "employee_key_values-config.properties");
        assertThat(emp.getContactInfo()).isInstanceOf(Map.class);
        assertThat(emp.getFirstName()).isEqualTo("John");
        assertThat(emp.getLastName()).isEqualTo("Smith");
        assertThat(emp.getContactInfo().get("HOME").getType()).isEqualTo(PhoneType.HOME);
        assertThat(emp.getContactInfo().get("CELL").getNumber()).isEqualTo("212 555-1235");
    }
}