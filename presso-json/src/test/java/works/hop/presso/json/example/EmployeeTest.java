package works.hop.presso.json.example;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeTest {

    @Test
    void copy_employee_to_another_instance() throws IOException {
        Employee emp = new Employee();
        emp.setAge(40);
        emp.setLastName("Bob");
        emp.setFirstName("Jim");
        emp.setEmailAddress("jim.bob@email.com");
        Phone cell = new Phone();
        cell.setType(PhoneType.CELL);
        cell.setNumber("209-5643");
        cell.setAreaCode("563");
        cell.setCountryCode("+1");
        Phone work = new Phone();
        work.setType(PhoneType.OFFICE);
        work.setNumber("339-7708");
        work.setAreaCode("563");
        work.setCountryCode("+1");
        emp.setContactInfo(List.of(cell, work));
        Address addr = new Address();
        addr.setStreetAddress("616 Highland Terrace");
        addr.setCity("Madison");
        addr.setState("WI");
        addr.setPostalCode("53716");
        emp.setAddress(addr);

        Employee copyOf = new Employee();
        emp.convert(copyOf);

        assertThat(copyOf.getContactInfo().get(1).getType()).isEqualTo(PhoneType.OFFICE);
        assertThat(copyOf.getContactInfo().get(0).getNumber()).isEqualTo("209-5643");
        assertThat(copyOf.getEmailAddress()).isEqualTo("jim.bob@email.com");
        assertThat(copyOf.getAddress().getStreetAddress()).isEqualTo("616 Highland Terrace");
    }
}