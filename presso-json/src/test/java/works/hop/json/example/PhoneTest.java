package works.hop.json.example;

import org.junit.jupiter.api.Test;
import works.hop.presso.json.example.Phone;
import works.hop.presso.json.example.PhoneType;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneTest {

    @Test
    void test_phone_with_number_only() {
        Phone phone = new Phone(null, null, "  ", "123-456");
        String string = phone.toString();
        assertThat(string).isEqualTo("123-456");
    }

    @Test
    void test_phone_with_number_and_area_code() {
        Phone phone = new Phone(PhoneType.FAX, "", "607", "232-5466");
        String string = phone.toString();
        assertThat(string).isEqualTo("607 232-5466");
    }

    @Test
    void test_phone_with_all_values_present() {
        Phone phone = new Phone(PhoneType.FAX, "+1", "607", "232-5466");
        String string = phone.toString();
        assertThat(string).isEqualTo("+1 607 232-5466");
    }
}