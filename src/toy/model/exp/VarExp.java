package toy.model.exp;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.adt.Heap;
import toy.model.value.Value;
import toy.model.type.Type;

public class VarExp implements Exp {
    private final String id;

    public  VarExp(String id) {
        this.id = id;
    }

    @Override
    public Type typeCheck(Dictionary<String, Type> typeEnv) throws InterpreterException {
        return typeEnv.lookup(id); // Retrieve the type associated with the variable identifier, throwing an exception if not found
    }

    @Override
    public Value eval(Dictionary<String, Value> symTable, Heap heap) throws InterpreterException {
        return symTable.lookup(id); // Retrieve the value associated with the variable identifier, throwing an exception if not found
    }

    @Override
    public String toString() {
        return id;
    }
}
