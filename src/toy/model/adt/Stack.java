package toy.model.adt;

import toy.exceptions.InterpreterException;

public interface Stack<T> {
    void push(T value);
    T pop() throws InterpreterException;
    T peek() throws InterpreterException;
    boolean isEmpty();
    int size();

    String toString();
}
