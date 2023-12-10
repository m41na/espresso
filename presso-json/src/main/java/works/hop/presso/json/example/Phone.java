package works.hop.presso.json.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import works.hop.presso.json.example.mapper.Convertible;

import java.util.Arrays;
import java.util.Properties;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phone implements Convertible {

    PhoneType type;
    String countryCode;
    String areaCode;
    String number;

    @Override
    public String toString() {
        return Arrays.stream(new String[]{countryCode, areaCode, number})
                .filter(val -> val != null && !val.isBlank())
                .reduce("", (acc, curr) -> acc + " " + curr).trim();
    }

    @Override
    public <T> void convert(T target, Properties properties) {
        resolve("type", target, PhoneType.class, properties);
        resolve("countryCode", target, String.class, properties);
        resolve("areaCode", target, String.class, properties);
        resolve("number", target, String.class, properties);
    }
}
