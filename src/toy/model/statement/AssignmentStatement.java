package toy.model.statement;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.exp.Exp;
import toy.model.value.Value;
import toy.model.type.Type;

public class AssignmentStatement implements Statement {
    private final String id;
    private final Exp exp;

    public AssignmentStatement(String id, Exp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        Dictionary<String, Value> sym = state.getSymTable();

        if (!sym.isDefined(id)) {
            throw new InterpreterException("Assignment: variable " + id + " not declared.");
        }

        Value val = exp.eval(sym, state.getHeap());
        Type typeId = (sym.lookup(id)).getType();
        if (!val.getType().equals(typeId)) {
            throw new InterpreterException("Assignment: type mismatch for variable " + id + " (expected " + typeId + ", got " + val.getType() + ").");
        }
        sym.update(id, val);
        return state;
    }

    @Override
    public String toString() {
        return id + " = " + exp.toString();
    }
}
