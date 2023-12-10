package works.hop.presso.json.example.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Properties;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address2 implements Convertible {

    String street_address;
    String city;
    String state;
    String zip_code;

    @Override
    public <T> void convert(T target, Properties properties) {
        resolve("street_address", target, String.class, properties);
        resolve("city", target, String.class, properties);
        resolve("state", target, String.class, properties);
        resolve("zip_code", target, String.class, properties);
    }
}
