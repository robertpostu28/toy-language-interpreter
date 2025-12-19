package toy.model.statement;

import toy.exceptions.InterpreterException;
import toy.model.adt.Sequence;
import toy.model.adt.Dictionary;
import toy.model.exp.Exp;
import toy.model.statement.PrgState;
import toy.model.type.Type;
import toy.model.value.Value;

public class PrintStatement implements Statement {
    private final Exp exp;

    public PrintStatement(Exp exp) {
        this.exp = exp;
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws InterpreterException {
        exp.typeCheck(typeEnv);
        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        Dictionary<String, Value> symTable = state.getSymTable();
        Sequence<Value> out = state.getOut();
        out.addStatement(exp.eval(symTable, state.getHeap()));
        return null;
    }

    @Override
    public String toString() {
        return "print(" + exp.toString() + ")";
    }
}
