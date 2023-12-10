package works.hop.presso.json.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import works.hop.presso.json.example.mapper.Convertible;

import java.util.List;
import java.util.Properties;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Convertible {

    String firstName;
    String lastName;
    int age;
    String emailAddress;
    Address address;
    List<Phone> contactInfo;

    @Override
    public <T> void convert(T target, Properties properties) {
        resolve("firstName", target, String.class, properties);
        resolve("lastName", target, String.class, properties);
        resolve("age", target, int.class, properties);
        resolve("emailAddress", target, String.class, properties);
//        resolve("address", target, EmployeeAr.class, properties);
//        resolve("contactInfo", target, Phone[].class, properties);
        resolve("address", target, Address.class, properties);
        resolve("contactInfo", target, List.class, properties);
    }
}
