package toy.model.adt;

import toy.exceptions.InterpreterException;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class RuntimeSequence<T> implements Sequence<T> {
    private final List<T> list;

    public RuntimeSequence() {
        this.list = new ArrayList<>();
    }

    @Override
    public void addStatement(T statement) {
        list.add(statement);
    }

    @Override
    public List<T> asList() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
