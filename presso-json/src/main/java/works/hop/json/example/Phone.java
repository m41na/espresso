package works.hop.json.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phone {

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
}
