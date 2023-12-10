package works.hop.presso.json.example.mapper;

import org.junit.jupiter.api.Test;
import works.hop.presso.json.example.Address;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class Address2Test {

    @Test
    void convert_from_Address2_to_Address() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("address-to-address2.properties"));

        Address2 addr2 = new Address2();
        addr2.setStreet_address("616 Highland Terrace");
        addr2.setCity("Madison");
        addr2.setState("WI");
        addr2.setZip_code("53716");

        Address addr = new Address();
        addr2.convert(addr, properties);

        assertThat(addr.getStreetAddress()).isEqualTo("616 Highland Terrace");
    }

    @Test
    void convert_from_Map_to_Address() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("address-to-address2.properties"));

        Map<String, Object> addr2 = new HashMap<>();
        addr2.put("street_address", "616 Highland Terrace");
        addr2.put("city", "Madison");
        addr2.put("state", "WI");
        addr2.put("zip_code", "53716");

        Address addr = new Address();
        addr.from(addr2, properties);

        assertThat(addr.getStreetAddress()).isEqualTo("616 Highland Terrace");
    }
}