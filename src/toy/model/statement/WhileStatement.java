package toy.model.statement;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.adt.Heap;
import toy.model.adt.Stack;
import toy.model.exp.Exp;
import toy.model.type.BoolType;
import toy.model.value.BoolValue;
import toy.model.value.Value;

public class WhileStatement implements Statement {
    private final Exp condition;
    private final Statement body;

    public WhileStatement(Exp condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        Dictionary<String, Value> symTable = state.getSymTable();
        Heap heap = state.getHeap();
        Stack<Statement> stack = state.getExeStack();

        Value condVal = condition.eval(symTable, heap);

        if (!condVal.getType().equals(new BoolType())) {
            throw new InterpreterException(
                    "While: condition is not a boolean (got " + condVal.getType() + ")."
            );
        }

        BoolValue boolVal = (BoolValue) condVal;

        if (boolVal.getValue()) {
            stack.push(this);
            stack.push(body);
        }

        return state;
    }

    @Override
    public String toString() {
        return "while(" + condition.toString() + ") " + body.toString();
    }
}
