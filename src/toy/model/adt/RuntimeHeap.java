package toy.model.adt;

import toy.exceptions.InterpreterException;
import toy.model.value.Value;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RuntimeHeap implements Heap {
    private final Map<Integer, Value> map;
    private int nextFreeAddress;

    public RuntimeHeap() {
        this.map = new HashMap<>();
        this.nextFreeAddress = 1; // start addresses from 1, 0 is reserved for null reference
    }

    @Override
    public int allocate(Value value) {
        int address = nextFreeAddress;

        while (map.containsKey(address)) {
            address++;
        } // to ensure we find a free address and we don't overwrite existing values

        map.put(address, value);
        nextFreeAddress = address + 1;
        return address;
    }

    @Override
    public boolean isDefined(int address) {
        return map.containsKey(address);
    }

    @Override
    public Value lookup(int address) throws InterpreterException {
        if (!isDefined(address)) {
            throw new InterpreterException("Heap lookup error: address " + address + " is not defined.");
        }
        return map.get(address);
    }

    @Override
    public void update(int address, Value value) throws InterpreterException {
        if (!isDefined(address)) {
            throw new InterpreterException("Heap update error: address " + address + " is not defined.");
        }
        map.put(address, value);
    }

    @Override
    public Map<Integer, Value> getContent() {
        return Collections.unmodifiableMap(map);
    }

    @Override
    public void setContent(Map<Integer, Value> newContent) {
        map.clear();
        map.putAll(newContent);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
