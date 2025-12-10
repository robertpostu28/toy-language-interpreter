package toy.model.value;

import toy.model.type.Type;
import toy.model.type.RefType;

public class RefValue implements Value {
    private final int address; // memory address
    private final Type locationType; // type of the value stored at the address

    public RefValue(int address, Type locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddress() {
        return address;
    }

    public Type getLocationType() {
        return locationType;
    }

    @Override
    public Type getType() {
        return new RefType(locationType);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof RefValue)) return false;

        RefValue otherRef = (RefValue) other;
        return this.address == otherRef.address && this.locationType.equals(otherRef.locationType);
    }

    @Override
    public String toString() {
        return "(" + address + " -> " + locationType + ")";
    }
}
