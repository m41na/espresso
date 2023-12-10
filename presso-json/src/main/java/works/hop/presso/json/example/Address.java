package works.hop.presso.json.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import works.hop.presso.json.example.mapper.Convertible;

import java.util.Properties;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Convertible {

    String streetAddress;
    String city;
    String state;
    String postalCode;

    @Override
    public <T> void convert(T target, Properties properties) {
        resolve("streetAddress", target, String.class, properties);
        resolve("city", target, String.class, properties);
        resolve("state", target, String.class, properties);
        resolve("postalCode", target, String.class, properties);
    }
}
