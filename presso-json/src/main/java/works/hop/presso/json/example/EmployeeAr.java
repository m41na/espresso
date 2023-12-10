package works.hop.presso.json.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAr {

    String firstName;
    String lastName;
    int age;
    String emailAddress;
    Address address;
    Phone[] contactInfo;
}
