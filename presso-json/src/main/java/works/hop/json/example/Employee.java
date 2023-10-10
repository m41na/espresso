package works.hop.json.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    String firstName;
    String lastName;
    int age;
    String emailAddress;
    Address address;
    List<Phone> contactInfo;
}
