package toy.model.exp;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.adt.Heap;
import toy.model.value.Value;

public class VarExp implements Exp {
    private final String id;

    public  VarExp(String id) {
        this.id = id;
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
