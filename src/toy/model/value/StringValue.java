package toy.model.value;

import toy.model.type.StringType;
import toy.model.type.Type;

public class StringValue implements Value {
    private final String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return new StringType();
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        if (!(another instanceof StringValue)) return false;
        return this.value.equals(((StringValue) another).getValue());
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
