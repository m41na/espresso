package works.hop.presso.json.example;

import org.junit.jupiter.api.Test;
import works.hop.presso.json.example.mapper.Address2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class AddressTest {

    @Test
    void convert_from_Address_to_Address2() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("address2-to-address.properties"));

        Address addr = new Address();
        addr.setStreetAddress("616 Highland Terrace");
        addr.setCity("Madison");
        addr.setState("WI");
        addr.setPostalCode("53716");

        Address2 addr2 = new Address2();
        addr.convert(addr2, properties);

        assertThat(addr2.getStreet_address()).isEqualTo("616 Highland Terrace");
    }

    @Test
    void convert_from_Map_to_Address2() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("address2-to-address.properties"));

        Map<String, Object> addr = new HashMap<>();
        addr.put("streetAddress", "616 Highland Terrace");
        addr.put("city", "Madison");
        addr.put("state", "WI");
        addr.put("postalCode", "53716");

        Address2 addr2 = new Address2();
        addr2.from(addr, properties);

        assertThat(addr2.getStreet_address()).isEqualTo("616 Highland Terrace");
    }
}