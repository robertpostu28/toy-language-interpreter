package toy.model.value;

import toy.model.type.Type;
import toy.model.type.IntType;

public class IntValue implements Value {
    private final int value;

    public IntValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return new IntType();
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        if (!(another instanceof IntValue)) return false;
        return this.value == ((IntValue) another).getValue();
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
