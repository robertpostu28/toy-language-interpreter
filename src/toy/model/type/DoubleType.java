package toy.model.type;

import toy.model.value.DoubleValue;
import toy.model.value.Value;

public class DoubleType implements Type {
    @Override
    public boolean equals(Object another) {
        return another instanceof DoubleType; // Check if the other object is also an instance of DoubleType
    }

    @Override
    public Value defaultValue() {
        return new DoubleValue(0.0);
    }

    @Override
    public String toString() {
        return "double";
    }
}
