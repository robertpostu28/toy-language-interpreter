package toy.model.value;

import toy.model.type.Type;
import toy.model.type.DoubleType;

public class DoubleValue implements Value {
    private final double value;

    public DoubleValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return new DoubleType();
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        if (!(another instanceof DoubleValue)) return false;
        return this.value == ((DoubleValue) another).getValue();
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
