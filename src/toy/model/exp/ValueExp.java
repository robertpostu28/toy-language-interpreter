package toy.model.exp;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.adt.Heap;
import toy.model.value.Value;
import toy.model.type.Type;

public class ValueExp implements Exp {
    private final Value value;

    public ValueExp(Value value) {
        this.value = value;
    }

    @Override
    public Type typeCheck(Dictionary<String, Type> typeEnv) throws InterpreterException {
        return value.getType();
    }

    @Override
    public Value eval(Dictionary<String, Value> symTable, Heap heap) throws InterpreterException {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
