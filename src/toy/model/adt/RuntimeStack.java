package toy.model.adt;

import toy.exceptions.InterpreterException;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class RuntimeStack<T> implements Stack<T> {
    private final Deque<T> stack;

    public RuntimeStack() {
        this.stack = new ArrayDeque<>();
    }

    @Override
    public void push(T value) {
        stack.push(value);
    }

    @Override
    public T pop() throws InterpreterException {
        if (stack.isEmpty()) {
            throw new InterpreterException("Stack is empty. Cannot pop.");
        }
        return stack.pop();
    }

    @Override
    public T peek() throws InterpreterException {
        if (stack.isEmpty()) {
            throw new InterpreterException("Stack is empty. Cannot peek.");
        }
        return stack.peek();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public int size() {
        return stack.size();
    }

    @Override
    public String toString() {
        // show top first [top|...|bottom]
        StringBuilder sb = new StringBuilder("[");
        Iterator<T> iterator = stack.iterator(); // iterator goes top -> bottom for ArrayDeque's push()

        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext()) {
                sb.append("|");
            }
        }

        sb.append("]");
        return sb.toString();
    }
}
