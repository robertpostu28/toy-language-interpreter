package toy.model.statement;

import toy.exceptions.InterpreterException;

public class NoOpStatement implements Statement {
    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        // No operation performed, simply return the current state
        return state;
    }

    @Override
    public String toString() {
        return "NoOp";
    }
}
