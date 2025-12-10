package toy.model.type;

import toy.model.value.BoolValue;
import toy.model.value.Value;

public class BoolType implements Type {
    @Override
    public boolean equals(Object another) {
        return another instanceof BoolType; // Check if the other object is also an instance of BoolType
    }

    @Override
    public Value defaultValue() {
        return new BoolValue(false);
    }

    @Override
    public String toString() {
        return "bool";
    }
}
