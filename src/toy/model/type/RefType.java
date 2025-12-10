package toy.model.type;

import toy.model.value.RefValue;
import toy.model.value.Value;

public class RefType implements Type {
    private final Type inner; // type of the referenced value

    public RefType(Type inner) {
        this.inner = inner;
    }

    public Type getInner() {
        return inner;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof RefType)) return false;

        RefType otherRef = (RefType) other;
        return inner.equals(otherRef.inner);
    }

    @Override
    public Value defaultValue() {
        // address 0 is used to represent a null reference on heap
        return new RefValue(0, inner);
    }

    @Override
    public String toString() {
        return "Ref(" + inner.toString() + ")";
    }
}
