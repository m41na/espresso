package works.hop.json.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    String streetAddress;
    String city;
    String state;
    String postalCode;
}
