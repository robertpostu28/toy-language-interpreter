package toy.model.adt;

import toy.exceptions.InterpreterException;

import java.util.List;

public interface Sequence<T> {
    void addStatement(T statement);
    List<T> asList();
    int size();

    String toString();
}
