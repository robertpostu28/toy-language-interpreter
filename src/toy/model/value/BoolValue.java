package toy.model.value;

import toy.model.type.BoolType;
import toy.model.type.Type;

public class BoolValue implements Value {
    private final boolean value;

    public BoolValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return new BoolType();
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        if (!(another instanceof BoolValue)) return false;
        return this.value == ((BoolValue) another).getValue();
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
