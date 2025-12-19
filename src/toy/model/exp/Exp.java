package toy.model.exp;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.adt.Heap;
import toy.model.value.Value;
import toy.model.type.Type;

public interface Exp {
    Value eval(Dictionary<String, Value> symTable, Heap heap) throws InterpreterException;
    Type typeCheck(Dictionary<String, Type> typeEnv) throws InterpreterException;
}
