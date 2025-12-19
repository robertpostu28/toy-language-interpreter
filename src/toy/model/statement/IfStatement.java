package toy.model.statement;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.exp.Exp;
import toy.model.value.Value;
import toy.model.type.BoolType;
import toy.model.value.BoolValue;

public class IfStatement implements Statement {
    private final Exp condition;
    private final Statement thenStatement;
    private final Statement elseStatement;

    public IfStatement(Exp condition, Statement thenStatement, Statement elseStatement) {
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        Dictionary<String, Value> symTable = state.getSymTable();
        Value condValue = condition.eval(symTable, state.getHeap());
        if (!condValue.getType().equals(new BoolType())) {
            throw new InterpreterException("Condition expression is not of type Bool (found " + condValue.getType() + ")");
        }

        boolean b = ((BoolValue) condValue).getValue();
        state.getExeStack().push(b ? thenStatement : elseStatement);
        return null;
    }

    @Override
    public String toString() {
        return "if (" + condition.toString() + ") then (" + thenStatement.toString() + ") else (" + elseStatement.toString() + ")";
    }
}
