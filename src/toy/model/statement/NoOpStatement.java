package toy.model.statement;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.type.Type;

public class NoOpStatement implements Statement {
    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        // No operation performed, simply return the current state
        return null;
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws InterpreterException {
        // No changes to the type environment
        return typeEnv;
    }

    @Override
    public String toString() {
        return "NoOp";
    }
}
