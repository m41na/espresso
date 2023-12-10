package works.hop.presso.json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NodeValue<T> {

    T value;

    public NodeValue(T value) {
        this.value = value;
    }

    public T getValue(Class<T> type) {
        return type.cast(getValue());
    }

    public Boolean is(Class<?> type) {
        return type == value.getClass();
    }
}
