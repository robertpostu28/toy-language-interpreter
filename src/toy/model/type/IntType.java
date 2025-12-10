package toy.model.type;

import toy.model.value.IntValue;
import toy.model.value.Value;

public class IntType implements Type {
    @Override
    public boolean equals(Object another) {
        return another instanceof IntType; // Check if the other object is also an instance of IntType
    }

    @Override
    public Value defaultValue() {
        return new IntValue(0);
    }

    @Override
    public String toString() {
        return "int";
    }
}
