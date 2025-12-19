package toy.model.statement;

import toy.exceptions.InterpreterException;
import toy.model.type.Type;
import toy.model.adt.Dictionary;
import toy.model.statement.PrgState;

public class CompoundStatement implements Statement {
    private final Statement first;
    private final Statement second;

    public CompoundStatement(Statement first, Statement second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        // push second, then first - first will be executed next
        state.getExeStack().push(second);
        state.getExeStack().push(first);
        return null;
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws InterpreterException {
        Dictionary<String, Type> updatedEnv = first.typeCheck(typeEnv);
        return second.typeCheck(updatedEnv);
    }

    @Override
    public String toString() {
        return "(" + first.toString() + "; " + second.toString() + ")";
    }
}
