package toy.model.adt;

import toy.exceptions.InterpreterException;
import toy.model.value.Value;

import java.util.Map;

public interface Heap {
    int allocate(Value value);
    boolean isDefined(int address);
    Value lookup(int address) throws InterpreterException;
    void update(int address, Value value) throws InterpreterException;
    Map<Integer,Value> getContent();
    void setContent(Map<Integer,Value> newContent); // used for garbage collection - replace the whole heap content
}
