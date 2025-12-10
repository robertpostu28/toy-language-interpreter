package toy.model.adt;

import toy.exceptions.InterpreterException;

import java.util.Map;

public interface Dictionary<K, V> {
    boolean isDefined(K key);
    V lookup(K key) throws InterpreterException;
    void update(K key, V value); // insert or update
    void remove(K key) throws InterpreterException;
    int size();

    Map<K, V> asMap(); // for iterations / debugging
    String toString();
}
