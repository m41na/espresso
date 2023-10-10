package works.hop.json.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeKv {

    String firstName;
    String lastName;
    int age;
    String emailAddress;
    Address address;
    Map<String, Phone> contactInfo;
}
