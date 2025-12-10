package toy.model.type;

import toy.model.value.StringValue;
import toy.model.value.Value;

public class StringType implements Type {
    @Override
    public boolean equals(Object another) {
        return another instanceof StringType; // Check if the other object is also an instance of StringType
    }

    @Override
    public Value defaultValue() {
        return new StringValue("");
    }

    @Override
    public String toString() {
        return "string";
    }
}
