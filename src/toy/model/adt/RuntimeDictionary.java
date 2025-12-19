package toy.model.adt;

import toy.exceptions.InterpreterException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RuntimeDictionary<K, V> implements Dictionary<K, V> {
    private final Map<K, V> map;

    public RuntimeDictionary() {
        this.map = new HashMap<>();
    }

    @Override
    public boolean isDefined(K key) {
        return map.containsKey(key);
    }

    @Override
    public V lookup(K key) throws InterpreterException {
        if (!isDefined(key)) {
            throw new InterpreterException("Key " + key + " is not defined in the symbol table. Lookup failed.");
        }
        return map.get(key);
    }

    @Override
    public void update(K key, V value) {
        map.put(key, value);
    }

    @Override
    public void remove(K key) throws InterpreterException {
        if (!isDefined(key)) {
            throw new InterpreterException("Key " + key + " is not defined in the symbol table. Remove failed.");
        }
        map.remove(key);
    }

    @Override
    public Dictionary<K, V> deepCopy() {
        Dictionary<K, V> copy = new RuntimeDictionary<>();
        for (var entry : this.asMap().entrySet()) {
            copy.update(entry.getKey(), entry.getValue());
        }
        return copy;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Map<K, V> asMap() {
        return Collections.unmodifiableMap(map);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
